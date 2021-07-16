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

package grondag.frex.impl.light;

import grondag.frex.api.light.ItemLight;

public class SimpleItemLight implements ItemLight {
	private final float intensity;
	private final float red;
	private final float green;
	private final float blue;
	private final boolean worksInFluid;
	private final int innerConeAngleDegrees;
	private final int outerConeAngleDegrees;

	public SimpleItemLight(
		float intensity,
		float red,
		float green,
		float blue,
		boolean worksInFluid,
		int innerConeAngleDegrees,
		int outerConeAngleDegrees
	) {
		this.intensity = intensity;
		this.red = red;
		this.green = green;
		this.blue = blue;
		this.worksInFluid = worksInFluid;
		this.innerConeAngleDegrees = innerConeAngleDegrees;
		this.outerConeAngleDegrees = outerConeAngleDegrees;
	}

	@Override
	public float intensity() {
		return intensity;
	}

	@Override
	public float red() {
		return red;
	}

	@Override
	public float green() {
		return green;
	}

	@Override
	public float blue() {
		return blue;
	}

	@Override
	public boolean worksInFluid() {
		return worksInFluid;
	}

	@Override
	public int innerConeAngleDegrees() {
		return innerConeAngleDegrees;
	}

	@Override
	public int outerConeAngleDegrees() {
		return outerConeAngleDegrees;
	}
}
