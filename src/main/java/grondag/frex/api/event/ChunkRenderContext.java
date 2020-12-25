package grondag.frex.api.event;

import net.minecraft.client.render.block.BlockRenderManager;
import net.minecraft.client.render.chunk.ChunkRendererRegion;
import net.minecraft.client.util.math.MatrixStack;

import java.util.Random;

public interface ChunkRenderContext extends ChunkRenderConditionContext {
    ChunkRendererRegion getChunkRendererRegion();

    Random getRandom();

    BlockRenderManager getBlockRenderManager();

    GhostBlockRenderer getRenderer();

    MatrixStack getMatrixStack();
}
