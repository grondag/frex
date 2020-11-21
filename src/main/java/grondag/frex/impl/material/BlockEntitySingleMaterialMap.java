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

import java.util.function.BiPredicate;

import org.jetbrains.annotations.ApiStatus.Internal;

import net.minecraft.block.BlockState;

import grondag.frex.api.material.BlockEntityMaterialMap;
import grondag.frex.api.material.MaterialFinder;
import grondag.frex.api.material.RenderMaterial;

@Internal
class BlockEntitySingleMaterialMap implements BlockEntityMaterialMap {
	private final MaterialTransform transform;
	private final BiPredicate<BlockState, RenderMaterial> predicate;

	BlockEntitySingleMaterialMap(BiPredicate<BlockState, RenderMaterial> predicate, MaterialTransform transform) {
		this.predicate = predicate;
		this.transform = transform;
	}

	@Override
	public RenderMaterial getMapped(RenderMaterial material, BlockState blockState, MaterialFinder finder) {
		return predicate.test(blockState, material) ? transform.transform(material, finder) : material;
	}
}
