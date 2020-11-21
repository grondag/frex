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

package grondag.frex;

/**
 * Use to make FREX an optional dependency. To do so, implement this interface
 * in a stand-alone class and declare a "FREX" end point in the mod's
 * fabric.mod.json that points to the implementation.
 *
 * <p>Every mod that implements this interface and declares and end point will receive
 * exactly one call to {@link #onInitalizeFrex()}.
 *
 * <p>To maintain an optional dependency, all calls to FREX methods must be isolated to
 * the FrexInitializer instance or to classes that are only loaded if {@link #onInitalizeFrex()}
 * is called.
 *
 * <p>Note that it is NOT necessary to implement this interface and register a
 * "frex" end point for mods that nest the FREX library or have a hard dependency on FREX.
 * Such mods can safely handle FREX registration in their client initialize instance.
 */

public interface FrexInitializer {
	/**
	 * Signals mods that maintain an optional dependency on FREX that FREX is
	 * loaded. Such mods should handle initialization activities that reference
	 * FREX classes during this call.
	 *
	 * <p>Will be called during client mod initialization, possibly before the requesting
	 * mod initialization is complete. It will be called exactly once per game start.
	 */
	void onInitalizeFrex();
}
