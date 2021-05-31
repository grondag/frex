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

package grondag.frex.impl;

import org.jetbrains.annotations.ApiStatus.Internal;

@Internal
public class RendererFeatureImpl {
	private static final long[] FLAGS = new long[128];

	public static boolean isAvailable(int featureId) {
		return (FLAGS[featureId >> 6] & (1L << (featureId & 63))) != 0;
	}

	public static void registerFeatures(int... features) {
		for (final int featureId : features) {
			FLAGS[featureId >> 6] |= (1L << (featureId & 63));
		}
	}
}
