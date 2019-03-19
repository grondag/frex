package grondag.frex.api.extended;

import grondag.frex.api.core.Renderer;
import net.minecraft.util.Identifier;

public interface ExtendedRenderer extends Renderer {
    @Override
    ExtendedMaterialFinder materialFinder();

    PipelineBuilder pipelineBuilder();
    
    Pipeline pipelineById(Identifier id);
    
    boolean registerPipeline(Identifier id, Pipeline pipeline);
}
