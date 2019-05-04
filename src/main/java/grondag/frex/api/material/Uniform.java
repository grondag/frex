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

import org.apiguardian.api.API;
import org.joml.Matrix4f;

/**
 * Interfaces for uniform initialization. Called by renderer when uniform should
 * be potentially updated from game state.
 * <p>
 * 
 * See {@link ShaderManager} and {@link UniformRefreshFrequency}
 */
@API(status = API.Status.EXPERIMENTAL)
public interface Uniform {
    /**
     * {@link UniformMatrix4f} with current model view matrix. Binding: u_modelView.
     * Refreshed on load.
     */
    public static String UNIFORM_MODEL_VIEW = "u_modelView";

    /**
     * {@link UniformMatrix4f} with current model view projection matrix. Binding:
     * u_modelViewProjection. Refresh on load.
     */
    public static String UNIFORM_MODEL_VIEW_PROJECTION = "u_modelViewProjection";

    /**
     * {@link UniformMatrix4f} with current projection matrix. Binding:
     * u_projection. Refresh on load.
     */
    public static String UNIFORM_PROJECTION = "u_projection";

    /**
     * {@link Uniform1f} with current game time for animation. Refreshed every
     * frame.
     */
    public static String UNIFORM_TIME = "u_time";

    /**
     * {@link Uniform3f} with viewing entity eye position. Refreshed every frame.
     */
    public static String UNIFORM_EYE_POSITION = "u_eyePosition";

    /**
     * {@link Uniform3f} with fog parameters: end, end-start, and density. Zero
     * density means linear fog. Refreshed every tick.
     */
    public static String UNIFORM_FOG_ATTRIBUTES = "u_fogAttributes";

    /**
     * {@link Uniform3f} with current fog color. Refreshed every tick.
     */
    public static String UNIFORM_FOG_COLOR = "u_fogColor";
    
    @FunctionalInterface
    public interface Uniform1f extends Uniform {
        void set(float v0);
    }

    @FunctionalInterface
    public interface Uniform2f extends Uniform {
        void set(float v0, float v1);
    }

    @FunctionalInterface
    public interface Uniform3f extends Uniform {
        void set(float v0, float v1, float v2);
    }

    @FunctionalInterface
    public interface Uniform4f extends Uniform {
        void set(float v0, float v1, float v2, float v3);
    }

    @FunctionalInterface
    public interface UniformIntArray extends Uniform {
        void set(int[] v);
    }
    
    @FunctionalInterface
    public interface Uniform1i extends Uniform {
        void set(int v0);
    }

    @FunctionalInterface
    public interface Uniform2i extends Uniform {
        void set(int v0, int v1);
    }

    @FunctionalInterface
    public interface Uniform3i extends Uniform {
        void set(int v0, int v1, int v2);
    }

    @FunctionalInterface
    public interface Uniform4i extends Uniform {
        void set(int v0, int v1, int v2, int v3);
    }

    @FunctionalInterface
    public interface UniformMatrix4f extends Uniform {
        void set(Matrix4f matrix);
    }
}
