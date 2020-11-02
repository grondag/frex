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

package grondag.frex.api;

import java.util.function.BooleanSupplier;

import grondag.frex.Frex;
import grondag.frex.api.material.MaterialCondition;
import grondag.frex.api.material.MaterialFinder;
import org.jetbrains.annotations.ApiStatus.Experimental;
import org.jetbrains.annotations.ApiStatus.ScheduledForRemoval;

import net.minecraft.util.Identifier;

import net.fabricmc.fabric.api.renderer.v1.RendererAccess;
import net.fabricmc.fabric.api.renderer.v1.material.RenderMaterial;

/**
 * Interface for rendering plug-ins that provide enhanced capabilities
 * for model lighting, buffering and rendering. Such plug-ins implement the
 * enhanced model rendering interfaces specified by the Fabric API.<p>
 */
public interface Renderer extends net.fabricmc.fabric.api.renderer.v1.Renderer {

	/** Will throw exception if not implemented. Check {@link Frex#isAvailable()} before calling. */
	static Renderer get() {
		if(Frex.isAvailable()) {
			return (Renderer) RendererAccess.INSTANCE.getRenderer();
		} else {
			throw new IllegalStateException("A mod tried to obtain a FREX renderer but no FREX implementation is active.");
		}
	}

	/**
	 * Obtain a new {@link MaterialFinder} instance used to retrieve
	 * standard {@link RenderMaterial} instances.<p>
	 *
	 * Renderer does not retain a reference to returned instances and they should be re-used for
	 * multiple materials when possible to avoid memory allocation overhead.
	 */
	@Override
	MaterialFinder materialFinder();

	@ScheduledForRemoval
	@Deprecated
	int maxSpriteDepth();

	@Experimental
	MaterialCondition createCondition(BooleanSupplier supplier, boolean affectBlocks, boolean affectItems);

	@Experimental
	MaterialCondition conditionById(Identifier id);

	@Experimental
	boolean registerCondition(Identifier id, MaterialCondition pipeline);
}
