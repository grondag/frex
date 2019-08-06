package grondag.frex.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.client.render.model.BakedModel;
import net.minecraft.client.render.model.BasicBakedModel;

@Mixin(BasicBakedModel.Builder.class)
public abstract class MixinHack {
    /** sole purpose is to force BakedModel into the ref map */
    @Inject(at = @At("RETURN"), method = "build")
    private void egregiousRefmapHack (CallbackInfoReturnable<BakedModel> ci) {
        //NOOP
     }
}
