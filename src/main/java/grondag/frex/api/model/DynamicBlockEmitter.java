package grondag.frex.api.model;

import grondag.frex.api.render.RenderContext;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockView;

@FunctionalInterface
public interface DynamicBlockEmitter {
    public void emit(BlockView blockView, BlockPos pos, RenderContext context);
}
