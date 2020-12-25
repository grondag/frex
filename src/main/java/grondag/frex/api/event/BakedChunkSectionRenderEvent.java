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

package grondag.frex.api.event;

import java.util.ArrayList;
import java.util.Collection;
import java.util.function.Predicate;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;

public class BakedChunkSectionRenderEvent {
	private static final Collection<Predicate<ChunkRenderConditionContext>> PREDICATES = new ArrayList<>();

	private static final Event<BakedChunkSectionRenderer> EVENT = EventFactory.createArrayBacked(BakedChunkSectionRenderer.class, listeners -> context -> {
		for (final BakedChunkSectionRenderer renderer : listeners) {
			renderer.bake(context);
		}
	});

	public static void register(Predicate<ChunkRenderConditionContext> predicate, BakedChunkSectionRenderer renderer) {
		PREDICATES.add(predicate);

		EVENT.register(context -> {
			if (predicate.test(context)) {
				renderer.bake(context);
			}
		});
	}

	public static boolean shouldAnyFire(ChunkRenderConditionContext context) {
		for (final Predicate<ChunkRenderConditionContext> predicate : PREDICATES) {
			if (predicate.test(context)) {
				return true;
			}
		}

		return false;
	}

	public static void invoke(ChunkRenderContext context) {
		EVENT.invoker().bake(context);
	}
}
