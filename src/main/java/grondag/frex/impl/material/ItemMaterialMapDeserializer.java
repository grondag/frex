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

import java.io.InputStreamReader;
import java.util.IdentityHashMap;

import com.google.gson.JsonObject;
import grondag.frex.Frex;
import grondag.frex.api.material.MaterialMap;
import org.jetbrains.annotations.ApiStatus.Internal;
import org.jetbrains.annotations.Nullable;

import net.minecraft.item.Item;
import net.minecraft.util.Identifier;
import net.minecraft.util.JsonHelper;

import net.fabricmc.fabric.api.renderer.v1.material.RenderMaterial;

@Internal
public class ItemMaterialMapDeserializer {

	public static void deserialize(Item item, Identifier idForLog, InputStreamReader reader, IdentityHashMap<Item, MaterialMap> itemMap) {
		try {
			final JsonObject json = JsonHelper.deserialize(reader);
			final String idString = idForLog.toString();

			final MaterialMap globalDefaultMap = MaterialMapLoader.DEFAULT_MAP;
			@Nullable RenderMaterial defaultMaterial = null;
			MaterialMap result = globalDefaultMap;

			if (json.has("defaultMaterial")) {
				defaultMaterial = MaterialLoaderImpl.loadMaterial(idString, json.get("defaultMaterial").getAsString(), defaultMaterial);
				result = new SingleMaterialMap(defaultMaterial);
			}

			if (json.has("defaultMap")) {
				result = MaterialMapDeserializer.loadMaterialMap(idString + "#default", json.getAsJsonObject("defaultMap"), result, defaultMaterial);
			}

			if (result != globalDefaultMap) {
				itemMap.put(item, result);
			}
		} catch (final Exception e) {
			Frex.LOG.warn("Unable to load block material map for " + idForLog.toString() + " due to unhandled exception:", e);
		}
	}
}
