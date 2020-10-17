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

import grondag.frex.api.Renderer;

import net.fabricmc.fabric.api.renderer.v1.material.BlendMode;

/**
 * Finds standard {@link RenderMaterial} instances used to communicate
 * quad rendering characteristics to a {@link RenderContext}.<p>
 *
 * Must be obtained via {@link Renderer#materialFinder()}.
 */
public interface MaterialFinder extends net.fabricmc.fabric.api.renderer.v1.material.MaterialFinder {
	@Override
	MaterialFinder clear();

	/**
	 * Values > 1 enable decal rendering. Sprites at index 1+
	 * are blended with the sprite at index 0.<p>
	 *
	 * Lighting, coloring and shading can be controlled for individual
	 * decal layers but geometry, vertex normals,  blend mode and condition
	 * will all be shared.<p>
	 *
	 * Decals on top of a non-translucent base layer <em>must</em> are not
	 * sorted from the camera perspective as a performance optimization.
	 * This means decals on top of cutout textures must not have any
	 * pixels with alpha > 0 if the base sprite is cutout there.
	 * Visual errors due to incorect sorting will result. <p>
	 *
	 * @param depth 1 up to  {@link Renderer#maxSpriteDepth()}
	 */
	@Override
	@Deprecated
	MaterialFinder spriteDepth(int depth);

	/**
	 * {@inheritDoc}
	 *
	 * For 1+ layers anything other than CUTOUT or CUTOUT_MIPPED
	 * will be handled as as translucent decal layer that is not
	 * perspective-sorted and does not set the depth buffer. Such
	 * layers should be always be backed by solid pixels in the
	 * the base layer.<p>
	 *
	 * 1+ CUTOUT layers should not share any pixels with other
	 * layers, otherwise Z-fighting or overwrite will occur.
	 */
	@Override
	@Deprecated
	MaterialFinder blendMode(int spriteIndex, BlendMode blendMode);

	/**
	 * Defines how base sprite pixels will be blended with the scene.
	 * Decal sprites are blended as if translucent by default.
	 *
	 * @param blendMode
	 * @return
	 */
	@Deprecated
	MaterialFinder blendMode(BlendMode blendMode);

	@Override
	@Deprecated
	default MaterialFinder disableColorIndex(int spriteIndex, boolean disable) {
		return disableColorIndex(0, disable);
	}

	MaterialFinder disableColorIndex(boolean disable);

	@Override
	@Deprecated
	default MaterialFinder disableDiffuse(int spriteIndex, boolean disable) {
		return disableDiffuse(0, disable);
	}

	MaterialFinder disableDiffuse(boolean disable);

	@Override
	@Deprecated
	default MaterialFinder disableAo(int spriteIndex, boolean disable) {
		return disableAo(0, disable);
	}

	MaterialFinder disableAo(boolean disable);

	@Override
	@Deprecated
	default MaterialFinder emissive(int spriteIndex, boolean isEmissive) {
		return emissive(0, isEmissive);
	}

	MaterialFinder emissive(boolean isEmissive);

	@Deprecated
	default MaterialFinder shader(int spriteIndex, MaterialShader shader) {
		return shader(0, shader);
	}

	MaterialFinder shader(MaterialShader shader);

	MaterialFinder condition(MaterialCondition condition);

	MaterialFinder copyFrom(RenderMaterial material);

	@Override
	RenderMaterial find();
}
