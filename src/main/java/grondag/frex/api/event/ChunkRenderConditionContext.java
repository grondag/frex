package grondag.frex.api.event;

import net.minecraft.util.math.BlockPos;

public interface ChunkRenderConditionContext {
    BlockPos getStartPos();

    BlockPos getEndPos();
}
