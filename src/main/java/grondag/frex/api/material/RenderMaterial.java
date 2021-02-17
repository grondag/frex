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

import org.jetbrains.annotations.ApiStatus.ScheduledForRemoval;
import org.jetbrains.annotations.Nullable;

import net.minecraft.util.Identifier;

import net.fabricmc.fabric.api.renderer.v1.material.BlendMode;

public interface RenderMaterial extends net.fabricmc.fabric.api.renderer.v1.material.RenderMaterial {
	@Nullable BlendMode blendMode();

	boolean blur();

	MaterialCondition condition();

	boolean cull();

	boolean cutout();

	int decal();

	int depthTest();

	@Deprecated
	@ScheduledForRemoval
	default boolean disableAo(int spriteIndex) {
		return disableAo();
	}

	boolean disableAo();

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

	boolean discardsTexture();

	@Deprecated
	@ScheduledForRemoval
	default boolean emissive(int spriteIndex) {
		return emissive();
	}

	boolean emissive();

	/**
	 * @deprecated No longer used or valid. Always returns true.
	 * Will be removed in a subsequent release.
	 */
	@Deprecated
	@ScheduledForRemoval
	default boolean enableLightmap() {
		return true;
	}

	boolean flashOverlay();

	int fog();

	/**
	 * @deprecated No longer used or valid. Always returns false.
	 * Will be removed in a subsequent release.
	 */
	@Deprecated
	@ScheduledForRemoval
	default boolean gui() {
		return false;
	}

	Identifier fragmentShaderId();

	String fragmentShader();

	boolean hurtOverlay();

	boolean lines();

	boolean sorted();

	@Override
	@Deprecated
	@ScheduledForRemoval
	default int spriteDepth() {
		return 1;
	}

	int target();

	Identifier textureId();

	String texture();

	int transparency();

	boolean transparentCutout();

	boolean unmipped();

	Identifier vertexShaderId();

	String vertexShader();

	int writeMask();

	/**
	 * If this material is derived from and/or represents a vanilla {@code RenderLayer}
	 * and that layer has an associated name (given by Mojang) the name of that layer.
	 * Value is undefined in other cases.
	 *
	 * @return name of associated vanilla {@code RenderLayer} if any, undefined otherwise
	 */
	String renderLayerName();

	/**
	 * True when material should cast shadows. Pipelines that lack
	 * shadowmaps or some other mechanism for realistic shadows will
	 * ignore this.  Defaults to true.
	 *
	 * <p>Set false for materials that render as solid for practical
	 * reasons but should not cast shadows.  Beacon beams are a vanilla example.
	 *
	 * @return true if material should cast shadows
	 */
	boolean castShadows();
}
