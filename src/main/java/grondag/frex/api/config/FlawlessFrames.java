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
 * Defines an entry point mods like ReplayMod ("consumers") can use to signal a renderer
 * or other mods that affect rendering ("providers") that quality should be favored over
 * speed because output is being recorded. An active signal means a provider that
 * is altering some visual elements to achieve interactive frame rates should try
 * to disable those measures if it will give a noticeable boost to quality.
 *
 * <p>At a minimum, a renderer should ensure that terrain iteration blocks the main
 * render thread until the entire visible set of chunk sections are determined and all
 * visible chunk sections have been built and can be rendered in the frame.
 *
 * <p>While this API is defined as part of FREX, it uses standard Java interfaces so that
 * it can be implemented and consumed without reference to any FREX library or any other
 * mod dependency. This is done to facilitate adoption by suppliers that do not implement
 * or depend on FREX and to spare mods that do not use other FREX features from including it.
 *
 * <p>This interface and it's implementation are provided as a convenience for providers
 * that depend on FREX directly. Providers that do not depend on FREX can copy the relevant
 * code from FREX or create their own implementation as desired.
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
 * <p>Any provider that invokes this end point should ensure every consumer that declares it
 * is called exactly once during initialization. (For providers using FREX, the library
 * automatically satisfies this guarantee.)
 *
 * <p>When an end point is invoked, the consumer will receive a {@code Function<String, Consumer<Boolean>>}
 * instance. The string name passed in to this function is meant to facilitate logging and debugging
 * of activation within the renderer when multiple mods may be using the end point. The resulting
 * "activation function" instance can then be used at any time to activate or deactivate this feature.
 *
 * <p>Consumers of this endpoint <em>MUST</em> be prepared to be called by multiple providers and
 * <em>MUST</em> retrieve, retain and call all received activation functions uniformly. This is necessary
 * because the dependency on FREX is entirely optional and thus providers implementations may not be
 * shared. This scenario will happen, for example, when a FREX renderer is present and another provider
 * that affects rendering (but does not use the FREX library) is loaded and has provided the endpoint.
 *
 * <p>The contract for usage of activation functions by consumers is as follows:
 * <ul><li>Calls are thread-safe but not reentrant. (Only one thread should call a given
 * activation consumer, but it can be any thread.)
 * <li>Calls are idempotent - setting true or false multiple times in succession has the same
 * outcome as calling once.
 * <li>The provider will check the current status at least once every frame, and most
 * providers will probably check at the start of the frame.  While a provider may
 * check for changes at any time during a frame, it isn't required to change behavior until the next.
 * <li>Because of the above conditions, consumers needing precise control of frames should
 * change status on the render thread before the start of the next frame.
 * <li>As noted earlier, consumers <em>MUST</em> call every activation function provided to them
 * during initialization, or else when there are multiple providers the state of rendering quality will
 * become undefined overall.</ul>
 *
 * <p>The feature will be active when one or more consumers have requested it and inactive
 * when no consumers have activated it. FREX dependents can use {@link #isActive()} to query
 * the currently effective state.
 */
public interface FlawlessFrames {
	/**
	 * Queries the effective status of this feature.
	 * @return True if any consumer has requested activation, false otherwise.
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
