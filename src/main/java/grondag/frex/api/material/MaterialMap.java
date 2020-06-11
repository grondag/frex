/*******************************************************************************
 * Copyright 2020 grondag
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
 ******************************************************************************/

package grondag.frex.api.material;

import javax.annotation.Nullable;

import net.minecraft.block.BlockState;
import net.minecraft.client.texture.Sprite;

import net.fabricmc.fabric.api.renderer.v1.material.RenderMaterial;

import grondag.frex.impl.material.MaterialMapImpl;

public interface MaterialMap {
	/**
	 * Used by renderer to avoid overhead of sprite reverse lookup when not needed.
	 * @return true if map is sprite-sensitive, false if always returns same material
	 */
	boolean needsSprite();

	RenderMaterial getMapped(@Nullable Sprite sprite);

	static MaterialMap get(BlockState state) {
		return MaterialMapImpl.INSTANCE.get(state);
	}

	MaterialMap DEFAULT_MATERIAL_MAP = MaterialMapImpl.DEFAULT_MATERIAL_MAP;
}
