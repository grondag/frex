/*******************************************************************************
 * Copyright 2020 grondag
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

import javax.annotation.Nullable;

import java.io.InputStreamReader;
import java.util.IdentityHashMap;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import grondag.frex.Frex;
import grondag.frex.api.material.MaterialMap;
import org.apiguardian.api.API;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.block.BlockModels;
import net.minecraft.client.texture.MissingSprite;
import net.minecraft.client.texture.Sprite;
import net.minecraft.client.texture.SpriteAtlasTexture;
import net.minecraft.util.Identifier;
import net.minecraft.util.JsonHelper;

import net.fabricmc.fabric.api.renderer.v1.material.RenderMaterial;

@API(status = API.Status.INTERNAL)
public class BlockMaterialMapDeserializer {

	public static void deserialize(Block block, Identifier idForLog, InputStreamReader reader, IdentityHashMap<BlockState, MaterialMap> map) {
		try {
			final JsonObject json = JsonHelper.deserialize(reader);
			final String idString = idForLog.toString();

			final MaterialMap globalDefaultMap = MaterialMapImpl.DEFAULT_MAP;
			@Nullable RenderMaterial defaultMaterial = null;
			MaterialMap defaultMap = globalDefaultMap;

			if (json.has("defaultMaterial")) {
				defaultMaterial = MaterialLoaderImpl.loadMaterial(idString, json.get("defaultMaterial").getAsString(), defaultMaterial);
				defaultMap = new SingleMaterialMap(defaultMaterial);
			}

			if (json.has("defaultMap")) {
				defaultMap = loadMaterialMap(idString + "#default", json.getAsJsonObject("defaultMap"), defaultMap, defaultMaterial);
			}

			JsonObject variants = null;

			if (json.has("variants")) {
				variants = json.getAsJsonObject("variants");

				if(variants.isJsonNull()) {
					Frex.LOG.warn("Unable to load variant material maps for " + idString + " because the 'variants' block is empty. Using default map.");
					variants = null;
				}
			}

			for(final BlockState state : block.getStateManager().getStates()) {
				MaterialMap result = defaultMap;

				if (variants != null)  {
					final String stateId = BlockModels.propertyMapToString(state.getEntries());
					result = loadMaterialMap(idString + "#" + stateId, variants.getAsJsonObject(stateId), defaultMap, defaultMaterial);
				}

				if (result != globalDefaultMap) {
					map.put(state, result);
				}
			}
		} catch (final Exception e) {
			Frex.LOG.warn("Unable to load block material map for " + idForLog.toString() + " due to unhandled exception:", e);
		}
	}

	private static MaterialMap loadMaterialMap(String idForLog, JsonObject mapObject, MaterialMap defaultMap, @Nullable RenderMaterial defaultMaterial) {
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
