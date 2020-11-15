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

package grondag.frex.api.material;

import grondag.frex.impl.material.MaterialMapLoader;

import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntityType;

/**
 * Transforms materials for block entities.
 * Requires FREX material extensions.
 */
@FunctionalInterface
public interface BlockEntityMaterialMap {
	RenderMaterial getMapped(RenderMaterial material, BlockState blockState, MaterialFinder finder);

	static BlockEntityMaterialMap get(BlockEntityType<?> blockEntityType) {
		return MaterialMapLoader.INSTANCE.get(blockEntityType);
	}

	BlockEntityMaterialMap IDENTITY = (m, s, f) -> m;
}
