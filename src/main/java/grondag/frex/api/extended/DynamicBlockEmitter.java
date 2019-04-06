package grondag.frex.api.extended;

import grondag.frex.api.core.RenderContext;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockView;

@FunctionalInterface
public interface DynamicBlockEmitter {
    public void emit(BlockView blockView, BlockPos pos, RenderContext context);
}
