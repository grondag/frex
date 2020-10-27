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

import java.util.IdentityHashMap;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import grondag.frex.Frex;
import grondag.frex.api.material.MaterialMap;
import org.jetbrains.annotations.ApiStatus.Internal;
import org.jetbrains.annotations.Nullable;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.texture.MissingSprite;
import net.minecraft.client.texture.Sprite;
import net.minecraft.client.texture.SpriteAtlasTexture;

import net.fabricmc.fabric.api.renderer.v1.material.RenderMaterial;

@Internal
public final class MaterialMapDeserializer {
	private MaterialMapDeserializer() {}

	public static MaterialMap loadMaterialMap(String idForLog, JsonObject mapObject, MaterialMap defaultMap, @Nullable RenderMaterial defaultMaterial) {
		if (mapObject == null || mapObject.isJsonNull()) {
			return  defaultMap;
		}

		try {
			if (mapObject.has("defaultMaterial")) {
				defaultMaterial = MaterialLoaderImpl.loadMaterial(idForLog, mapObject.get("defaultMaterial").getAsString(), defaultMaterial);
				defaultMap = new SingleMaterialMap(defaultMaterial);
			}

			if (mapObject.has("spriteMap")) {
				final SpriteAtlasTexture blockAtlas = MinecraftClient.getInstance().getBakedModelManager().method_24153(SpriteAtlasTexture.BLOCK_ATLAS_TEXTURE);
				final Sprite missingSprite = blockAtlas.getSprite(MissingSprite.getMissingSpriteId());

				final JsonArray jsonArray = mapObject.getAsJsonArray("spriteMap");
				final int limit = jsonArray.size();
				final IdentityHashMap<Sprite, RenderMaterial> spriteMap = new IdentityHashMap<>();

				for (int i = 0; i < limit; ++i) {
					final JsonObject obj = jsonArray.get(i).getAsJsonObject();

					if (!obj.has("sprite")) {
						Frex.LOG.warn("Unable to load material map " + idForLog + " because 'sprite' property is missing. Using default material.");
						continue;
					}

					final Sprite sprite = MaterialLoaderImpl.loadSprite(idForLog, obj.get("sprite").getAsString(), blockAtlas, missingSprite);

					if (sprite == null) {
						continue;
					}

					if (!obj.has("material")) {
						Frex.LOG.warn("Unable to load material map " + idForLog + " because 'material' property is missing. Using default material.");
						continue;
					}

					spriteMap.put(sprite, MaterialLoaderImpl.loadMaterial(idForLog, obj.get("material").getAsString(), defaultMaterial));
				}

				return spriteMap.isEmpty() ? defaultMap : (defaultMaterial == null ? new MultiMaterialMap(spriteMap) : new DefaultedMultiMaterialMap(defaultMaterial, spriteMap));

			} else {
				return defaultMap;
			}
		} catch (final Exception e) {
			Frex.LOG.warn("Unable to load material map " + idForLog + " because of exception. Using default material map." , e);
			return defaultMap;
		}
	}

}
