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


package grondag.frex;

import grondag.frex.impl.fluid.FluidQuadSupplierImpl;
import grondag.frex.impl.light.ItemLightLoader;
import grondag.frex.impl.material.MaterialMapLoader;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.minecraft.resource.ResourceType;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.InvalidateRenderStateCallback;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.ModContainer;

public class Frex implements ClientModInitializer {
	public static Logger LOG = LogManager.getLogger("FREX");

	private static final boolean isAvailable;

	static {
		boolean result = false;

		for(final ModContainer mod : FabricLoader.getInstance().getAllMods()) {
			if(mod.getMetadata().containsCustomValue("frex:contains_frex_renderer")) {
				result = true;
				break;
			}
		}

		isAvailable = result;
	}

	/**
	 * TODO: replace with something that indicates renderer feature set
	 */
	@Deprecated
	public static boolean isAvailable() {
		return isAvailable;
	}

	@Override
	public void onInitializeClient() {
		ResourceManagerHelper.get(ResourceType.CLIENT_RESOURCES).registerReloadListener(MaterialMapLoader.INSTANCE);
		ResourceManagerHelper.get(ResourceType.CLIENT_RESOURCES).registerReloadListener(ItemLightLoader.INSTANCE);

		FabricLoader.getInstance().getEntrypoints("frex", FrexInitializer.class).forEach(
			api -> api.onInitalizeFrex());

		InvalidateRenderStateCallback.EVENT.register(FluidQuadSupplierImpl::reload);
	}
}
