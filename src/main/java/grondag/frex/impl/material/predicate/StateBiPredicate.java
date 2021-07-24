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

import net.minecraft.block.BlockState;

import grondag.frex.api.material.RenderMaterial;

public abstract class StateBiPredicate implements BiPredicate<BlockState, RenderMaterial> {
	public static StateBiPredicate BLOCK_ALWAYS_TRUE = new StateBiPredicate() {
		@Override
		public boolean test(BlockState blockState, RenderMaterial renderMaterial) {
			return true;
		}

		@Override
		public boolean equals(Object obj) {
			return obj == BLOCK_ALWAYS_TRUE;
		}
	};

	@Override
	public abstract boolean equals(Object obj);
}
