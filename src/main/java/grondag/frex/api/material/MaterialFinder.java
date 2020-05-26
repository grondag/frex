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

import net.fabricmc.fabric.api.renderer.v1.material.BlendMode;
import net.fabricmc.fabric.api.renderer.v1.material.RenderMaterial;

import grondag.frex.api.Renderer;

/**
 * Finds standard {@link RenderMaterial} instances used to communicate
 * quad rendering characteristics to a {@link RenderContext}.<p>
 *
 * Must be obtained via {@link Renderer#materialFinder()}.
 */
@API(status = API.Status.STABLE)
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
	MaterialFinder spriteDepth(int depth);

	/**
	 * {@inheritDoc}
	 *
	 * @deprecated
	 * In FREX the blend mode explicitly applies only to the base
	 * quad, not any decal layers and Indigo does not support sprite
	 * depth at all so the spriteIndex parameter is unnecessary.
	 * Use {@link #blendMode(BlendMode)} instead.
	 */
	@Deprecated
	@Override
	MaterialFinder blendMode(int spriteIndex, BlendMode blendMode);

	/**
	 * Defines how sprite pixels will be blended with the scene.
	 * Affects only the base sprite at index 0 - decal sprites are
	 * always blended as if translucent.
	 *
	 * @param blendMode
	 * @return
	 */
	MaterialFinder blendMode(BlendMode blendMode);

	@Override
	MaterialFinder disableColorIndex(int spriteIndex, boolean disable);

	@Override
	MaterialFinder disableDiffuse(int spriteIndex, boolean disable);

	@Override
	MaterialFinder disableAo(int spriteIndex, boolean disable);

	@Override
	MaterialFinder emissive(int spriteIndex, boolean isEmissive);

	@Deprecated
	@API(status = API.Status.DEPRECATED)
	MaterialFinder shader(MaterialShader pipeline);

	@Deprecated
	@API(status = API.Status.EXPERIMENTAL)
	MaterialFinder shader(int spriteIndex, MaterialShader pipeline);

	@API(status = API.Status.EXPERIMENTAL)
	MaterialFinder condition(MaterialCondition condition);
}
