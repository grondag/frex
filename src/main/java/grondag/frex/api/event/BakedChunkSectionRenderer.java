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
import java.util.function.Predicate;

@Environment(EnvType.CLIENT)
public interface BakedChunkSectionRenderer {
    ConditionalEvent<ChunkRenderConditionContext, BakedChunkSectionRenderer> EVENT =
            new ArrayBackedConditionalEvent<>(listeners -> (context) -> {
                ChunkRenderConditionContext conditionContext = new ChunkRenderConditionContext(context.chunkSectionStartPos, context.chunkSectionEndPos);
                for (Pair<Predicate<ChunkRenderConditionContext>, BakedChunkSectionRenderer> pair : listeners) {
                    if (pair.getLeft().test(conditionContext)) {
                        pair.getRight().bake(context);
                    }
                }
            }, null);

    void bake(ChunkRenderContext context);

    class ChunkRenderContext {
        protected ChunkRendererRegion chunkRendererRegion;
        protected Random random;
        protected BlockRenderManager blockRenderManager;
        protected GhostBlockRenderer renderer;
        protected BlockPos chunkSectionStartPos;
        protected BlockPos chunkSectionEndPos;
        protected MatrixStack matrixStack;

        public ChunkRenderContext(ChunkRendererRegion chunkRendererRegion, BlockRenderManager blockRenderManager, BlockPos chunkSectionStartPos, BlockPos chunkSectionEndPos, Random random, MatrixStack matrixStack, GhostBlockRenderer renderer) {
            this.chunkRendererRegion = chunkRendererRegion;
            this.blockRenderManager = blockRenderManager;
            this.chunkSectionStartPos = chunkSectionStartPos;
            this.chunkSectionEndPos = chunkSectionEndPos;
            this.random = random;
            this.renderer = renderer;
            this.matrixStack = matrixStack;
        }

        public ChunkRendererRegion getChunkRendererRegion() {
            return chunkRendererRegion;
        }

        public Random getRandom() {
            return random;
        }

        public BlockRenderManager getBlockRenderManager() {
            return blockRenderManager;
        }

        public GhostBlockRenderer getRenderer() {
            return renderer;
        }

        public BlockPos getChunkSectionStartPos() {
            return chunkSectionStartPos;
        }

        public BlockPos getChunkSectionEndPos() {
            return chunkSectionEndPos;
        }

        public MatrixStack getMatrixStack() {
            return matrixStack;
        }

        public static class Mutable extends ChunkRenderContext {
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