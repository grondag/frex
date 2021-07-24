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

import java.util.function.BiPredicate;

import net.minecraft.entity.Entity;

import grondag.frex.api.material.RenderMaterial;

public abstract class EntityBiPredicate implements BiPredicate<Entity, RenderMaterial> {
	public static EntityBiPredicate ENTITY_ALWAYS_TRUE = new EntityBiPredicate() {
		@Override
		public boolean test(Entity entity, RenderMaterial renderMaterial) {
			return true;
		}

		@Override
		public boolean equals(Object obj) {
			return obj == ENTITY_ALWAYS_TRUE;
		}
	};

	@Override
	public abstract boolean equals(Object obj);
}
