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

package grondag.frex.impl.fluid;

import java.util.IdentityHashMap;
import java.util.function.BiFunction;
import java.util.function.Function;

import grondag.frex.Frex;
import grondag.frex.api.fluid.FluidQuadSupplier;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import org.jetbrains.annotations.ApiStatus.Internal;

import net.minecraft.fluid.Fluid;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

@Internal
public class FluidQuadSupplierImpl {
	private static final Object2ObjectOpenHashMap<Identifier, Function<Fluid, FluidQuadSupplier>> FACTORIES = new Object2ObjectOpenHashMap<>();
	private static final IdentityHashMap<Fluid, FluidQuadSupplier> SUPPLIERS = new IdentityHashMap<>();
	private static BiFunction<Fluid, Function<Fluid, FluidQuadSupplier>, FluidQuadSupplier> handler;

	public static void reload() {
		SUPPLIERS.clear();

		if(handler != null) {
			Registry.FLUID.forEach(fluid -> {
				final Function<Fluid, FluidQuadSupplier> factory = FACTORIES.get(Registry.FLUID.getId(fluid));
				SUPPLIERS.put(fluid, handler.apply(fluid, factory));
			});
		}
	}

	public static FluidQuadSupplier get(Fluid forFluid) {
		return SUPPLIERS.get(forFluid);
	}

	public static void registerFactory(Function<Fluid, FluidQuadSupplier> factory, Identifier forFluidId) {
		if (FACTORIES.put(forFluidId, factory) != null) {
			Frex.LOG.warn("A FluidQuadSupplier was registered more than once for fluid " + forFluidId.toString() + ". This is probably a mod conflict and the fluid may not render correctly.");
		}
	}

	public static void setReloadHandler(BiFunction<Fluid, Function<Fluid, FluidQuadSupplier>, FluidQuadSupplier> handlerIn) {
		assert handler == null;
		handler = handlerIn;
	}
}
