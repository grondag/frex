/*
 * Copyright 2019, 2020 grondag
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License.  You may obtain a copy
 * of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */

package grondag.frex.impl.material;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

import com.google.gson.JsonObject;
import grondag.frex.Frex;
import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;
import org.jetbrains.annotations.ApiStatus.Internal;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.texture.Sprite;
import net.minecraft.client.texture.SpriteAtlasTexture;
import net.minecraft.resource.Resource;
import net.minecraft.resource.ResourceManager;
import net.minecraft.util.Identifier;
import net.minecraft.util.JsonHelper;

import net.fabricmc.fabric.api.renderer.v1.Renderer;
import net.fabricmc.fabric.api.renderer.v1.RendererAccess;
import net.fabricmc.fabric.api.renderer.v1.material.RenderMaterial;

@Internal
public final class MaterialLoaderImpl {
	private MaterialLoaderImpl() {}

	private static final ObjectOpenHashSet<Identifier> CAUGHT = new ObjectOpenHashSet<>();

	static RenderMaterial loadMaterial(String idForLog, String materialString, RenderMaterial defaultValue) {
		try {
			final RenderMaterial result = loadMaterial(new Identifier(materialString));
			return result == null ? defaultValue : result;
		} catch (final Exception e) {
			Frex.LOG.warn("Unable to load material " + materialString + " for material map " + idForLog + " because of exception. Using default material." , e);
			return defaultValue;
		}
	}

	public static synchronized RenderMaterial loadMaterial(Identifier idIn) {
		final Renderer r = RendererAccess.INSTANCE.getRenderer();
		RenderMaterial result = r.materialById(idIn);

		if(result == null) {
			result = loadMaterialInner(idIn);
			r.registerMaterial(idIn, result);
		}

		return result;
	}

	private static RenderMaterial loadMaterialInner(Identifier idIn) {
		final Identifier id = new Identifier(idIn.getNamespace(), "materials/" + idIn.getPath() + ".json");

		RenderMaterial result = null;
		final ResourceManager rm = MinecraftClient.getInstance().getResourceManager();

		try(Resource res = rm.getResource(id)) {
			result = MaterialDeserializer.deserialize(readJsonObject(res));
		} catch (final Exception e) {
			// TODO:  make error suppression configurable
			if(CAUGHT.add(idIn)) {
				Frex.LOG.info("Unable to load render material " + idIn.toString() + " due to exception " + e.toString());
			}
		}

		return result;
	}

	static Sprite loadSprite(String idForLog, String spriteId, SpriteAtlasTexture atlas, Sprite missingSprite) {
		final Sprite sprite = atlas.getSprite(new Identifier(spriteId));

		if (sprite == null || sprite == missingSprite) {
			Frex.LOG.warn("Unable to find sprite " + spriteId + " for material map " + idForLog + ". Using default material.");
			return null;
		}

		return sprite;
	}

	public static JsonObject readJsonObject(Resource res) {
		InputStream stream = null;
		InputStreamReader reader = null;
		JsonObject result = null;

		try {
			stream = res.getInputStream();
			reader = new InputStreamReader(stream, StandardCharsets.UTF_8);
			result = JsonHelper.deserialize(reader);
		} catch(final Exception e) {
			throw new RuntimeException("Unexpected error during material deserialization", e);
		} finally {
			try {
				if (reader != null) {
					reader.close();
				}

				if (stream != null) {
					stream.close();
				}
			} catch(final Exception e) {
				Frex.LOG.warn("Unexpected error during material deserialization", e);
			}
		}

		return result;
	}
}