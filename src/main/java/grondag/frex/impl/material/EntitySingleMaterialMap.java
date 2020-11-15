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

import java.util.function.BiPredicate;

import grondag.frex.api.material.EntityMaterialMap;
import grondag.frex.api.material.MaterialFinder;
import grondag.frex.api.material.RenderMaterial;
import org.jetbrains.annotations.ApiStatus.Internal;

import net.minecraft.entity.Entity;

@Internal
class EntitySingleMaterialMap implements EntityMaterialMap {
	private final MaterialTransform transform;
	private final BiPredicate<Entity, RenderMaterial> predicate;

	EntitySingleMaterialMap(BiPredicate<Entity, RenderMaterial> predicate, MaterialTransform transform) {
		this.predicate = predicate;
		this.transform = transform;
	}

	@Override
	public RenderMaterial getMapped(RenderMaterial material, Entity entity, MaterialFinder finder) {
		return predicate.test(entity, material) ? transform.transform(material, finder) : material;
	}
}