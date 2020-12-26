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

package grondag.frex.impl.event;

import net.minecraft.block.BlockState;
import net.minecraft.client.render.block.BlockRenderManager;
import net.minecraft.client.render.chunk.ChunkRendererRegion;
import net.minecraft.client.render.model.BakedModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.BlockPos;

import net.fabricmc.fabric.impl.client.indigo.renderer.accessor.AccessChunkRendererRegion;

import grondag.frex.api.event.RenderRegionBakeListener.BlockStateRenderer;

public class BlockStateRendererImpl implements BlockStateRenderer {
	private BlockRenderManager blockRenderManager;
	private MatrixStack matrixStack;
	private ChunkRendererRegion chunkRendererRegion;

	public void prepare(BlockRenderManager blockRenderManager, MatrixStack matrixStack, ChunkRendererRegion chunkRendererRegion) {
		this.blockRenderManager = blockRenderManager;
		this.matrixStack = matrixStack;
		this.chunkRendererRegion = chunkRendererRegion;
	}

	@Override
	public void bake(BlockPos pos, BlockState state) {
		final BakedModel model = blockRenderManager.getModel(state);
		((AccessChunkRendererRegion) chunkRendererRegion).fabric_getRenderer().tesselateBlock(state, pos, model, matrixStack);
	}
}
