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
import org.jetbrains.annotations.ApiStatus.Internal;

import static grondag.frex.api.material.MaterialFinder.DECAL_NONE;
import static grondag.frex.api.material.MaterialFinder.DECAL_POLYGON_OFFSET;
import static grondag.frex.api.material.MaterialFinder.DECAL_VIEW_OFFSET;
import static grondag.frex.api.material.MaterialFinder.DEPTH_TEST_ALWAYS;
import static grondag.frex.api.material.MaterialFinder.DEPTH_TEST_DISABLE;
import static grondag.frex.api.material.MaterialFinder.DEPTH_TEST_EQUAL;
import static grondag.frex.api.material.MaterialFinder.DEPTH_TEST_LEQUAL;
import static grondag.frex.api.material.MaterialFinder.FOG_BLACK;
import static grondag.frex.api.material.MaterialFinder.FOG_NONE;
import static grondag.frex.api.material.MaterialFinder.FOG_TINTED;
import static grondag.frex.api.material.MaterialFinder.TARGET_CLOUDS;
import static grondag.frex.api.material.MaterialFinder.TARGET_ENTITIES;
import static grondag.frex.api.material.MaterialFinder.TARGET_MAIN;
import static grondag.frex.api.material.MaterialFinder.TARGET_OUTLINE;
import static grondag.frex.api.material.MaterialFinder.TARGET_PARTICLES;
import static grondag.frex.api.material.MaterialFinder.TARGET_TRANSLUCENT;
import static grondag.frex.api.material.MaterialFinder.TARGET_WEATHER;
import static grondag.frex.api.material.MaterialFinder.TRANSPARENCY_ADDITIVE;
import static grondag.frex.api.material.MaterialFinder.TRANSPARENCY_CRUMBLING;
import static grondag.frex.api.material.MaterialFinder.TRANSPARENCY_DEFAULT;
import static grondag.frex.api.material.MaterialFinder.TRANSPARENCY_GLINT;
import static grondag.frex.api.material.MaterialFinder.TRANSPARENCY_LIGHTNING;
import static grondag.frex.api.material.MaterialFinder.TRANSPARENCY_NONE;
import static grondag.frex.api.material.MaterialFinder.TRANSPARENCY_TRANSLUCENT;
import static grondag.frex.api.material.MaterialFinder.WRITE_MASK_COLOR;
import static grondag.frex.api.material.MaterialFinder.WRITE_MASK_COLOR_DEPTH;
import static grondag.frex.api.material.MaterialFinder.WRITE_MASK_DEPTH;

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
		if(finder instanceof grondag.frex.api.material.MaterialFinder) {
			readLayerFrex(layer, (grondag.frex.api.material.MaterialFinder) finder);
			return;
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

	private static void readLayerFrex(JsonObject layer, grondag.frex.api.material.MaterialFinder finder) {
		if(layer.has("fragmentSource") && layer.has("vertexSource")) {
			finder.shader(new Identifier(JsonHelper.getString(layer, "vertexSource")), new Identifier(JsonHelper.getString(layer, "fragmentSource")));
		}

		// We test for tags even though getBoolean also does so because we don't necessarily know the correct default value
		if(layer.has("disableAo")) {
			finder.disableAo(JsonHelper.getBoolean(layer, "disableAo", false));
		}

		if(layer.has("disableColorIndex")) {
			finder.disableColorIndex(JsonHelper.getBoolean(layer, "disableColorIndex", false));
		}

		if(layer.has("disableDiffuse")) {
			finder.disableDiffuse(JsonHelper.getBoolean(layer, "disableDiffuse", false));
		}

		if(layer.has("emissive")) {
			finder.emissive(JsonHelper.getBoolean(layer, "emissive", false));
		}

		if(layer.has("blendMode")) {
			finder.blendMode(readBlendModeFrex(JsonHelper.getString(layer, "blendMode")));
		}

		if(layer.has("blur")) {
			finder.blur(JsonHelper.getBoolean(layer, "blur", false));
		}

		if(layer.has("cull")) {
			finder.cull(JsonHelper.getBoolean(layer, "cull", false));
		}

		if(layer.has("cutout")) {
			finder.cutout(JsonHelper.getBoolean(layer, "cutout", false));
		}

		if(layer.has("decal")) {
			final String decal = layer.get("decal").getAsString().toLowerCase();

			if (decal.equals("polygon_offset")) {
				finder.decal(DECAL_POLYGON_OFFSET);
			} else if (decal.equals("view_offset")) {
				finder.decal(DECAL_VIEW_OFFSET);
			} else if (decal.equals("none")) {
				finder.decal(DECAL_NONE);
			}
		}

		if(layer.has("depthTest")) {
			final String depthTest = layer.get("depthTest").getAsString().toLowerCase();

			if (depthTest.equals("always")) {
				finder.depthTest(DEPTH_TEST_ALWAYS);
			} else if (depthTest.equals("equal")) {
				finder.depthTest(DEPTH_TEST_EQUAL);
			} else if (depthTest.equals("lequal")) {
				finder.depthTest(DEPTH_TEST_LEQUAL);
			} else if (depthTest.equals("disable")) {
				finder.depthTest(DEPTH_TEST_DISABLE);
			}
		}

		if(layer.has("discardsTexture")) {
			finder.discardsTexture(JsonHelper.getBoolean(layer, "discardsTexture", false));
		}

		if(layer.has("enableLightmap")) {
			finder.enableLightmap(JsonHelper.getBoolean(layer, "enableLightmap", false));
		}

		if(layer.has("flashOverlay")) {
			finder.flashOverlay(JsonHelper.getBoolean(layer, "flashOverlay", false));
		}

		if(layer.has("fog")) {
			final String fog = layer.get("fog").getAsString().toLowerCase();

			if (fog.equals("none")) {
				finder.fog(FOG_NONE);
			} else if (fog.equals("tinted")) {
				finder.fog(FOG_TINTED);
			} else if (fog.equals("black")) {
				finder.fog(FOG_BLACK);
			}
		}

		if(layer.has("hurtOverlay")) {
			finder.hurtOverlay(JsonHelper.getBoolean(layer, "hurtOverlay", false));
		}

		if(layer.has("lines")) {
			finder.lines(JsonHelper.getBoolean(layer, "lines", false));
		}

		if(layer.has("sorted")) {
			finder.sorted(JsonHelper.getBoolean(layer, "sorted", false));
		}

		if(layer.has("target")) {
			final String target = layer.get("target").getAsString().toLowerCase();

			if (target.equals("main")) {
				finder.target(TARGET_MAIN);
			} else if (target.equals("outline")) {
				finder.target(TARGET_OUTLINE);
			} else if (target.equals("translucent")) {
				finder.target(TARGET_TRANSLUCENT);
			} else if (target.equals("particles")) {
				finder.target(TARGET_PARTICLES);
			} else if (target.equals("weather")) {
				finder.target(TARGET_WEATHER);
			} else if (target.equals("clouds")) {
				finder.target(TARGET_CLOUDS);
			} else if (target.equals("entities")) {
				finder.target(TARGET_ENTITIES);
			}
		}

		if(layer.has("transparentCutout")) {
			finder.transparentCutout(JsonHelper.getBoolean(layer, "transparentCutout", false));
		}

		if(layer.has("transparency")) {
			final String transparency = layer.get("transparency").getAsString().toLowerCase();

			if (transparency.equals("none")) {
				finder.transparency(TRANSPARENCY_NONE);
			} else if (transparency.equals("additive")) {
				finder.transparency(TRANSPARENCY_ADDITIVE);
			} else if (transparency.equals("lightning")) {
				finder.transparency(TRANSPARENCY_LIGHTNING);
			} else if (transparency.equals("glint")) {
				finder.transparency(TRANSPARENCY_GLINT);
			} else if (transparency.equals("crumbling")) {
				finder.transparency(TRANSPARENCY_CRUMBLING);
			} else if (transparency.equals("translucent")) {
				finder.transparency(TRANSPARENCY_TRANSLUCENT);
			} else if (transparency.equals("default")) {
				finder.transparency(TRANSPARENCY_DEFAULT);
			}
		}

		if(layer.has("unmipped")) {
			finder.unmipped(JsonHelper.getBoolean(layer, "unmipped", false));
		}

		if(layer.has("writeMask")) {
			final String writeMask = layer.get("writeMask").getAsString().toLowerCase();

			if (writeMask.equals("color")) {
				finder.writeMask(WRITE_MASK_COLOR);
			} else if (writeMask.equals("depth")) {
				finder.writeMask(WRITE_MASK_DEPTH);
			} else if (writeMask.equals("color_depth")) {
				finder.writeMask(WRITE_MASK_COLOR_DEPTH);
			}
		}
	}

	private static BlendMode readBlendMode(String val) {
		val = val.toLowerCase(Locale.ROOT);

		switch(val) {
			case "solid":
				return BlendMode.SOLID;
			case "cutout":
				return BlendMode.CUTOUT;
			case "cutout_mipped":
				return BlendMode.CUTOUT_MIPPED;
			case "translucent":
				return BlendMode.TRANSLUCENT;
			case "default":
			default:
				return BlendMode.DEFAULT;
		}
	}

	private static BlendMode readBlendModeFrex(String val) {
		val = val.toLowerCase(Locale.ROOT);
		switch(val) {
			case "solid":
				return BlendMode.SOLID;
			case "cutout":
				return BlendMode.CUTOUT;
			case "cutout_mipped":
				return BlendMode.CUTOUT_MIPPED;
			case "translucent":
				return BlendMode.TRANSLUCENT;
			case "default":
				return BlendMode.DEFAULT;
			default:
				return null;
		}
	}

}
