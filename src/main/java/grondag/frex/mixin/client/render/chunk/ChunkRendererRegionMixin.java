package grondag.frex.mixin.client.render.chunk;

import grondag.frex.api.event.BakedChunkSectionRenderer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.chunk.ChunkRendererRegion;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.chunk.WorldChunk;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Environment(EnvType.CLIENT)
@Mixin(ChunkRendererRegion.class)
public class ChunkRendererRegionMixin {
    @Inject(method = "method_30000", at = @At("HEAD"), cancellable = true)
    private static void isChunkEmpty(BlockPos startPos, BlockPos endPos, int i, int j, WorldChunk[][] worldChunks, CallbackInfoReturnable<Boolean> cir) {
        if (BakedChunkSectionRenderer.EVENT.shouldAnyFire(new BakedChunkSectionRenderer.ChunkRenderConditionContext(startPos, endPos))) {
            cir.setReturnValue(false);
        }
    }
}
