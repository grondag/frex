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

package grondag.frex.impl.material;

import grondag.frex.api.material.MaterialFinder;
import grondag.frex.api.material.RenderMaterial;

@FunctionalInterface
public interface MaterialTransform {
	default RenderMaterial transform(RenderMaterial material, MaterialFinder finder) {
		finder.copyFrom(material);
		apply(finder);
		return finder.find();
	}

	void apply(MaterialFinder finder);

	MaterialTransform IDENTITY = (f) -> { };

	static MaterialTransform constant(RenderMaterial material) {
		return new MaterialTransform() {
			@Override
			public RenderMaterial transform(RenderMaterial ignored, MaterialFinder finder) {
				return material;
			}

			@Override
			public void apply(MaterialFinder finder) {
				// NOOP
			}
		};
	}
}
