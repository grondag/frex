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

package grondag.frex.impl.config;

import java.util.Collections;
import java.util.IdentityHashMap;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Function;

import net.fabricmc.loader.api.FabricLoader;

import grondag.frex.Frex;

public class FlawlessFramesImpl {
	private static class Controller implements Consumer<Boolean> {
		final String owner;
		boolean isActive = false;

		private Controller(String owner) {
			this.owner = owner;
		}

		@Override
		public String toString() {
			return owner;
		}

		@Override
		public void accept(Boolean isActive) {
			if (this.isActive != isActive) {
				synchronized (ACTIVE) {
					if (this.isActive) {
						ACTIVE.remove(this);
						if (enableTrace) Frex.LOG.info("Deactivating Flawless Frames at request of " + owner);
					} else {
						ACTIVE.add(this);
						if (enableTrace) Frex.LOG.info("Activating Flawless Frames at request of " + owner);
					}

					this.isActive = isActive;

					if (enableTrace) {
						Frex.LOG.info("Flawless Frames current status is " + isActive());
						if (isActive()) Frex.LOG.info("Current active controllers are: " + ACTIVE.toString());
					}
				}
			}
		}
	}

	@SuppressWarnings("unchecked")
	public static void onClientInitialization() {
		final Function<String, Consumer<Boolean>> provider = Controller::new;
		FabricLoader.getInstance().getEntrypoints("frex_flawless_frames", Consumer.class).forEach(api -> api.accept(provider));
	}

	private static final Set<Controller> ACTIVE = Collections.newSetFromMap(new IdentityHashMap<Controller, Boolean>());
	private static boolean enableTrace = false;

	public static boolean isActive() {
		return !ACTIVE.isEmpty();
	}

	public static void enableTrace(boolean enable) {
		enableTrace = enable;
	}
}
