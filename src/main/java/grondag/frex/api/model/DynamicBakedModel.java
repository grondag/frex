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

package grondag.frex.api.model;

import java.util.Random;
import java.util.function.Supplier;

import org.apiguardian.api.API;

import grondag.frex.api.render.DynamicConsumer;
import net.fabricmc.fabric.api.renderer.v1.model.FabricBakedModel;
import net.fabricmc.fabric.api.renderer.v1.render.RenderContext;
import net.minecraft.block.BlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.ExtendedBlockView;

/**
 * Interface for baked models that output meshes with enhanced rendering features.
 * Can also be used to generate or customize outputs based on world state instead of
 * or in addition to block state when render chunks are rebuilt.<p>
 * 
 * Note for {@link Renderer} implementors: Fabric causes BakedModel to extend this
 * interface with {@link #isVanillaAdapter()} == true and to produce standard vertex data. 
 * This means any BakedModel instance can be safely cast to this interface without an instanceof check.
 */
@API(status = API.Status.STABLE)
public interface DynamicBakedModel extends FabricBakedModel {
    @API(status = API.Status.EXPERIMENTAL)
    void emitBlockQuads(ExtendedBlockView blockView, BlockState state, BlockPos pos, Supplier<Random> randomSupplier, RenderContext context, DynamicConsumer<DynamicBlockEmitter> dynamicConsumer);
    
    @API(status = API.Status.EXPERIMENTAL)
    void emitItemQuads(ItemStack stack, Supplier<Random> randomSupplier, RenderContext context, DynamicConsumer<DynamicItemEmitter> dynamicConsumer);
}
