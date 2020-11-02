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

import org.jetbrains.annotations.ApiStatus.ScheduledForRemoval;

import net.minecraft.util.Identifier;

import net.fabricmc.fabric.api.renderer.v1.material.BlendMode;

public interface RenderMaterial extends net.fabricmc.fabric.api.renderer.v1.material.RenderMaterial {
	@Deprecated
	BlendMode blendMode();

	@Deprecated
	@ScheduledForRemoval
	default boolean disableColorIndex(int spriteIndex) {
		return disableColorIndex();
	}

	boolean disableColorIndex();

	@Deprecated
	@ScheduledForRemoval
	default boolean disableDiffuse(int spriteIndex) {
		return disableDiffuse();
	}

	boolean disableDiffuse();

	@Deprecated
	@ScheduledForRemoval
	default boolean disableAo(int spriteIndex) {
		return disableAo();
	}

	boolean disableAo();

	@Deprecated
	@ScheduledForRemoval
	default boolean emissive(int spriteIndex) {
		return emissive();
	}

	boolean emissive();

	Identifier vertexShader();

	Identifier fragmentShader();

	MaterialCondition condition();

	@Override
	@Deprecated
	@ScheduledForRemoval
	default int spriteDepth() {
		return 1;
	}
}
