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

package grondag.frex.api.light;

import net.minecraft.item.ItemStack;

import grondag.frex.impl.light.ItemLightLoader;
import grondag.frex.impl.light.SimpleItemLight;

@FunctionalInterface
public interface ItemLight {
	/**
	 * How far the light can reach.  A value of 1.0 means the light
	 * can reach the maximum configured distance, 12 blocks by default.
	 * Zero disables the light source.
	 * @return
	 */
	float intensity();

	/**
	 * Red light component.
	 * @return 0 to 1
	 */
	default float red() {
		return 1f;
	}

	/**
	 * Green light component.
	 * @return 0 to 1
	 */
	default float green() {
		return 1f;
	}

	/**
	 * Blue light component.
	 * @return 0 to 1
	 */
	default float blue() {
		return 1f;
	}

	/**
	 * Control if the light should work when submerged.
	 * @return true if works in a fluid
	 */
	default boolean worksInFluid() {
		return true;
	}

	/**
	 * Setting to a value < 360 will result in a spot light effect.
	 * This is the angle of full brightness within the light cone.
	 * Attenuation is assumed to be the same as for non-spot lights.
	 */
	default int innerConeAngleDegrees() {
		return 360;
	}

	/**
	 * The angle of reduced brightness around the inner light cone.
	 * Set to a value < 360 but greater than {@link #innerConeAngleDegrees()}
	 * to create a fall-off effect around a spot light.
	 * Attenuation is assumed to be the same as for non-spot lights.
	 */
	default int outerConeAngleDegrees() {
		return 360;
	}

	ItemLight NONE = () -> 0;

	static ItemLight get(ItemStack stack) {
		if (stack == null || stack.isEmpty()) {
			return NONE;
		} else if (stack.getItem() instanceof ItemLightProvider) {
			return ((ItemLightProvider) stack.getItem()).getItemLight(stack);
		} else {
			return ItemLightLoader.get(stack);
		}
	}

	static ItemLight of(float intensity, float red, float green, float blue, boolean worksInFluid) {
		return new SimpleItemLight(intensity, red, green, blue, worksInFluid, 360, 360);
	}

	static ItemLight of(float intensity, float red, float green, float blue, boolean worksInFluid, int innerConeAngleDegrees, int outerConeAngleDegrees) {
		innerConeAngleDegrees = Math.min(360, Math.max(1, innerConeAngleDegrees));
		outerConeAngleDegrees = Math.min(360, Math.max(innerConeAngleDegrees, outerConeAngleDegrees));
		return new SimpleItemLight(intensity, red, green, blue, worksInFluid, innerConeAngleDegrees, outerConeAngleDegrees);
	}
}
