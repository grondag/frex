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

package grondag.frex.impl.event;

import java.util.List;
import java.util.function.Predicate;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;

import grondag.frex.api.event.RenderRegionBakeListener;
import grondag.frex.api.event.RenderRegionBakeListener.RenderRegionContext;

@Environment(EnvType.CLIENT)
public class RenderRegionBakeListenerImpl {
	@Environment(EnvType.CLIENT)
	@FunctionalInterface
	private interface BakeHandler {
		void handle(RenderRegionContext context, List<RenderRegionBakeListener> list);
	}

	private static class BakeHandlerImpl implements BakeHandler {
		private final Predicate<RenderRegionContext> predicate;
		private final RenderRegionBakeListener listener;

		private BakeHandlerImpl(Predicate<RenderRegionContext> predicate, RenderRegionBakeListener listener) {
			this.predicate = predicate;
			this.listener = listener;
		}

		@Override
		public void handle(RenderRegionContext context, List<RenderRegionBakeListener> list) {
			if (predicate.test(context)) {
				list.add(listener);
			}
		}
	}

	private static final Event<BakeHandler> EVENT = EventFactory.createArrayBacked(BakeHandler.class, listeners -> (context, list) -> {
		list.clear();

		for (final BakeHandler handler : listeners) {
			handler.handle(context, list);
		}
	});

	public static void register(Predicate<RenderRegionContext> predicate, RenderRegionBakeListener handler) {
		EVENT.register(new BakeHandlerImpl(predicate, handler));
	}

	public static void prepareInvocations(RenderRegionContext context, List<RenderRegionBakeListener> list) {
		EVENT.invoker().handle(context, list);
	}
}
