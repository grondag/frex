package grondag.frex.api.extended;

import java.util.Random;
import java.util.function.Supplier;

import grondag.frex.api.core.FabricBakedModel;
import grondag.frex.api.core.RenderContext;
import grondag.frex.api.core.TerrainBlockView;
import net.minecraft.block.BlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;

public interface DynamicBakedModel extends FabricBakedModel {
    default void emitBlockQuads(TerrainBlockView blockView, BlockState state, BlockPos pos, Supplier<Random> randomSupplier, RenderContext context, DynamicConsumer<DynamicBlockEmitter> dynamicConsumer) {
        this.emitBlockQuads(blockView, state, pos, randomSupplier, context);
    }
    
    default void emitItemQuads(ItemStack stack, Supplier<Random> randomSupplier, RenderContext context, DynamicConsumer<DynamicItemEmitter> dynamicConsumer) {
        this.emitItemQuads(stack, randomSupplier, context);
    }
}
