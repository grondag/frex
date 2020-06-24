/*******************************************************************************
 * Copyright 2020 grondag
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

package grondag.frex.api.fkuid;

import java.util.Random;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Supplier;

import net.minecraft.fluid.Fluid;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;

import net.fabricmc.fabric.api.renderer.v1.model.FabricBakedModel;
import net.fabricmc.fabric.api.renderer.v1.render.RenderContext;

import grondag.frex.impl.fluid.FluidQuadSupplierImpl;

/**
 * Identical in operation to {@link FabricBakedModel#emitBlockQuads} but for fluids.<p>
 *
 * A FREX-compliant renderer will call this - in addition to the block quad emitter - for
 * block state with a non-empty fluid state.  Block state is passed instead of fluid state
 * to keep the method signature compact and provide access to the block state if needed. <p>
 */
@FunctionalInterface
public interface FluidQuadSupplier extends FabricBakedModel {
	@Override
	default boolean isVanillaAdapter() {
		return false;
	}

	@Override
	default void emitItemQuads(ItemStack stack, Supplier<Random> randomSupplier, RenderContext context) {
		// NOOP
	}

	static FluidQuadSupplier get(Fluid forFluid) {
		return FluidQuadSupplierImpl.get(forFluid);
	}

	/**
	 * Add a FluidQuadSupplier factory for the given fluid.
	 *
	 * Accepts a factory so that instances can be recreated when render state
	 * is invalidated. This allows implementations to cache sprites or other elements of
	 * render state without checking for or handling reloads.
	 */
	static void registerFactory(Function<Fluid, FluidQuadSupplier> factory, Identifier forFluid) {
		FluidQuadSupplierImpl.registerFactory(factory, forFluid);
	}

	/**
	 * To be called 1X by renderer implementation. Provides the logic
	 * that will implement fluid : supplier factory.
	 *
	 * Handler gets the fluid and the associated factory if available.
	 */
	static void setReloadHandler(BiFunction<Fluid, Function<Fluid, FluidQuadSupplier>, FluidQuadSupplier> handler) {
		FluidQuadSupplierImpl.setReloadHandler(handler);
	}
}
