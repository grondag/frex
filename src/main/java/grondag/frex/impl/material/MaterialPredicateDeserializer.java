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

package grondag.frex.impl.material;

import static grondag.frex.api.material.MaterialFinder.CUTOUT_ALPHA;
import static grondag.frex.api.material.MaterialFinder.CUTOUT_HALF;
import static grondag.frex.api.material.MaterialFinder.CUTOUT_NONE;
import static grondag.frex.api.material.MaterialFinder.CUTOUT_TENTH;
import static grondag.frex.api.material.MaterialFinder.CUTOUT_ZERO;
import static grondag.frex.api.material.MaterialFinder.DECAL_NONE;
import static grondag.frex.api.material.MaterialFinder.DECAL_POLYGON_OFFSET;
import static grondag.frex.api.material.MaterialFinder.DECAL_VIEW_OFFSET;
import static grondag.frex.api.material.MaterialFinder.DEPTH_TEST_ALWAYS;
import static grondag.frex.api.material.MaterialFinder.DEPTH_TEST_DISABLE;
import static grondag.frex.api.material.MaterialFinder.DEPTH_TEST_EQUAL;
import static grondag.frex.api.material.MaterialFinder.DEPTH_TEST_LEQUAL;
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

import java.util.Locale;
import java.util.function.Predicate;

import com.google.gson.JsonObject;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;

import net.minecraft.util.JsonHelper;

import net.fabricmc.fabric.api.renderer.v1.material.BlendMode;

import grondag.frex.api.material.RenderMaterial;

public class MaterialPredicateDeserializer {
	public static Predicate<RenderMaterial> ALWAYS_TRUE = m -> true;

	@SuppressWarnings("unchecked")
	public static Predicate<RenderMaterial> deserialize(JsonObject json) {
		if (json == null || json.isJsonNull()) {
			return ALWAYS_TRUE;
		}

		final ObjectArrayList<Predicate<RenderMaterial>> predicates = new ObjectArrayList<>();

		readPredicates(json, predicates);

		if (predicates.isEmpty()) {
			return ALWAYS_TRUE;
		} else if (predicates.size() == 1) {
			return predicates.get(0);
		} else {
			return new ArrayPredicate(predicates.toArray(new Predicate[predicates.size()]));
		}
	}

