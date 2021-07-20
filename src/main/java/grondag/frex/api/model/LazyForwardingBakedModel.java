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

package grondag.frex.api.model;

import java.util.List;
import java.util.Random;
import java.util.function.Supplier;

import net.minecraft.block.BlockState;
import net.minecraft.client.render.model.BakedModel;
import net.minecraft.client.render.model.BakedQuad;
import net.minecraft.client.render.model.json.ModelOverrideList;
import net.minecraft.client.render.model.json.ModelTransformation;
import net.minecraft.client.texture.Sprite;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.BlockRenderView;

import net.fabricmc.fabric.api.renderer.v1.model.FabricBakedModel;
import net.fabricmc.fabric.api.renderer.v1.render.RenderContext;

/**
 * Improved base class for specialized model implementations that need to wrap other baked models.
 * Avoids boilerplate code for pass-through methods.}.
 */
public abstract class LazyForwardingBakedModel implements BakedModel, FabricBakedModel {
	protected BakedModel lazyWrapped;

	/** MUST BE THREAD-SAFE AND INVARIANT. */
	protected abstract BakedModel createWrapped();

	protected BakedModel wrapped() {
		BakedModel wrapped = lazyWrapped;

		if (wrapped == null) {
			wrapped = createWrapped();
			lazyWrapped = wrapped;
		}

		return wrapped;
	}

	@Override
	public void emitBlockQuads(BlockRenderView blockView, BlockState state, BlockPos pos, Supplier<Random> randomSupplier, RenderContext context) {
		((FabricBakedModel) wrapped()).emitBlockQuads(blockView, state, pos, randomSupplier, context);
	}

	@Override
	public boolean isVanillaAdapter() {
		return ((FabricBakedModel) wrapped()).isVanillaAdapter();
	}

	@Override
	public void emitItemQuads(ItemStack stack, Supplier<Random> randomSupplier, RenderContext context) {
		((FabricBakedModel) wrapped()).emitItemQuads(stack, randomSupplier, context);
	}

	@Override
	public List<BakedQuad> getQuads(BlockState blockState, Direction face, Random rand) {
		return wrapped().getQuads(blockState, face, rand);
	}

	@Override
	public boolean useAmbientOcclusion() {
		return wrapped().useAmbientOcclusion();
	}

	@Override
	public boolean hasDepth() {
		return wrapped().hasDepth();
	}

	@Override
	public boolean isSideLit() {
		return wrapped().isSideLit();
	}

	@Override
	public boolean isBuiltin() {
		return wrapped().isBuiltin();
	}

	@Override
	public Sprite getParticleSprite() {
		return wrapped().getParticleSprite();
	}

	@Override
	public ModelTransformation getTransformation() {
		return wrapped().getTransformation();
	}

	@Override
	public ModelOverrideList getOverrides() {
		return wrapped().getOverrides();
	}
}
