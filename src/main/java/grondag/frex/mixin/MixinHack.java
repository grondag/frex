package grondag.frex.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import grondag.frex.Frex;
import net.minecraft.client.render.model.BakedModel;
import net.minecraft.client.render.model.BakedModelManager;

@Mixin(BakedModelManager.class)
public class MixinHack {
    private static boolean isHackNeeded = true;
    
    /** sole purpose is to force BakedModel into the ref map */
    @Inject(at = @At("HEAD"), method = "getMissingModel")
    public void frex_egregiousHack(CallbackInfoReturnable<BakedModel> ci) {
        if(isHackNeeded) {
            Frex.LOG.debug("FREX empty mixin refmap workaround activated. This is normal");
            isHackNeeded = false;
        }
    }
}
