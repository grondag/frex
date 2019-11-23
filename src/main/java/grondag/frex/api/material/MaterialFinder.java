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

import grondag.frex.api.Renderer;
import net.fabricmc.fabric.api.renderer.v1.material.BlendMode;
import net.fabricmc.fabric.api.renderer.v1.material.RenderMaterial;
import net.minecraft.client.render.RenderLayer;

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

	@Override
	MaterialFinder spriteDepth(int depth);

	@Override
	@Deprecated
	MaterialFinder blendMode(int spriteIndex, RenderLayer blendMode);

	@Override
	MaterialFinder blendMode(int spriteIndex, BlendMode blendMode);

	@Override
	MaterialFinder disableColorIndex(int spriteIndex, boolean disable);

	@Override
	MaterialFinder disableDiffuse(int spriteIndex, boolean disable);

	@Override
	MaterialFinder disableAo(int spriteIndex, boolean disable);

	@Override
	MaterialFinder emissive(int spriteIndex, boolean isEmissive);

	@API(status = API.Status.EXPERIMENTAL)
	MaterialFinder shader(MaterialShader pipeline);

	@API(status = API.Status.EXPERIMENTAL)
	MaterialFinder condition(MaterialCondition condition);
}
