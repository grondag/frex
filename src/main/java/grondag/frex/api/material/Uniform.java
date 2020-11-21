/*
 *  Copyright 2019, 2020 grondag
 *
 *  Licensed under the Apache License, Version 2.0 (the "License"); you may not
 *  use this file except in compliance with the License.  You may obtain a copy
 *  of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 *  WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the
 *  License for the specific language governing permissions and limitations under
 *  the License.
 */

package grondag.frex.api.material;

import org.jetbrains.annotations.ApiStatus.Experimental;

import net.minecraft.util.math.Matrix3f;
import net.minecraft.util.math.Matrix4f;

/**
 * Interfaces for uniform initialization. Called by renderer when uniform should
 * be potentially updated from game state.
 *
 * <p>See {@link ShaderManager} and {@link UniformRefreshFrequency}
 */
@Experimental
public interface Uniform {
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
	public interface UniformArrayf extends Uniform {
		void set(float[] v);
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
	public interface UniformArrayi extends Uniform {
		void set(int[] v);
	}

	@FunctionalInterface
	public interface Uniform1ui extends Uniform {
		void set(int v0);
	}

	@FunctionalInterface
	public interface Uniform2ui extends Uniform {
		void set(int v0, int v1);
	}

	@FunctionalInterface
	public interface Uniform3ui extends Uniform {
		void set(int v0, int v1, int v2);
	}

	@FunctionalInterface
	public interface Uniform4ui extends Uniform {
		void set(int v0, int v1, int v2, int v3);
	}

	@FunctionalInterface
	public interface UniformArrayui extends Uniform {
		void set(int[] v);
	}

	@FunctionalInterface
	public interface UniformMatrix4f extends Uniform {
		void set(Matrix4f matrix);
	}

	@FunctionalInterface
	public interface UniformMatrix3f extends Uniform {
		void set(Matrix3f matrix);
	}
}
