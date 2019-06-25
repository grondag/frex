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

package grondag.frex.mixin;

import java.util.Random;
import java.util.function.Supplier;

import org.spongepowered.asm.mixin.Mixin;

import grondag.frex.api.model.DynamicBakedModel;
import grondag.frex.api.model.DynamicBlockEmitter;
import grondag.frex.api.model.DynamicItemEmitter;
import grondag.frex.api.render.DynamicConsumer;
import net.fabricmc.fabric.api.renderer.v1.render.RenderContext;
import net.minecraft.block.BlockState;
import net.minecraft.client.render.model.BakedModel;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.ExtendedBlockView;

/**
 * Avoids instanceof checks and enables consistent code path for all baked models.
 */
@Mixin(BakedModel.class)
public interface MixinBakedModel extends DynamicBakedModel {
    @Override
    default void emitBlockQuads(ExtendedBlockView blockView, BlockState state, BlockPos pos, Supplier<Random> randomSupplier, RenderContext context, DynamicConsumer<DynamicBlockEmitter> dynamicConsumer) {
        this.emitBlockQuads(blockView, state, pos, randomSupplier, context);
    }
    
    @Override
    default void emitItemQuads(ItemStack stack, Supplier<Random> randomSupplier, RenderContext context, DynamicConsumer<DynamicItemEmitter> dynamicConsumer) {
        this.emitItemQuads(stack, randomSupplier, context);
    }
}