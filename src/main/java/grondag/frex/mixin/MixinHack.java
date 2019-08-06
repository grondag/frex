package grondag.frex.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.client.render.block.BlockModelRenderer;
import net.minecraft.client.render.model.BakedModel;

@Mixin(BlockModelRenderer.class)
public abstract class MixinHack {
    /** sole purpose is to force BakedModel into the ref map */
    @Inject(at = @At("HEAD"), method = "render")
    private void egregiousRefmapHack(BakedModel bakedModel_1, float float_1, float float_2, float float_3, float float_4, CallbackInfo ci) {
        //NOOP
     }
}
