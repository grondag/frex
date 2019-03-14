package grondag.frex.api;

import net.fabricmc.fabric.api.client.model.fabric.Renderer;

public interface ExtendedRenderer extends Renderer {
    /**
     * Access to {@link ShaderManager} for attachment of shaders to standard
     * materials. Will return null if this renderer does not support that feature.
     */
    ShaderManager shaderManager();
}
