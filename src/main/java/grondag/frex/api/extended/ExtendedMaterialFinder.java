/*******************************************************************************
 * Copyright 2019 grondag
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License.  You may obtain a copy
 * of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the
 * License for the specific language governing permissions and limitations under
 * the License.
 ******************************************************************************/

package grondag.frex.api.extended;

import grondag.frex.api.core.MaterialFinder;
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
