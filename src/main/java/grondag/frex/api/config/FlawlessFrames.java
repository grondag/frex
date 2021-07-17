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

package grondag.frex.api.config;

import grondag.frex.impl.config.FlawlessFramesImpl;

/**
 * FREX renderer access for an entry point mods like ReplayMod can use to signal the
 * renderer that rendering quality should be favored over speed because output is being
 * automatically recorded. If a renderer is playing loose with some visual elements
 * to achieve interactive frame rates it should try to disable those measures if it will
 * give a noticeable boost to quality.
 *
 * <p>At a minimum, the renderer should ensure that terrain iteration blocks the main
 * render thread until the entire visible set of chunk sections are determined and all
 * visible chunk sections have been built and can be rendered in the frame.
 *
 * <p>While this API is defined as part of FREX, it uses standard Java interfaces so that
 * it can be implemented and consumed without reference to any FREX library or any other
 * mod dependency. This is done to facilitate adoption by renderers that do not implement
 * FREX and to spare mods that do not use other FREX features from including it.
 *
 * <p>This interface and it's implementation are provided as a convenience for FREX renderers.
 * Non-FREX renderers can copy the relevant code from FREX or create their own implementation as desired.
 *
 * <p>Mods that want to request control of this feature should implement the {@code frex_flawless_frames}
 * entry point, as illustrated below. The entry point class must implement
 * {@code Consumer<Function<String, Consumer<Boolean>>>}.
 *
 * <p><pre>
 * "entrypoints": {
 *    "frex_flawless_frames": [ "yourorg.yourmod.yourpackage.MyConsumer" ]
 * }</pre>
 *
 * <p>Renderer implementations should ensure every mod that declares this end point receives exactly
 * one call to provided consumers. (For renderers using FREX, the provided implementation satisfies
 * this guarantee.)
 *
 * <p>When an end point is invoked, the mod consumer will receive a {@code Function<String, Consumer<Boolean>>}
 * instance.  The string name passed in to this function is meant to facilitate logging and debugging
 * of activation within the renderer when multiple mods may be using the end point. The resulting
 * consumer can then be used at any time to activate or deactivate this feature.
 *
 * <p>The contract for usage of the activation consumer is as follows:
 * <ul><li>Calls are thread-safe but not reentrant. (Only one thread should call a given
 * consumer, but it can be any thread.)
 * <li>Calls are idempotent - setting true or false multiple times in succession has the same
 * outcome as calling once.
 * <li>The renderer will check the current status at the start of a frame and that status
 * will be effective for the whole frame.
 * <li>Because of the above conditions, consumers needing precise control of frames should
 * change status on the render thread before the start of the next frame.</ul>
 *
 * <p>The feature will be active when one or more consumers have requested it and inactive
 * when no consumers have activated it.  FREX renderers can use {@link #isActive()} to query
 * the currently effective state.
 */
public interface FlawlessFrames {
	/**
	 * Queries the effective status of this feature.
	 * @return True if any mod has requested activation, false otherwise.
	 */
	static boolean isActive() {
		return FlawlessFramesImpl.isActive();
	}

	/**
	 * Enables or disables logging of feature activation as a diagnostic aid.
	 */
	static void enableTrace(boolean enable) {
		FlawlessFramesImpl.enableTrace(enable);
	}
}
