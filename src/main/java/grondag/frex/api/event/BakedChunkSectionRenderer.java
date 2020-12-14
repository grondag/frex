package grondag.frex.api.event;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.BlockState;
import net.minecraft.client.render.block.BlockRenderManager;
import net.minecraft.client.render.chunk.ChunkRendererRegion;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Pair;
import net.minecraft.util.math.BlockPos;

import java.util.Random;
import java.util.function.Function;

@Environment(EnvType.CLIENT)
public interface BakedChunkSectionRenderer {
    ConditionalEvent<ChunkRenderConditionContext, BakedChunkSectionRenderer> EVENT =
            new ArrayBackedConditionalEvent<>(listeners -> {
                return (context) -> {
                    ChunkRenderConditionContext conditionContext = new ChunkRenderConditionContext(context.chunkSectionStartPos, context.chunkSectionEndPos);
                    for (Pair<Function<ChunkRenderConditionContext, Boolean>, BakedChunkSectionRenderer> pair : listeners) {
                        if (pair.getLeft().apply(conditionContext)) {
                            pair.getRight().bake(context);
                        }
                    }
                };
            }, null);

    void bake(ChunkRenderContext context);

    class ChunkRenderContext {
        public final ChunkRendererRegion chunkRendererRegion;
        public final BlockState state;
        public final BlockPos pos;
        public final Random random;
        public final BlockRenderManager blockRenderManager;
        public final GhostBlockRenderer renderer;
        public final BlockPos chunkSectionStartPos;
        public final BlockPos chunkSectionEndPos;
        public final MatrixStack matrixStack;

        public ChunkRenderContext(ChunkRendererRegion chunkRendererRegion, BlockState state, BlockRenderManager blockRenderManager, BlockPos chunkSectionStartPos, BlockPos chunkSectionEndPos, BlockPos pos, Random random, MatrixStack matrixStack, GhostBlockRenderer renderer) {
            this.chunkRendererRegion = chunkRendererRegion;
            this.state = state;
            this.blockRenderManager = blockRenderManager;
            this.chunkSectionStartPos = chunkSectionStartPos;
            this.chunkSectionEndPos = chunkSectionEndPos;
            this.pos = pos;
            this.random = random;
            this.renderer = renderer;
            this.matrixStack = matrixStack;
        }
    }

    interface GhostBlockRenderer {
        void bake(BlockPos pos, BlockState state);
    }

    class ChunkRenderConditionContext {
        public final BlockPos startPos;
        public final BlockPos endPos;

        public ChunkRenderConditionContext(BlockPos startPos, BlockPos endPos) {
            this.startPos = startPos;
            this.endPos = endPos;
        }
    }
}