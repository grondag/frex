package grondag.frex.mixin.client.render.chunk;

import grondag.frex.api.event.BakedChunkSectionRenderer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.impl.client.indigo.renderer.accessor.AccessChunkRendererRegion;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.client.render.block.BlockRenderManager;
import net.minecraft.client.render.chunk.BlockBufferBuilderStorage;
import net.minecraft.client.render.chunk.ChunkBuilder;
import net.minecraft.client.render.chunk.ChunkOcclusionDataBuilder;
import net.minecraft.client.render.chunk.ChunkRendererRegion;
import net.minecraft.client.render.model.BakedModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.BlockPos;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.util.Iterator;
import java.util.Random;
import java.util.Set;

@Environment(EnvType.CLIENT)
@Mixin(targets = "net/minecraft/client/render/chunk/ChunkBuilder$BuiltChunk$RebuildTask")
public class ChunkBuilderMixin {
    @Inject(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/block/BlockState;getRenderType()Lnet/minecraft/block/BlockRenderType;"), locals = LocalCapture.CAPTURE_FAILHARD)
    private void bakeGhostBlocks(float cameraX, float cameraY, float cameraZ, ChunkBuilder.ChunkData data, BlockBufferBuilderStorage buffers, CallbackInfoReturnable<Set<BlockEntity>> cir, int i, BlockPos blockPos, BlockPos blockPos2, ChunkOcclusionDataBuilder chunkOcclusionDataBuilder, Set<BlockEntity> set, ChunkRendererRegion chunkRendererRegion, MatrixStack matrixStack, Random random, BlockRenderManager blockRenderManager, Iterator<BlockPos> var15, BlockPos blockPos3, BlockState blockState) {
        BakedChunkSectionRenderer.ChunkRenderContext chunkRenderContext = new BakedChunkSectionRenderer.ChunkRenderContext(
                chunkRendererRegion,
                blockState,
                blockRenderManager,
                blockPos,
                blockPos2,
                blockPos3,
                random,
                matrixStack,
                (pos, state) -> {
                    final BakedModel model = blockRenderManager.getModel(state);
                    ((AccessChunkRendererRegion) chunkRendererRegion).fabric_getRenderer().tesselateBlock(state, pos, model, matrixStack);
                }
        );

        matrixStack.push();
        matrixStack.translate((blockPos3.getX() & 15), (blockPos3.getY() & 15), (blockPos3.getZ() & 15));

        BakedChunkSectionRenderer.EVENT.invoker().bake(chunkRenderContext);

        matrixStack.pop();
    }

}
