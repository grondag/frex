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
import java.util.function.BiPredicate;
import java.util.function.Predicate;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import grondag.frex.Frex;
import grondag.frex.api.material.BlockEntityMaterialMap;
import grondag.frex.api.material.RenderMaterial;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import org.jetbrains.annotations.ApiStatus.Internal;

import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.predicate.StatePredicate;
import net.minecraft.util.Identifier;
import net.minecraft.util.JsonHelper;

@Internal
public class BlockEntityMaterialMapDeserializer {
	private static final BiPredicate<BlockState, RenderMaterial> ALWAYS_TRUE = (s, m) -> true;

	public static void deserialize(BlockEntityType<?> key, Identifier idForLog, InputStreamReader reader, IdentityHashMap<BlockEntityType<?>, BlockEntityMaterialMap> map) {
		try {
			final JsonObject json = JsonHelper.deserialize(reader);
			final String idString = idForLog.toString();
			final BlockEntityMaterialMap defaultMap = BlockEntityMaterialMap.IDENTITY;
			MaterialTransform defaultTransform = MaterialTransform.IDENTITY;
			BlockEntityMaterialMap result = defaultMap;

			if (json.has("defaultMaterial")) {
				defaultTransform = MaterialTransformLoader.loadTransform(idString, json.get("defaultMaterial").getAsString(), defaultTransform);
				result = new BlockEntitySingleMaterialMap(ALWAYS_TRUE, defaultTransform);
			}

			if (json.has("map")) {
				result = loadBlockEntityMaterialMap(idString, json.getAsJsonArray("map"), result, defaultTransform);
			}

			if (result != defaultMap) {
				map.put(key, result);
			}
		} catch (final Exception e) {
			Frex.LOG.warn("Unable to load block entity material map for " + idForLog.toString() + " due to unhandled exception:", e);
		}
	}

	@SuppressWarnings("unchecked")
	public static BlockEntityMaterialMap loadBlockEntityMaterialMap(String idForLog, JsonArray jsonArray, BlockEntityMaterialMap defaultMap, MaterialTransform defaultTransform) {
		if (jsonArray == null || jsonArray.isJsonNull() || jsonArray.size() == 0) {
			return  defaultMap;
		}

		try {
			final int limit = jsonArray.size();
			final ObjectArrayList<BiPredicate<BlockState, RenderMaterial>> predicates = new ObjectArrayList<>();
			final ObjectArrayList<MaterialTransform> transforms = new ObjectArrayList<>();

			for (int i = 0; i < limit; ++i) {
				final JsonObject obj = jsonArray.get(i).getAsJsonObject();

				if (!obj.has("predicate")) {
					Frex.LOG.warn("Unable to load entity material map " + idForLog + " because 'predicate' tag is missing. Using default material.");
					continue;
				}

				if (!obj.has("material")) {
					Frex.LOG.warn("Unable to load entity material map " + idForLog + " because 'material' tag is missing. Using default material.");
					continue;
				}

				final BiPredicate<BlockState, RenderMaterial> predicate = loadPredicate(obj.get("predicate").getAsJsonObject());

				if (predicate == ALWAYS_TRUE) {
					Frex.LOG.warn("Empty material map predicate in " + idForLog + " was skipped. This may indicate an invalid JSON.");
				} else {
					predicates.add(predicate);
					transforms.add(MaterialTransformLoader.loadTransform(idForLog, obj.get("material").getAsString(), defaultTransform));
				}
			}

			if (predicates.isEmpty()) {
				return defaultMap;
			} else {
				predicates.add(ALWAYS_TRUE);
				transforms.add(defaultTransform);
				final int n = predicates.size();
				return new BlockEntityMultiMaterialMap(predicates.toArray(new BiPredicate[n]), transforms.toArray(new MaterialTransform[n]));
			}
		} catch (final Exception e) {
			Frex.LOG.warn("Unable to load material map " + idForLog + " because of exception. Using default material map." , e);
			return defaultMap;
		}
	}

	private static BiPredicate<BlockState, RenderMaterial> loadPredicate(JsonObject obj) {
		final StatePredicate statePredicate = StatePredicate.fromJson(obj.get("statePredicate"));
		final Predicate<RenderMaterial> materialPredicate = MaterialPredicateDeserializer.deserialize(obj.get("materialPredicate").getAsJsonObject());

		if (statePredicate == StatePredicate.ANY) {
			if (materialPredicate == MaterialPredicateDeserializer.ALWAYS_TRUE) {
				return ALWAYS_TRUE;
			} else {
				return (s, m) -> materialPredicate.test(m);
			}
		} else {
			if (materialPredicate == MaterialPredicateDeserializer.ALWAYS_TRUE) {
				return (s, m) -> statePredicate.test(s);
			} else {
				return (s, m) -> materialPredicate.test(m) && statePredicate.test(s);
			}
		}
	}
}
