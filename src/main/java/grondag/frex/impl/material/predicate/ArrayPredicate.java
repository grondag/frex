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

package grondag.frex.impl.material.predicate;

import java.util.function.Function;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;

import grondag.frex.api.material.RenderMaterial;

public class ArrayPredicate extends MaterialPredicate {
	private final MaterialPredicate[] compact;
	// strictly for equation
	private final MaterialPredicate[] sparse;

	private static void addOrNull(ObjectArrayList<MaterialPredicate> sparseList, ObjectArrayList<MaterialPredicate> compactList, JsonObject json, String property, Function<JsonElement, MaterialPredicate> factory) {
		if (json.has(property)) {
			final MaterialPredicate created = factory.apply(json.get(property));

			if (created != null) {
				compactList.add(created);
			}

			sparseList.add(created);
		} else {
			sparseList.add(null);
		}
	}

	public ArrayPredicate(JsonObject json) {
		final ObjectArrayList<MaterialPredicate> sparsePredicates = new ObjectArrayList<>();
		final ObjectArrayList<MaterialPredicate> compactPredicates = new ObjectArrayList<>();

		addOrNull(sparsePredicates, compactPredicates, json, "texture", e -> MaterialTester.createString(e, MaterialTester.TEXTURE_TEST));
		addOrNull(sparsePredicates, compactPredicates, json, "vertexSource", e -> MaterialTester.createString(e, MaterialTester.VERTEX_SOURCE_TEST));
		addOrNull(sparsePredicates, compactPredicates, json, "fragmentSource", e -> MaterialTester.createString(e, MaterialTester.FRAGMENT_SOURCE_TEST));
		addOrNull(sparsePredicates, compactPredicates, json, "renderLayerName", e -> MaterialTester.createString(e, MaterialTester.RENDER_LAYER_NAME_TEST));
		addOrNull(sparsePredicates, compactPredicates, json, "disableAo", e -> MaterialTester.createBoolean(e, MaterialTester.DISABLE_AO_TEST));
		addOrNull(sparsePredicates, compactPredicates, json, "disableColorIndex", e -> MaterialTester.createBoolean(e, MaterialTester.DISABLE_COLOR_INDEX_TEST));
		addOrNull(sparsePredicates, compactPredicates, json, "disableDiffuse", e -> MaterialTester.createBoolean(e, MaterialTester.DISABLE_DIFFUSE_TEST));
		addOrNull(sparsePredicates, compactPredicates, json, "emissive", e -> MaterialTester.createBoolean(e, MaterialTester.EMISSIVE_TEST));
		addOrNull(sparsePredicates, compactPredicates, json, "blendMode", MaterialTester::createBlendMode);
		addOrNull(sparsePredicates, compactPredicates, json, "blur", e -> MaterialTester.createBoolean(e, MaterialTester.BLUR_TEST));
		addOrNull(sparsePredicates, compactPredicates, json, "cull", e -> MaterialTester.createBoolean(e, MaterialTester.CULL_TEST));
		addOrNull(sparsePredicates, compactPredicates, json, "cutout", MaterialTester::createCutout);
		addOrNull(sparsePredicates, compactPredicates, json, "decal", MaterialTester::createDecal);
		addOrNull(sparsePredicates, compactPredicates, json, "depthTest", MaterialTester::createDepthTest);
		addOrNull(sparsePredicates, compactPredicates, json, "discardsTexture", e -> MaterialTester.createBoolean(e, MaterialTester.DISCARDS_TEXTURE_TEST));
		addOrNull(sparsePredicates, compactPredicates, json, "enableLightmap", e -> MaterialTester.createBoolean(e, MaterialTester.ENABLE_LIGHTMAP_TEST));
		addOrNull(sparsePredicates, compactPredicates, json, "flashOverlay", e -> MaterialTester.createBoolean(e, MaterialTester.FLASH_OVERLAY_TEST));
		addOrNull(sparsePredicates, compactPredicates, json, "fog", e -> MaterialTester.createBoolean(e, MaterialTester.FOG_TEST));
		addOrNull(sparsePredicates, compactPredicates, json, "hurtOverlay", e -> MaterialTester.createBoolean(e, MaterialTester.HURT_OVERLAY_TEST));
		addOrNull(sparsePredicates, compactPredicates, json, "lines", e -> MaterialTester.createBoolean(e, MaterialTester.LINES_TEST));
		addOrNull(sparsePredicates, compactPredicates, json, "sorted", e -> MaterialTester.createBoolean(e, MaterialTester.SORTED_TEST));
		addOrNull(sparsePredicates, compactPredicates, json, "target", MaterialTester::createTarget);
		addOrNull(sparsePredicates, compactPredicates, json, "transparency", MaterialTester::createTransparency);
		addOrNull(sparsePredicates, compactPredicates, json, "unmipped", e -> MaterialTester.createBoolean(e, MaterialTester.UNMIPPED_TEST));
		addOrNull(sparsePredicates, compactPredicates, json, "writeMask", MaterialTester::createWriteMask);

		sparse = sparsePredicates.toArray(new MaterialPredicate[0]);
		compact = compactPredicates.toArray(new MaterialPredicate[0]);
	}

	public int size() {
		return compact.length;
	}

	public MaterialPredicate get(int i) {
		return compact[i];
	}

	@Override
	public boolean test(RenderMaterial material) {
		final int limit = compact.length;

		for (int i = 0; i < limit; ++i) {
			if (!compact[i].test(material)) {
				return false;
			}
		}

		return true;
	}

	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof ArrayPredicate)) {
			return false;
		}

		final int limit = sparse.length;

		for (int i = 0; i < limit; ++i) {
			final MaterialPredicate own = sparse[i];
			final MaterialPredicate theirs = ((ArrayPredicate) obj).sparse[i];

			if (own != null && theirs != null) {
				if (!own.equals(theirs)) {
					return false;
				}
			} else if (own != theirs) {
				return false;
			}
		}

		return true;
	}
}
