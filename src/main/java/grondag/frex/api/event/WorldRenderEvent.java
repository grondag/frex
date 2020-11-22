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

import net.minecraft.client.render.WorldRenderer;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;

@FunctionalInterface
public interface WorldRenderEvent {
	void onRender(WorldRenderContext context);

	/**
	 * Called before world rendering executes. Input parameters are available but frustum is not.
	 * Use this event instead of injecting to the HEAD of {@link WorldRenderer#render} to avoid
	 * compatibility problems with 3rd-party renderer implementations.
	 *
	 * <p>Typical usage is for setup of state that is needed during the world render call that
	 * does not depend on the view frustum.
	 */
	Event<WorldRenderEvent> BEFORE_START = EventFactory.createArrayBacked(WorldRenderEvent.class, callbacks -> context -> {
		for (final WorldRenderEvent callback : callbacks) {
			callback.onRender(context);
		}
	});

	/**
	 * Called after view Frustum is computed and all render chunks to be rendered are
	 * identified and rebuilt but before chunks are uploaded to GPU.
	 *
	 * <p>Typical usage is for setup of state that depends on view frustum.
	 */
	Event<WorldRenderEvent> AFTER_TERRAIN_SETUP = EventFactory.createArrayBacked(WorldRenderEvent.class, callbacks -> context -> {
		for (final WorldRenderEvent callback : callbacks) {
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
	 * <p>The caller is responsible for setup and tear down of GL state appropriate for the intended output.
	 *
	 * <p>Because solid and cutout quads are depth-tested, order of output does not matter except to improve
	 * culling performance, which should not be significant after primary terrain rendering. This means
	 * mods that currently hook calls to individual render layers can simply execute them all at once when
	 * the event is called.
	 *
	 * <p>This event fires before entities and block entities are rendered and may be useful to prepare them.
	 */
	Event<WorldRenderEvent> AFTER_SOLID_TERRAIN = EventFactory.createArrayBacked(WorldRenderEvent.class, callbacks -> context -> {
		for (final WorldRenderEvent callback : callbacks) {
			callback.onRender(context);
		}
	});

	/**
	 * Called after entities and block entities are rendered.
	 * Use to run additional entity renders.
	 *
	 * <p>Satin: EntitiesPostRenderCallback.onEntitiesRendered
	 * Litematica: LitematicaRenderer.piecewiseRenderEntities
	 */
	Event<WorldRenderEvent> AFTER_ENTITIES = EventFactory.createArrayBacked(WorldRenderEvent.class, callbacks -> context -> {
		for (final WorldRenderEvent callback : callbacks) {
			callback.onRender(context);
		}
	});

	/**
	 * Called before vanilla debug renderers are output to the framebuffer.
	 * This happens very soon after entities, block breaking and most other
	 * non-translucent renders but before translucency is drawn.
	 *
	 * <p>Unlike other events, renders in this event are expected to be drawn
	 * directly and immediately to the framebuffer. The OpenGL render state view
	 * matrix will be transformed to match the camera view before the event is called.
	 *
	 * <p>Use to drawn lines, overlays and other content similar to vanilla
	 * debug renders.
	 *
	 * <p>Cloth: ClothClientHooks.DEBUG_RENDER_PRE
	 */
	Event<WorldRenderEvent> BEFORE_DEBUG_RENDER = EventFactory.createArrayBacked(WorldRenderEvent.class, callbacks -> context -> {
		for (final WorldRenderEvent callback : callbacks) {
			callback.onRender(context);
		}
	});

	/**
	 * Called when Minecraft has started and it's client about to tick for the first time.
	 *
	 * <p>This occurs while the splash screen is displayed.
	 *
	 * <p>Litematica: LitematicaRenderer.piecewiseRenderTranslucent
	 * Litematica: LitematicaRenderer.piecewiseRenderOverlay
	 * JustMap: WaypointRenderer.renderWaypoints
	 */
	Event<WorldRenderEvent> AFTER_TRANSLUCENT_TERRAIN_AND_PARTICLES = EventFactory.createArrayBacked(WorldRenderEvent.class, callbacks -> context -> {
		for (final WorldRenderEvent callback : callbacks) {
			callback.onRender(context);
		}
	});

	/**
	 * Use when want to add to translucent buffers.
	 *
	 * <p>This occurs while the splash screen is displayed.
	 *
	 * <p>VoxelMap: MixinWorldRenderer.postRenderLayer
	 * CustomSelectionBox: MixinWorldRenderer.renderWorldBorder
	 */
	Event<WorldRenderEvent> BEFORE_TRANSLUCENT_END = EventFactory.createArrayBacked(WorldRenderEvent.class, callbacks -> context -> {
		for (final WorldRenderEvent callback : callbacks) {
			callback.onRender(context);
		}
	});

	/**
	 * Called when Minecraft has started and it's client about to tick for the first time.
	 *
	 * <p>This occurs while the splash screen is displayed.
	 *
	 * <p>Satin: PostWorldRenderCallbackV2.onWorldRendered
	 * BBOR: ClientInterop.render
	 */
	Event<WorldRenderEvent> BEFORE_END = EventFactory.createArrayBacked(WorldRenderEvent.class, callbacks -> context -> {
		for (final WorldRenderEvent callback : callbacks) {
			callback.onRender(context);
		}
	});

	/**
	 * Called when Minecraft has started and it's client about to tick for the first time.
	 *
	 * <p>This occurs while the splash screen is displayed.
	 *
	 * <p>Satin: PostWorldRenderCallbackV2.onWorldRendered
	 * BBOR: ClientInterop.render
	 * VoxelMap: MixinWorldRenderer.postRender
	 */
	Event<WorldRenderEvent> AFTER_END = EventFactory.createArrayBacked(WorldRenderEvent.class, callbacks -> context -> {
		for (final WorldRenderEvent callback : callbacks) {
			callback.onRender(context);
		}
	});
}
