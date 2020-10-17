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

package grondag.frex.api.material;

import grondag.frex.impl.material.CompoundMaterialImpl;
import org.jetbrains.annotations.Nullable;

import net.minecraft.util.Identifier;

import net.fabricmc.fabric.api.renderer.v1.material.RenderMaterial;

public interface CompoundMaterial {
	int depth();

	RenderMaterial material(int index);

	/**
	 * Register a compound material for re-use by other mods or models within a mod.
	 * The registry does not persist registrations - mods must create and register
	 * all materials at game initialization.
	 *
	 * <p>Returns false if a material with the given identifier is already present,
	 * leaving the existing material intact.
	 */
	static boolean register(Identifier id, CompoundMaterial material) {
		return CompoundMaterialImpl.register(id, material);
	}

	/**
	 * Return a material previously registered via {@link #registerMaterial(Identifier, RenderMaterial)}.
	 * Will return null if no material was found matching the given identifier.
	 */
	@Nullable
	static CompoundMaterial get(Identifier id) {
		return CompoundMaterialImpl.get(id);
	}
}
