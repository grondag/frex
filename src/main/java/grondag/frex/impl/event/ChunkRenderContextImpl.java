package grondag.frex.impl.event;

import grondag.frex.api.event.BakedChunkSectionRenderer;
import grondag.frex.api.event.ChunkRenderContext;
import grondag.frex.api.event.GhostBlockRenderer;
import net.minecraft.client.render.block.BlockRenderManager;
import net.minecraft.client.render.chunk.ChunkRendererRegion;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.BlockPos;

import java.util.Random;

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