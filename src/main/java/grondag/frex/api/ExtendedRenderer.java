package grondag.frex.api;

import net.fabricmc.fabric.api.client.model.fabric.Renderer;
import net.minecraft.util.Identifier;

public interface ExtendedRenderer extends Renderer {
    @Override
    ExtendedMaterialFinder materialFinder();

    PipelineBuilder pipelineBuilder();
    
    Pipeline pipelineById(Identifier id);
    
    boolean registerPipeline(Identifier id, Pipeline pipeline);
}
