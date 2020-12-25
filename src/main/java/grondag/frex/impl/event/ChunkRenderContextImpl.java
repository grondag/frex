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

import java.util.Random;

import net.minecraft.client.render.block.BlockRenderManager;
import net.minecraft.client.render.chunk.ChunkRendererRegion;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.BlockPos;

import grondag.frex.api.event.ChunkRenderContext;
import grondag.frex.api.event.GhostBlockRenderer;

public class ChunkRenderContextImpl implements ChunkRenderContext {
	protected ChunkRendererRegion chunkRendererRegion;
	protected Random random;
	protected BlockRenderManager blockRenderManager;
	protected GhostBlockRenderer renderer;
	protected BlockPos chunkSectionStartPos;
	protected BlockPos chunkSectionEndPos;
	protected MatrixStack matrixStack;

	public ChunkRenderContextImpl(ChunkRendererRegion chunkRendererRegion, BlockRenderManager blockRenderManager, BlockPos chunkSectionStartPos, BlockPos chunkSectionEndPos, Random random, MatrixStack matrixStack, GhostBlockRenderer renderer) {
		this.chunkRendererRegion = chunkRendererRegion;
		this.blockRenderManager = blockRenderManager;
		this.chunkSectionStartPos = chunkSectionStartPos;
		this.chunkSectionEndPos = chunkSectionEndPos;
		this.random = random;
		this.renderer = renderer;
		this.matrixStack = matrixStack;
	}

	@Override
	public ChunkRendererRegion getChunkRendererRegion() {
		return chunkRendererRegion;
	}

	@Override
	public Random getRandom() {
		return random;
	}

	@Override
	public BlockRenderManager getBlockRenderManager() {
		return blockRenderManager;
	}

	@Override
	public GhostBlockRenderer getRenderer() {
		return renderer;
	}

	@Override
	public BlockPos getStartPos() {
		return chunkSectionStartPos;
	}

	@Override
	public BlockPos getEndPos() {
		return chunkSectionEndPos;
	}

	@Override
	public MatrixStack getMatrixStack() {
		return matrixStack;
	}

	public static class Mutable extends ChunkRenderContextImpl {
		public Mutable() {
			super(null, null, null, null, null, null, null);
		}

		public void init(ChunkRendererRegion chunkRendererRegion, BlockRenderManager blockRenderManager, BlockPos chunkSectionStartPos, BlockPos chunkSectionEndPos, Random random, MatrixStack matrixStack, GhostBlockRenderer renderer) {
			this.chunkRendererRegion = chunkRendererRegion;
			this.blockRenderManager = blockRenderManager;
			this.chunkSectionStartPos = chunkSectionStartPos;
			this.chunkSectionEndPos = chunkSectionEndPos;
			this.random = random;
			this.renderer = renderer;
			this.matrixStack = matrixStack;
		}
	}
}
