package grondag.frex.api;

import java.util.function.Supplier;

import net.fabricmc.fabric.api.client.model.fabric.RenderMaterial;

/**
 * Allows creation of shader-based custom materials by attaching shaders to
 * standard materials. The standard material controls vertex attribute bindings
 * and lighting model interaction.
 */
public interface ShaderManager {
    /**
     * Creates a new material using a standard material plus shaders. The standard
     * material controls how much information will be sent to the shader (via
     * texture depth) and if/how/which vertex colors will be modified by lighting.
     * <p>
     * 
     * See {@link QuadMaker} for standard vertex attribute binding names. Uniforms
     * will be automatically associated with a shader if the uniform name appears in
     * the shader header declarations.
     * <p>
     * 
     * Renderers should re-query sources and recompile shaders on resource reload
     * and whenever renderer reload occurs. (Happens when user presses F3+A or
     * changes some graphics settings.) This allows shader code distribution via
     * resource packs and enables shader debugging without a game restart.
     * <p>
     * 
     * @param standard       Must be a standard material.
     * @param vertexSource   Supplier for GLSL 120 vertex shader source. Renderer
     *                       will strip redundant header declarations.
     * @param fragmentSource Supplier for GLSL 120 fragment shader source. Renderer
     *                       will strip redundant header declarations.
     * @param flags          - Flags to control other shader configuration. Reserved
     *                       for future use.
     * 
     * @return The new material. Material will act as a standard material if source
     *         compilation fails.
     */
    ShaderMaterial shaderMaterial(RenderMaterial baseMaterial, Supplier<String> vertexSource,
            Supplier<String> fragmentSource, int flags);
}