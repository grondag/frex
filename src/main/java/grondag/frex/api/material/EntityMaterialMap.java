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

package grondag.frex.api.material;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;

import grondag.frex.impl.material.MaterialMapLoader;

/**
 * Transforms materials for entities.
 * Requires FREX material extensions.
 */
@FunctionalInterface
public interface EntityMaterialMap {
	RenderMaterial getMapped(RenderMaterial material, Entity entity, MaterialFinder finder);

	static EntityMaterialMap get(EntityType<?> entityType) {
		return MaterialMapLoader.INSTANCE.get(entityType);
	}

	EntityMaterialMap IDENTITY = (m, e, f) -> m;
}
