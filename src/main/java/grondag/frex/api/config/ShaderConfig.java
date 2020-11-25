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

import java.util.function.Supplier;

import net.minecraft.client.MinecraftClient;
import net.minecraft.util.Identifier;

import grondag.frex.impl.config.ShaderConfigImpl;

/**
 * Use this to inject constant declarations that are managed by the mod into shaders .
 *
 * <p>The declarations are referenced by including a provided token in the shader(s) that need them.
 * They are provided by the mod as a string and consumed when needed by the renderer.
 *
 * <p>For example if you have a config option named {@code BOOP} with three choices: {@code BOOP_NEVER},
 * {@code BOOP_SOMETIMES} and {@code BOOP_ALWAYS} then you'd generate something like this:
 *
 * <p><pre>
 * #define BOOP_NEVER 0
 * #define BOOP_SOMETIMES 1
 * #define BOOP_ALWAYS 2
 *
 * // currently selected config
 * #define BOOP BOOP_ALWAYS
 * </pre>
 *
 * <p>Shader authors can reference this like so:
 *
 * <pre>
 * #include mymod:boop_config
 *
 * #if BOOP == BOOP_ALWAYS
 * // code that always boops goes here
 * #endif
 * </pre>
 *
 * <p>This is only suitable for configuration that can be represented as boolean or numeric values
 * via preprocessor declarations and that remain static until changed by the player.
 *
 * <p>The renderer implementation is responsible for detecting multiple/nested {code #include}
 * statements for the same token and for ignoring all duplicate occurrences.
 */
public interface ShaderConfig {
	/**
	 * Registers a supplier of configuration declarations that will replace the given
	 * token whenever it is encountered in shader source.
	 *
	 * <p>Will warn if the same token is registered twice and the last registration will be used.
	 */
	static void registerShaderConfigSupplier(Identifier token, Supplier<String> supplier) {
		ShaderConfigImpl.registerShaderConfigSupplier(token, supplier);
	}

	/**
	 * Used by the renderer implementation - retrieves supplier of configuration declarations
	 * to replace the given token when it is encountered in shader source.
	 *
	 * <p>If the token is not registered, will return a default supplier that outputs
	 * a GLSL-friendly comment explaining it was not found.
	 */
	static Supplier<String> getShaderConfigSupplier(Identifier token) {
		return ShaderConfigImpl.getShaderConfigSupplier(token);
	}

	/**
	 * Mods should invoke this after changing configuration that affects supplier output
	 * to force the renderer to recompile shaders.
	 */
	@SuppressWarnings("resource")
	static void invalidateShaderConfig() {
		MinecraftClient.getInstance().worldRenderer.reload();
	}
}
