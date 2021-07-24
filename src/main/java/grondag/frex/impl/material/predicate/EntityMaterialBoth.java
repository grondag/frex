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

package grondag.frex.impl.material.predicate;

import net.minecraft.entity.Entity;

import grondag.frex.api.material.RenderMaterial;

public class EntityMaterialBoth extends EntityBiPredicate {
	private final EntityOnly entityOnly;
	private final MaterialPredicate materialPredicate;

	public EntityMaterialBoth(EntityOnly entityOnly, MaterialPredicate materialPredicate) {
		this.entityOnly = entityOnly;
		this.materialPredicate = materialPredicate;
	}

	@Override
	public boolean test(Entity entity, RenderMaterial renderMaterial) {
		return entityOnly.test(entity) && materialPredicate.test(renderMaterial);
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof EntityMaterialBoth) {
			return entityOnly.equals(((EntityMaterialBoth) obj).entityOnly)
					&& materialPredicate.equals(((EntityMaterialBoth) obj).materialPredicate);
		} else {
			return false;
		}
	}
}
