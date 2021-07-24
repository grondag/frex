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

import net.minecraft.block.BlockState;

import grondag.frex.api.material.RenderMaterial;

public class StateMaterialOnly extends StateBiPredicate {
	private MaterialPredicate materialPredicate;

	public StateMaterialOnly(MaterialPredicate materialPredicate) {
		this.materialPredicate = materialPredicate;
	}

	@Override
	public boolean test(BlockState ignored, RenderMaterial renderMaterial) {
		return materialPredicate.test(renderMaterial);
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof StateMaterialOnly) {
			return materialPredicate.equals(((StateMaterialOnly) obj).materialPredicate);
		} else {
			return false;
		}
	}
}
