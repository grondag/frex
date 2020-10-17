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

import grondag.frex.api.material.CompoundMaterial;
import org.jetbrains.annotations.Nullable;

import net.minecraft.util.Identifier;

import net.fabricmc.fabric.api.renderer.v1.material.RenderMaterial;

public class CompoundMaterialImpl implements CompoundMaterial {
	@Override
	public int depth() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public RenderMaterial material(int index) {
		// TODO Auto-generated method stub
		return null;
	}

	public static boolean register(Identifier id, CompoundMaterial material) {
		// TODO Auto-generated method stub
		return false;
	}

	public static @Nullable CompoundMaterial get(Identifier id) {
		// TODO Auto-generated method stub
		return null;
	}
}
