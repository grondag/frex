package grondag.frex.impl.event;

import grondag.frex.api.event.ChunkRenderConditionContext;
import net.minecraft.util.math.BlockPos;

public class ChunkRenderConditionContextImpl implements ChunkRenderConditionContext {
    private final BlockPos startPos;
    private final BlockPos endPos;

    public ChunkRenderConditionContextImpl(BlockPos startPos, BlockPos endPos) {
        this.startPos = startPos;
        this.endPos = endPos;
    }

    @Override
    public BlockPos getStartPos() {
        return startPos;
    }

    @Override
    public BlockPos getEndPos() {
        return endPos;
    }
}
