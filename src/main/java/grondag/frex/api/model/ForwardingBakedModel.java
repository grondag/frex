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

import java.util.List;
import java.util.Random;
import java.util.function.Supplier;

import org.apiguardian.api.API;

import grondag.frex.api.render.RenderContext;
import grondag.frex.api.render.TerrainBlockView;
import net.minecraft.block.BlockState;
import net.minecraft.client.render.model.BakedModel;
import net.minecraft.client.render.model.BakedQuad;
import net.minecraft.client.render.model.json.ModelItemPropertyOverrideList;
import net.minecraft.client.render.model.json.ModelTransformation;
import net.minecraft.client.texture.Sprite;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;

/**
 * Base class for specialized model implementations that need to wrap other baked models.
 * Avoids boilerplate code for pass-through methods. For example usage see {@link DamageModel}.
 */
@API(status = API.Status.STABLE)
public abstract class ForwardingBakedModel implements BakedModel, DynamicBakedModel {
    /** implementations must set this somehow */
    protected BakedModel wrapped;
    
    @Override
    public void emitBlockQuads(TerrainBlockView blockView, BlockState state, BlockPos pos, Supplier<Random> randomSupplier, RenderContext context) {
        ((DynamicBakedModel)wrapped).emitBlockQuads(blockView, state, pos, randomSupplier, context);
    }

    @Override
    public boolean isVanillaAdapter() {
        return ((DynamicBakedModel)wrapped).isVanillaAdapter();
    }

    @Override
    public void emitItemQuads(ItemStack stack, Supplier<Random> randomSupplier, RenderContext context) {
        ((DynamicBakedModel)wrapped).emitItemQuads(stack, randomSupplier, context);
    }

    @Override
    public List<BakedQuad> getQuads(BlockState blockState, Direction face, Random rand) {
        return wrapped.getQuads(blockState, face, rand);
    }

    @Override
    public boolean useAmbientOcclusion() {
        return wrapped.useAmbientOcclusion();
    }

    @Override
    public boolean hasDepthInGui() {
        return wrapped.hasDepthInGui();
    }

    @Override
    public boolean isBuiltin() {
        return wrapped.isBuiltin();
    }

    @Override
    public Sprite getSprite() {
        return wrapped.getSprite();
    }

    @Override
    public ModelTransformation getTransformation() {
        return wrapped.getTransformation();
    }

    @Override
    public ModelItemPropertyOverrideList getItemPropertyOverrides() {
        return wrapped.getItemPropertyOverrides();
    }
}