	private static void readPredicates(JsonObject json, ObjectArrayList<Predicate<RenderMaterial>> predicates) {
		if (json.has("texture")) {
			final String vs = JsonHelper.getString(json, "texture");
			predicates.add(mat -> vs.equals(mat.texture()));
		}

		if (json.has("vertexSource")) {
			final String vs = JsonHelper.getString(json, "vertexSource");
			predicates.add(mat -> vs.equals(mat.vertexShader()));
		}

		if (json.has("fragmentSource")) {
			final String fs = JsonHelper.getString(json, "fragmentSource");
			predicates.add(mat -> fs.equals(mat.fragmentShader()));
		}

		if (json.has("renderLayerName")) {
			final String layer = JsonHelper.getString(json, "renderLayerName");
			predicates.add(mat -> layer.equals(mat.renderLayerName()));
		}

		// We test for tags even though getBoolean also does so because we don't necessarily know the correct default value
		if (json.has("disableAo")) {
			final boolean val = JsonHelper.getBoolean(json, "disableAo", true);
			predicates.add(mat -> mat.disableAo() == val);
		}

		if (json.has("disableColorIndex")) {
			final boolean val = JsonHelper.getBoolean(json, "disableColorIndex", true);
			predicates.add(mat -> mat.disableColorIndex() == val);
		}

		if (json.has("disableDiffuse")) {
			final boolean val = JsonHelper.getBoolean(json, "disableDiffuse", true);
			predicates.add(mat -> mat.disableDiffuse() == val);
		}

		if (json.has("emissive")) {
			final boolean val = JsonHelper.getBoolean(json, "emissive", true);
			predicates.add(mat -> mat.emissive() == val);
		}

		if (json.has("blendMode")) {
			final BlendMode val = MaterialDeserializer.readBlendModeFrex(JsonHelper.getString(json, "blendMode"));
			predicates.add(mat -> mat.blendMode() == val);
		}

		if (json.has("blur")) {
			final boolean val = JsonHelper.getBoolean(json, "blur", true);
			predicates.add(mat -> mat.blur() == val);
		}

		if (json.has("cull")) {
			final boolean val = JsonHelper.getBoolean(json, "cull", true);
			predicates.add(mat -> mat.cull() == val);
		}

		if (json.has("cutout")) {
			final String cutout = json.get("cutout").getAsString().toLowerCase(Locale.ROOT);
			int val;

			if (cutout.equals("cutout_half")) {
				val = CUTOUT_HALF;
			} else if (cutout.equals("cutout_tenth")) {
				val = CUTOUT_TENTH;
			} else if (cutout.equals("cutout_zero")) {
				val = CUTOUT_ZERO;
			} else if (cutout.equals("cutout_alpha")) {
				val = CUTOUT_ALPHA;
			} else {
				val = CUTOUT_NONE;
			}

			predicates.add(mat -> mat.cutout() == val);
		}

		if (json.has("decal")) {
			final String decal = json.get("decal").getAsString().toLowerCase(Locale.ROOT);

			if (decal.equals("polygon_offset")) {
				predicates.add(mat -> mat.decal() == DECAL_POLYGON_OFFSET);
			} else if (decal.equals("view_offset")) {
				predicates.add(mat -> mat.decal() == DECAL_VIEW_OFFSET);
			} else if (decal.equals("none")) {
				predicates.add(mat -> mat.decal() == DECAL_NONE);
			}
		}

		if (json.has("depthTest")) {
			final String depthTest = json.get("depthTest").getAsString().toLowerCase(Locale.ROOT);

			if (depthTest.equals("always")) {
				predicates.add(mat -> mat.depthTest() == DEPTH_TEST_ALWAYS);
			} else if (depthTest.equals("equal")) {
				predicates.add(mat -> mat.depthTest() == DEPTH_TEST_EQUAL);
			} else if (depthTest.equals("lequal")) {
				predicates.add(mat -> mat.depthTest() == DEPTH_TEST_LEQUAL);
			} else if (depthTest.equals("disable")) {
				predicates.add(mat -> mat.depthTest() == DEPTH_TEST_DISABLE);
			}
		}

		if (json.has("discardsTexture")) {
			final boolean val = JsonHelper.getBoolean(json, "discardsTexture", true);
			predicates.add(mat -> mat.discardsTexture() == val);
		}

		if (json.has("enableLightmap")) {
			final boolean val = JsonHelper.getBoolean(json, "enableLightmap", true);
			predicates.add(mat -> mat.enableLightmap() == val);
		}

		if (json.has("flashOverlay")) {
			final boolean val = JsonHelper.getBoolean(json, "flashOverlay", true);
			predicates.add(mat -> mat.flashOverlay() == val);
		}

		if (json.has("fog")) {
			final boolean val = JsonHelper.getBoolean(json, "fog", true);
			predicates.add(mat -> mat.fog() == val);
		}

		if (json.has("hurtOverlay")) {
			final boolean val = JsonHelper.getBoolean(json, "hurtOverlay", true);
			predicates.add(mat -> mat.hurtOverlay() == val);
		}

		if (json.has("lines")) {
			final boolean val = JsonHelper.getBoolean(json, "lines", true);
			predicates.add(mat -> mat.lines() == val);
		}

		if (json.has("sorted")) {
			final boolean val = JsonHelper.getBoolean(json, "sorted", true);
			predicates.add(mat -> mat.sorted() == val);
		}

		if (json.has("target")) {
			final String target = json.get("target").getAsString().toLowerCase(Locale.ROOT);

			if (target.equals("main")) {
				predicates.add(mat -> mat.target() == TARGET_MAIN);
			} else if (target.equals("outline")) {
				predicates.add(mat -> mat.target() == TARGET_OUTLINE);
			} else if (target.equals("translucent")) {
				predicates.add(mat -> mat.target() == TARGET_TRANSLUCENT);
			} else if (target.equals("particles")) {
				predicates.add(mat -> mat.target() == TARGET_PARTICLES);
			} else if (target.equals("weather")) {
				predicates.add(mat -> mat.target() == TARGET_WEATHER);
			} else if (target.equals("clouds")) {
				predicates.add(mat -> mat.target() == TARGET_CLOUDS);
			} else if (target.equals("entities")) {
				predicates.add(mat -> mat.target() == TARGET_ENTITIES);
			}
		}

		if (json.has("transparency")) {
			final String transparency = json.get("transparency").getAsString().toLowerCase(Locale.ROOT);

			if (transparency.equals("none")) {
				predicates.add(mat -> mat.transparency() == TRANSPARENCY_NONE);
			} else if (transparency.equals("additive")) {
				predicates.add(mat -> mat.transparency() == TRANSPARENCY_ADDITIVE);
			} else if (transparency.equals("lightning")) {
				predicates.add(mat -> mat.transparency() == TRANSPARENCY_LIGHTNING);
			} else if (transparency.equals("glint")) {
				predicates.add(mat -> mat.transparency() == TRANSPARENCY_GLINT);
			} else if (transparency.equals("crumbling")) {
				predicates.add(mat -> mat.transparency() == TRANSPARENCY_CRUMBLING);
			} else if (transparency.equals("translucent")) {
				predicates.add(mat -> mat.transparency() == TRANSPARENCY_TRANSLUCENT);
			} else if (transparency.equals("default")) {
				predicates.add(mat -> mat.transparency() == TRANSPARENCY_DEFAULT);
			}
		}

		if (json.has("unmipped")) {
			final boolean val = JsonHelper.getBoolean(json, "unmipped", true);
			predicates.add(mat -> mat.unmipped() == val);
		}

		if (json.has("writeMask")) {
			final String writeMask = json.get("writeMask").getAsString().toLowerCase(Locale.ROOT);

			if (writeMask.equals("color")) {
				predicates.add(mat -> mat.writeMask() == WRITE_MASK_COLOR);
			} else if (writeMask.equals("depth")) {
				predicates.add(mat -> mat.writeMask() == WRITE_MASK_DEPTH);
			} else if (writeMask.equals("color_depth")) {
				predicates.add(mat -> mat.writeMask() == WRITE_MASK_COLOR_DEPTH);
			}
		}
	}

	private static class ArrayPredicate implements Predicate<RenderMaterial> {
		private final Predicate<RenderMaterial>[] predicates;

		private ArrayPredicate(Predicate<RenderMaterial>[] predicates) {
			this.predicates = predicates;
		}

		@Override
		public boolean test(RenderMaterial material) {
			final int limit = predicates.length;

			for (int i = 0; i < limit; ++i) {
				if (!predicates[i].test(material)) {
					return false;
				}
			}

			return true;
		}
	}
}
