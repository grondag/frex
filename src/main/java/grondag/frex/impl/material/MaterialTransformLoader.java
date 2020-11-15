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

import grondag.frex.Frex;
import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;
import org.jetbrains.annotations.ApiStatus.Internal;

import net.minecraft.client.MinecraftClient;
import net.minecraft.resource.Resource;
import net.minecraft.resource.ResourceManager;
import net.minecraft.util.Identifier;

@Internal
public final class MaterialTransformLoader {
	private MaterialTransformLoader() {}

	private static final ObjectOpenHashSet<Identifier> CAUGHT = new ObjectOpenHashSet<>();

	static MaterialTransform loadTransform(String idForLog, String materialString, MaterialTransform defaultValue) {
		try {
			final MaterialTransform result = loadTransformInner(new Identifier(materialString));
			return result == null ? defaultValue : result;
		} catch (final Exception e) {
			Frex.LOG.warn("Unable to load material transform " + materialString + " for material map " + idForLog + " because of exception. Using default transform." , e);
			return defaultValue;
		}
	}

	private static MaterialTransform loadTransformInner(Identifier idIn) {
		final Identifier id = new Identifier(idIn.getNamespace(), "materials/" + idIn.getPath() + ".json");

		MaterialTransform result = null;
		final ResourceManager rm = MinecraftClient.getInstance().getResourceManager();

		try(Resource res = rm.getResource(id)) {
			result = MaterialTransformDeserializer.deserialize(MaterialLoaderImpl.readJsonObject(res));
		} catch (final Exception e) {
			// TODO:  make error suppression configurable
			if(CAUGHT.add(idIn)) {
				Frex.LOG.info("Unable to load material transform " + idIn.toString() + " due to exception " + e.toString());
			}
		}

		return result;
	}
}