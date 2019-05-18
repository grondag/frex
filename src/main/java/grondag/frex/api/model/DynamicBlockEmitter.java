package grondag.frex.api.model;

import org.apiguardian.api.API;

import net.fabricmc.fabric.api.renderer.v1.render.RenderContext;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockView;

@API(status = API.Status.EXPERIMENTAL)
@FunctionalInterface
public interface DynamicBlockEmitter {
    public void emit(BlockView blockView, BlockPos pos, RenderContext context);
}
