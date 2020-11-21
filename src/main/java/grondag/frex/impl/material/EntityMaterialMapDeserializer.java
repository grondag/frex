/*
 *  Copyright 2019, 2020 grondag
 *
 *  Licensed under the Apache License, Version 2.0 (the "License"); you may not
 *  use this file except in compliance with the License.  You may obtain a copy
 *  of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 *  WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the
 *  License for the specific language governing permissions and limitations under
 *  the License.
 */

package grondag.frex.impl.material;

import java.io.InputStreamReader;
import java.util.IdentityHashMap;
import java.util.function.BiPredicate;
import java.util.function.Predicate;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import grondag.frex.Frex;
import grondag.frex.api.material.EntityMaterialMap;
import grondag.frex.api.material.RenderMaterial;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import org.jetbrains.annotations.ApiStatus.Internal;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.util.Identifier;
import net.minecraft.util.JsonHelper;

@Internal
public class EntityMaterialMapDeserializer {
	private static final BiPredicate<Entity, RenderMaterial> ALWAYS_TRUE = (e, m) -> true;

	public static void deserialize(EntityType<?> entityType, Identifier idForLog, InputStreamReader reader, IdentityHashMap<EntityType<?>, EntityMaterialMap> map) {
		try {
			final JsonObject json = JsonHelper.deserialize(reader);
			final String idString = idForLog.toString();

			final EntityMaterialMap defaultMap = EntityMaterialMap.IDENTITY;
			MaterialTransform defaultTransform = MaterialTransform.IDENTITY;
			EntityMaterialMap result = defaultMap;

			if (json.has("defaultMaterial")) {
				defaultTransform = MaterialTransformLoader.loadTransform(idString, json.get("defaultMaterial").getAsString(), defaultTransform);
				result = new EntitySingleMaterialMap(ALWAYS_TRUE, defaultTransform);
			}

			if (json.has("map")) {
				result = loadEntityMaterialMap(idString, json.getAsJsonArray("map"), result, defaultTransform);
			}

			if (result != defaultMap) {
				map.put(entityType, result);
			}
		} catch (final Exception e) {
			Frex.LOG.warn("Unable to load entity material map for " + idForLog.toString() + " due to unhandled exception:", e);
		}
	}

	@SuppressWarnings("unchecked")
	public static EntityMaterialMap loadEntityMaterialMap(String idForLog, JsonArray jsonArray, EntityMaterialMap defaultMap, MaterialTransform defaultTransform) {
		if (jsonArray == null || jsonArray.isJsonNull() || jsonArray.size() == 0) {
			return  defaultMap;
		}

		try {
			final int limit = jsonArray.size();
			final ObjectArrayList<BiPredicate<Entity, RenderMaterial>> predicates = new ObjectArrayList<>();
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

				final BiPredicate<Entity, RenderMaterial> predicate = loadPredicate(obj.get("predicate").getAsJsonObject());

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
				return new EntityMultiMaterialMap(predicates.toArray(new BiPredicate[n]), transforms.toArray(new MaterialTransform[n]));
			}
		} catch (final Exception e) {
			Frex.LOG.warn("Unable to load material map " + idForLog + " because of exception. Using default material map." , e);
			return defaultMap;
		}
	}

	private static BiPredicate<Entity, RenderMaterial> loadPredicate(JsonObject obj) {
		final ClientEntityPredicate entityPredicate = ClientEntityPredicate.fromJson(obj.get("entityPredicate"));
		final Predicate<RenderMaterial> materialPredicate = MaterialPredicateDeserializer.deserialize(obj.get("materialPredicate").getAsJsonObject());

		if (entityPredicate == ClientEntityPredicate.ANY) {
			if (materialPredicate == MaterialPredicateDeserializer.ALWAYS_TRUE) {
				return ALWAYS_TRUE;
			} else {
				return (e, m) -> materialPredicate.test(m);
			}
		} else {
			if (materialPredicate == MaterialPredicateDeserializer.ALWAYS_TRUE) {
				return (e, m) -> entityPredicate.test(e);
			} else {
				return (e, m) -> materialPredicate.test(m) && entityPredicate.test(e);
			}
		}
	}
}
