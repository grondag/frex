package grondag.frex.api.model;

import org.apiguardian.api.API;

import grondag.frex.api.render.RenderContext;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockView;

@API(status = API.Status.EXPERIMENTAL)
@FunctionalInterface
public interface DynamicBlockEmitter {
    public void emit(BlockView blockView, BlockPos pos, RenderContext context);
}
