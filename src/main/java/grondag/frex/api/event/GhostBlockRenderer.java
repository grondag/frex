package grondag.frex.api.event;

import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;

public interface GhostBlockRenderer {
    void bake(BlockPos pos, BlockState state);
}
