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

package grondag.frex.api;

import grondag.frex.impl.RendererFeatureImpl;

public interface RendererFeature {
	static boolean isAvailable(int featureId) {
		return RendererFeatureImpl.isAvailable(featureId);
	}

	/**
	 * Renderers should call this exactly once during init to declare available features.
	 *
	 * @param features Array of feature flags declared here or third-party flags declared elsewhere.
	 */
	static void registerFeatures(int... features) {
		RendererFeatureImpl.registerFeatures(features);
	}

	// IDs 0 - 4095 are reserved for FREX.
	int FREX_BASE = 0;

	/** Present when registerOrUpdateMaterial is supported. */
	int UPDATE_MATERIAL_REGISTRATION = FREX_BASE;

	/** Third-party extension features begin numbering here. */
	int EXTENSION_BASE = 4096;
}
