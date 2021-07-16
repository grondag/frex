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

import java.io.InputStreamReader;

import com.google.gson.JsonObject;
import org.jetbrains.annotations.ApiStatus.Internal;

import net.minecraft.util.JsonHelper;

import grondag.frex.api.light.ItemLight;

@Internal
public class ItemLightDeserializer {
	private ItemLightDeserializer() { }

	public static ItemLight deserialize(InputStreamReader reader) {
		final JsonObject obj = JsonHelper.deserialize(reader);

		final float intensity = JsonHelper.getFloat(obj, "intensity", 0f);

		if (intensity == 0) {
			return ItemLight.NONE;
		}

		final float red = JsonHelper.getFloat(obj, "red", 1f);
		final float green = JsonHelper.getFloat(obj, "green", 1f);
		final float blue = JsonHelper.getFloat(obj, "blue", 1f);
		final boolean worksInFluid = JsonHelper.getBoolean(obj, "worksInFluid", true);
		final int innerConeAngleDegrees = JsonHelper.getInt(obj, "innerConeAngleDegrees", 360);
		final int outerConeAngleDegrees = JsonHelper.getInt(obj, "outerConeAngleDegrees", innerConeAngleDegrees);

		return ItemLight.of(intensity, red, green, blue, worksInFluid, innerConeAngleDegrees, outerConeAngleDegrees);
	}
}
