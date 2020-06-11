/*******************************************************************************
 * Copyright 2019 grondag
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
 ******************************************************************************/

package grondag.frex.impl.material;

import java.io.Reader;
import java.util.Locale;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import org.apiguardian.api.API;

import net.minecraft.util.Identifier;
import net.minecraft.util.JsonHelper;

import net.fabricmc.fabric.api.renderer.v1.Renderer;
import net.fabricmc.fabric.api.renderer.v1.RendererAccess;
import net.fabricmc.fabric.api.renderer.v1.material.BlendMode;
import net.fabricmc.fabric.api.renderer.v1.material.MaterialFinder;
import net.fabricmc.fabric.api.renderer.v1.material.RenderMaterial;

import grondag.frex.Frex;
import grondag.frex.api.material.ShaderBuilder;

@API(status = API.Status.INTERNAL)
public class MaterialDeserializer {
	private MaterialDeserializer() {}

	private static final Renderer RENDERER = RendererAccess.INSTANCE.getRenderer();
	private static final MaterialFinder FINDER = RENDERER.materialFinder();

	private static final boolean FREX;
	private static final grondag.frex.api.Renderer FREX_RENDERER;
	private static final int MAX_DEPTH;

	static {
		FREX = Frex.isAvailable();
		FREX_RENDERER = FREX ? (grondag.frex.api.Renderer)RENDERER : null;
		MAX_DEPTH = FREX ? FREX_RENDERER.maxSpriteDepth() : 1;
	}

	public static RenderMaterial deserialize(Reader reader) {
		final MaterialFinder finder = FINDER.clear();
		final JsonObject json = JsonHelper.deserialize(reader);

		if(json.has("layers")) {
			final JsonArray layers = JsonHelper.asArray(json.get("layers"), "layers");
			if(!layers.isJsonNull()) {
				final int depth = layers.size();
				if(depth > MAX_DEPTH) return null;

				for(int i = 0; i < depth; i++) {
					readLayer(layers.get(i).getAsJsonObject(), finder, i);
				}
			}
		}

		return finder.find();
	}

	private static void readLayer(JsonObject layer, MaterialFinder finder, int spriteIndex) {
		if(layer.has("fragmentSource") && layer.has("vertexSource")) {
			if(FREX) {
				final ShaderBuilder sb = FREX_RENDERER.shaderBuilder();
				sb.fragmentSource(new Identifier(JsonHelper.getString(layer, "fragmentSource")));
				sb.vertexSource(new Identifier(JsonHelper.getString(layer, "vertexSource")));
				((grondag.frex.api.material.MaterialFinder)finder).shader(spriteIndex, sb.build());
			}
		}

		if(layer.has("disableAo")) {
			finder.disableAo(spriteIndex, JsonHelper.getBoolean(layer, "disableAo", true));
		}

		if(layer.has("disableColorIndex")) {
			finder.disableColorIndex(spriteIndex, JsonHelper.getBoolean(layer, "disableColorIndex", true));
		}

		if(layer.has("disableDiffuse")) {
			finder.disableDiffuse(spriteIndex, JsonHelper.getBoolean(layer, "disableDiffuse", true));
		}

		if(layer.has("emissive")) {
			finder.emissive(spriteIndex, JsonHelper.getBoolean(layer, "emissive", true));
		}

		if(layer.has("blendMode")) {
			finder.blendMode(spriteIndex, readBlendMode(JsonHelper.getString(layer, "blendMode")));
		}
	}

	private static BlendMode readBlendMode(String val) {
		val = val.toLowerCase(Locale.ROOT);
		switch(val) {
		case "solid":
		default:
			return BlendMode.SOLID;
		case "cutout":
			return BlendMode.CUTOUT;
		case "cutout_mipped":
			return BlendMode.CUTOUT_MIPPED;
		case "translucent":
			return BlendMode.TRANSLUCENT;
		}
	}
}
