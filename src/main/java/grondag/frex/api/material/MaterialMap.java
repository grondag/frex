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

import grondag.frex.impl.material.MaterialMapImpl;
import org.jetbrains.annotations.Nullable;

import net.minecraft.block.BlockState;
import net.minecraft.client.texture.Sprite;
import net.minecraft.item.ItemStack;
import net.minecraft.particle.ParticleType;

import net.fabricmc.fabric.api.renderer.v1.material.RenderMaterial;

// TODO: add Fluid material map support

public interface MaterialMap {
	/**
	 * Used by renderer to avoid overhead of sprite reverse lookup when not needed.
	 * @return true if map is sprite-sensitive, false if always returns same material
	 */
	boolean needsSprite();

	/**
	 * Returns null if sprite is unmapped or if this is the default material map.
	 */
	@Nullable RenderMaterial getMapped(@Nullable Sprite sprite);

	static MaterialMap get(BlockState state) {
		return MaterialMapImpl.INSTANCE.get(state);
	}

	static MaterialMap getForParticle(ParticleType<?> particleType) {
		return MaterialMapImpl.INSTANCE.get(particleType);
	}

	static MaterialMap defaultMaterialMap() {
		return MaterialMapImpl.DEFAULT_MAP;
	}

	static MaterialMap get(ItemStack itemStack) {
		return MaterialMapImpl.INSTANCE.get(itemStack);
	}
}
