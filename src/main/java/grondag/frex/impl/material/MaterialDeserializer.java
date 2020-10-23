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

package grondag.frex.impl.material;

import java.io.Reader;
import java.util.Locale;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import grondag.frex.Frex;
import org.jetbrains.annotations.ApiStatus.Internal;

import net.minecraft.util.Identifier;
import net.minecraft.util.JsonHelper;

import net.fabricmc.fabric.api.renderer.v1.Renderer;
import net.fabricmc.fabric.api.renderer.v1.RendererAccess;
import net.fabricmc.fabric.api.renderer.v1.material.BlendMode;
import net.fabricmc.fabric.api.renderer.v1.material.MaterialFinder;
import net.fabricmc.fabric.api.renderer.v1.material.RenderMaterial;

@Internal
public class MaterialDeserializer {
	private MaterialDeserializer() {}

	private static final Renderer RENDERER = RendererAccess.INSTANCE.getRenderer();
	private static final MaterialFinder FINDER = RENDERER.materialFinder();

	private static final boolean FREX;

	static {
		FREX = Frex.isAvailable();
	}

	public static RenderMaterial deserialize(Reader reader) {
		final MaterialFinder finder = FINDER.clear();
		final JsonObject json = JsonHelper.deserialize(reader);

		// "layers" tag still support for old multi-layer format but
		// only first layer is used. Layer is not requred.
		if(json.has("layers")) {
			final JsonArray layers = JsonHelper.asArray(json.get("layers"), "layers");

			if(!layers.isJsonNull() && layers.size() >= 1) {
				readLayer(layers.get(0).getAsJsonObject(), finder);

				return finder.find();
			}
		}

		readLayer(json, finder);
		return finder.find();
	}

	private static void readLayer(JsonObject layer, MaterialFinder finder) {
		if(layer.has("fragmentSource") && layer.has("vertexSource")) {
			if(FREX) {
				final grondag.frex.api.material.MaterialFinder ff = (grondag.frex.api.material.MaterialFinder) finder;
				ff.fragmentShader(new Identifier(JsonHelper.getString(layer, "fragmentSource")));
				ff.vertexShader(new Identifier(JsonHelper.getString(layer, "vertexSource")));
			}
		}

		if(layer.has("disableAo")) {
			finder.disableAo(0, JsonHelper.getBoolean(layer, "disableAo", true));
		}

		if(layer.has("disableColorIndex")) {
			finder.disableColorIndex(0, JsonHelper.getBoolean(layer, "disableColorIndex", true));
		}

		if(layer.has("disableDiffuse")) {
			finder.disableDiffuse(0, JsonHelper.getBoolean(layer, "disableDiffuse", true));
		}

		if(layer.has("emissive")) {
			finder.emissive(0, JsonHelper.getBoolean(layer, "emissive", true));
		}

		if(layer.has("blendMode")) {
			finder.blendMode(0, readBlendMode(JsonHelper.getString(layer, "blendMode")));
		}
	}

	private static BlendMode readBlendMode(String val) {
		val = val.toLowerCase(Locale.ROOT);
		switch(val) {
			case "solid":
			default:
				return BlendMode.SOLID;
			case "cutout":
				return BlendMode.CUTOUT;
			case "cutout_mipped":
				return BlendMode.CUTOUT_MIPPED;
			case "translucent":
				return BlendMode.TRANSLUCENT;
		}
	}
}
