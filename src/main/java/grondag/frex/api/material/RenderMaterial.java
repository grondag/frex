/*
 * Copyright 2019, 2020 grondag
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
 */

package grondag.frex.api.material;

import net.fabricmc.fabric.api.renderer.v1.material.BlendMode;

public interface RenderMaterial extends net.fabricmc.fabric.api.renderer.v1.material.RenderMaterial {
	BlendMode blendMode();

	boolean disableColorIndex(int spriteIndex);

	boolean disableDiffuse(int spriteIndex);

	boolean disableAo(int spriteIndex);

	boolean emissive(int spriteIndex);

	MaterialShader shader(int spriteIndex);

	MaterialCondition condition();
}
