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

package grondag.frex.api.event;

import net.minecraft.client.render.WorldRenderer;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;

// TODO: THESE ARE NOT READY YET AND SHOULD NOT BE USED

@Environment(EnvType.CLIENT)
public final class WorldRenderEvents {
	private WorldRenderEvents() {
	}

	/**
	 * Called before world rendering executes. Input parameters are available but frustum is not.
	 * Use this event instead of injecting to the HEAD of {@link WorldRenderer#render} to avoid
	 * compatibility problems with 3rd-party renderer implementations.
	 *
	 * <p>Typical usage is for setup of state that is needed during the world render call that
	 * does not depend on the view frustum.
	 */
	public static final Event<WorldRenderCallback> BEFORE_START = EventFactory.createArrayBacked(WorldRenderCallback.class, callbacks -> context -> {
		for (final WorldRenderCallback callback : callbacks) {
			callback.onRender(context);
		}
	});


	/**
	 * Called after view Frustum is computed and all render chunks to be rendered are
	 * identified and rebuilt but before chunks are uploaded to GPU.
	 *
	 * <p>Typical usage is for setup of state that depends on view frustum.
	 */
	public static final Event<WorldRenderCallback> AFTER_TERRAIN_SETUP = EventFactory.createArrayBacked(WorldRenderCallback.class, callbacks -> context -> {
		for (final WorldRenderCallback callback : callbacks) {
			callback.onRender(context);
		}
	});


	/**
	 * Called after the Solid, Cutout and Cutout Mipped terrain layers have been output to the framebuffer.
	 *
	 * <p>Typical usage is to render non-translucent terrain to the framebuffer.
	 *
	 * <p>Note that 3rd-party renderers may combine these passes or otherwise alter the
	 * rendering pipeline for sake of performance or features. This can break direct writes to the
	 * framebuffer.  Use this event for cases that cannot be satisfied by FabricBakedModel,
	 * BlockEntityRenderer or other existing abstraction. If at all possible, use an existing terrain
	 * RenderLayer instead of outputting to the framebuffer directly with GL calls.
	 *
	 * <p>The callee is responsible for setup and tear down of GL state appropriate for the intended output.
	 *
	 * <p>Because solid and cutout quads are depth-tested, order of output does not matter except to improve
	 * culling performance, which should not be significant after primary terrain rendering. This means
	 * mods that currently hook calls to individual render layers can simply execute them all at once when
	 * the event is called.
	 */
	public static final Event<WorldRenderCallback> AFTER_SOLID_TERRAIN = EventFactory.createArrayBacked(WorldRenderCallback.class, callbacks -> context -> {
		for (final WorldRenderCallback callback : callbacks) {
			callback.onRender(context);
		}
	});

	/**
	 * Called before entities are rendered.  Possibly useful
	 * for state updates needed by entity renders.
	 *
	 * Satin: EntitiesPreRenderCallback.beforeEntitiesRender
	 */
	public static final Event<WorldRenderCallback> BEFORE_ENTITIES = EventFactory.createArrayBacked(WorldRenderCallback.class, callbacks -> context -> {
		for (final WorldRenderCallback callback : callbacks) {
			callback.onRender(context);
		}
	});

	/**
	 * Called after entities are rendered. Appropriate time
	 * to append additional entity renders.
	 *
	 * Satin: EntitiesPostRenderCallback.onEntitiesRendered
	 * Litematica: LitematicaRenderer.piecewiseRenderEntities
	 */
	public static final Event<WorldRenderCallback> AFTER_ENTITIES = EventFactory.createArrayBacked(WorldRenderCallback.class, callbacks -> context -> {
		for (final WorldRenderCallback callback : callbacks) {
			callback.onRender(context);
		}
	});

	/**
	 * Called before debug renderers are output to the framebuffer.
	 *
	 * <p>Typical usage is to render.
	 *
	 * Cloth: ClothClientHooks.DEBUG_RENDER_PRE
	 */
	public static final Event<WorldRenderCallback> BEFORE_DEBUG_RENDER = EventFactory.createArrayBacked(WorldRenderCallback.class, callbacks -> context -> {
		for (final WorldRenderCallback callback : callbacks) {
			callback.onRender(context);
		}
	});

	/**
	 * Called when Minecraft has started and it's client about to tick for the first time.
	 *
	 * <p>This occurs while the splash screen is displayed.
	 *
	 * Litematica: LitematicaRenderer.piecewiseRenderTranslucent
	 * Litematica: LitematicaRenderer.piecewiseRenderOverlay
	 * JustMap: WaypointRenderer.renderWaypoints
	 */
	public static final Event<WorldRenderCallback> AFTER_TRANSLUCENT_TERRAIN_AND_PARTICLES = EventFactory.createArrayBacked(WorldRenderCallback.class, callbacks -> context -> {
		for (final WorldRenderCallback callback : callbacks) {
			callback.onRender(context);
		}
	});

	/**
	 * Use when want to add to translucent buffers.
	 *
	 * <p>This occurs while the splash screen is displayed.
	 *
	 * VoxelMap: MixinWorldRenderer.postRenderLayer
	 * CustomSelectionBox: MixinWorldRenderer.renderWorldBorder
	 */
	public static final Event<WorldRenderCallback> BEFORE_TRANSLUCENT_END = EventFactory.createArrayBacked(WorldRenderCallback.class, callbacks -> context -> {
		for (final WorldRenderCallback callback : callbacks) {
			callback.onRender(context);
		}
	});

	/**
	 * Called when Minecraft has started and it's client about to tick for the first time.
	 *
	 * <p>This occurs while the splash screen is displayed.
	 *
	 * Satin: PostWorldRenderCallbackV2.onWorldRendered
	 * BBOR: ClientInterop.render
	 */
	public static final Event<WorldRenderCallback> BEFORE_END = EventFactory.createArrayBacked(WorldRenderCallback.class, callbacks -> context -> {
		for (final WorldRenderCallback callback : callbacks) {
			callback.onRender(context);
		}
	});

	/**
	 * Called when Minecraft has started and it's client about to tick for the first time.
	 *
	 * <p>This occurs while the splash screen is displayed.
	 *
	 * Satin: PostWorldRenderCallbackV2.onWorldRendered
	 * BBOR: ClientInterop.render
	 * VoxelMap: MixinWorldRenderer.postRender
	 */
	public static final Event<WorldRenderCallback> AFTER_END = EventFactory.createArrayBacked(WorldRenderCallback.class, callbacks -> context -> {
		for (final WorldRenderCallback callback : callbacks) {
			callback.onRender(context);
		}
	});
}