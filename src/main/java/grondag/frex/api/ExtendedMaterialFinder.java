package grondag.frex.api;

import net.fabricmc.fabric.api.client.model.fabric.MaterialFinder;
import net.minecraft.block.BlockRenderLayer;

public interface ExtendedMaterialFinder extends MaterialFinder {
    ExtendedMaterialFinder pipeline(Pipeline pipeline);
    
    @Override
    ExtendedMaterialFinder clear();

    @Override
    ExtendedMaterialFinder spriteDepth(int depth);
    
    @Override
    ExtendedMaterialFinder blendMode(int spriteIndex, BlockRenderLayer blendMode);

    @Override
    ExtendedMaterialFinder disableColorIndex(int spriteIndex, boolean disable);

    @Override
    ExtendedMaterialFinder disableDiffuse(int spriteIndex, boolean disable);

    @Override
    ExtendedMaterialFinder disableAo(int spriteIndex, boolean disable);

    @Override
    ExtendedMaterialFinder emissive(int spriteIndex, boolean isEmissive);

}
