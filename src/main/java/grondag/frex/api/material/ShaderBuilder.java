/*******************************************************************************
 * Copyright 2019 grondag
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License.  You may obtain a copy
 * of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the
 * License for the specific language governing permissions and limitations under
 * the License.
 ******************************************************************************/

package grondag.frex.api.material;

import java.util.function.Consumer;

import org.apiguardian.api.API;

import grondag.frex.api.material.Uniform.Uniform1f;
import grondag.frex.api.material.Uniform.Uniform1i;
import grondag.frex.api.material.Uniform.Uniform2f;
import grondag.frex.api.material.Uniform.Uniform2i;
import grondag.frex.api.material.Uniform.Uniform3f;
import grondag.frex.api.material.Uniform.Uniform3i;
import grondag.frex.api.material.Uniform.Uniform4f;
import grondag.frex.api.material.Uniform.Uniform4i;
import grondag.frex.api.material.Uniform.UniformArrayf;
import grondag.frex.api.material.Uniform.UniformArrayi;
import net.minecraft.util.Identifier;

/**
 * Allows creation of uniforms via one of the provided "uniform..."
 * methods. Does not expose uniform instances directly - this discourages abuse
 * of uniforms that would damage performance due to excessive updates.
 * (Per-block uniforms, for example, are not advisable.)
 * <p>
 * 
 * Using a uniform in a shader is simple: reference the uniform by name in the
 * shader GLSL declarations. The renderer will ensure the uniform is bound and
 * updated appropriately.
 * <p>
 * 
 * Uniforms share the same name space. This simplifies implementations by
 * avoiding the need to rename/rebind conflicting uniform names in the renderer.
 * Use a prefix or suffix to ensure uniqueness and/or publish names of uniforms
 * that should be reused.
 * <p>
 * 
 * Several uniforms are expected to be part of any renderer implementation that
 * supports shaders. Their names are listed below as string constants with
 * explanations of function.
 */
@API(status = API.Status.EXPERIMENTAL)
public interface ShaderBuilder {
    
    @API(status = API.Status.DEPRECATED)
    @Deprecated
    ShaderBuilder spriteDepth(int depth);
    
    /**
     * Accepts a resource id for GLSL 120 vertex shader source.
     * Renderer will strip redundant header declarations.<p>
     * 
     * Renderers should re-query sources and recompile shaders on resource reload
     * and whenever renderer reload occurs. (Happens when user presses F3+A or
     * changes some graphics settings.) This allows shader code distribution via
     * resource packs and enables shader debugging without a game restart.
     */
    ShaderBuilder vertexSource(Identifier vertexSource);

    /**
     * Accepts a Supplier for GLSL 120 fragment shader source.
     * Renderer will strip redundant header declarations.<p>
     * 
     * Renderers should re-query sources and recompile shaders on resource reload
     * and whenever renderer reload occurs. (Happens when user presses F3+A or
     * changes some graphics settings.) This allows shader code distribution via
     * resource packs and enables shader debugging without a game restart.
     */
    ShaderBuilder fragmentSource(Identifier fragmentSource);
    
    /**
     * Creates a new uniform. See {@link ShaderManager} header for additional info.
     */
    ShaderBuilder uniform1f(String name, UniformRefreshFrequency frequency, Consumer<Uniform1f> initializer);

    /**
     * Creates a new uniform. See {@link ShaderManager} header for additional info.
     */
    ShaderBuilder uniform2f(String name, UniformRefreshFrequency frequency, Consumer<Uniform2f> initializer);

    /**
     * Creates a new uniform. See {@link ShaderManager} header for additional info.
     */
    ShaderBuilder uniform3f(String name, UniformRefreshFrequency frequency, Consumer<Uniform3f> initializer);

    /**
     * Creates a new uniform. See {@link ShaderManager} header for additional info.
     */
    ShaderBuilder uniform4f(String name, UniformRefreshFrequency frequency, Consumer<Uniform4f> initializer);

    /**
     * Creates a new uniform. See {@link ShaderManager} header for additional info.
     */
    ShaderBuilder uniformArrayf(String name, UniformRefreshFrequency frequency, Consumer<UniformArrayf> initializer, int size);
    
    /**
     * Creates a new uniform. See {@link ShaderManager} header for additional info.
     */
    ShaderBuilder uniform1i(String name, UniformRefreshFrequency frequency, Consumer<Uniform1i> initializer);

    /**
     * Creates a new uniform. See {@link ShaderManager} header for additional info.
     */
    ShaderBuilder uniform2i(String name, UniformRefreshFrequency frequency, Consumer<Uniform2i> initializer);

    /**
     * Creates a new uniform. See {@link ShaderManager} header for additional info.
     */
    ShaderBuilder uniform3i(String name, UniformRefreshFrequency frequency, Consumer<Uniform3i> initializer);

    /**
     * Creates a new uniform. See {@link ShaderManager} header for additional info.
     */
    ShaderBuilder uniform4i(String name, UniformRefreshFrequency frequency, Consumer<Uniform4i> initializer);

    /**
     * Creates a new uniform. See {@link ShaderManager} header for additional info.
     */
    ShaderBuilder uniformArrayi(String name, UniformRefreshFrequency frequency, Consumer<UniformArrayi> initializer, int size);

    MaterialShader build();
}
