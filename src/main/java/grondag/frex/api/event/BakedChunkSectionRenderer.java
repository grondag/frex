package grondag.frex.api.event;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
public interface BakedChunkSectionRenderer {
    void bake(ChunkRenderContext context);
}