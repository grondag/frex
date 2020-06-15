/*******************************************************************************
 * Copyright 2020 grondag
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
 ******************************************************************************/

package grondag.frex.impl.material;

import java.io.FileNotFoundException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.IdentityHashMap;
import java.util.Iterator;
import java.util.List;

import com.google.common.collect.ImmutableList;
import org.apiguardian.api.API;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.resource.Resource;
import net.minecraft.resource.ResourceManager;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

import net.fabricmc.fabric.api.renderer.v1.material.RenderMaterial;
import net.fabricmc.fabric.api.resource.ResourceReloadListenerKeys;
import net.fabricmc.fabric.api.resource.SimpleSynchronousResourceReloadListener;

import grondag.frex.Frex;
import grondag.frex.api.Renderer;
import grondag.frex.api.material.MaterialMap;



@API(status = API.Status.INTERNAL)
public class MaterialMapImpl implements SimpleSynchronousResourceReloadListener {
	private MaterialMapImpl() { }

	public MaterialMap get(BlockState state) {
		assert defaultMaterialMap != null;
		return MAP.getOrDefault(state, defaultMaterialMap);
	}

	@Override
	public Identifier getFabricId() {
		return id;
	}

	@Override
	public Collection<Identifier> getFabricDependencies() {
		return DEPS;
	}

	@Override
	public void apply(ResourceManager manager) {
		if (defaultMaterial == null) {
			defaultMaterial = Renderer.get().materialById(RenderMaterial.MATERIAL_STANDARD);
			defaultMaterialMap = new SingleMaterialMap(defaultMaterial);
		}

		MAP.clear();
		final Iterator<Block> blocks = Registry.BLOCK.iterator();

		while(blocks.hasNext()) {
			loadBlock(manager, blocks.next());
		}
	}

	private void loadBlock(ResourceManager manager, Block block) {
		final Identifier blockId = Registry.BLOCK.getId(block);

		final Identifier id = new Identifier(blockId.getNamespace(), "materialmaps/block/" + blockId.getPath() + ".json");

		try(Resource res = manager.getResource(id)) {
			MaterialMapDeserializer.deserialize(block, id, new InputStreamReader(res.getInputStream(), StandardCharsets.UTF_8), MAP);
		} catch (final FileNotFoundException e) {
			// eat these, material maps are not required
		} catch (final Exception e) {
			Frex.LOG.info("Unable to load block material map " + id.toString() + " due to exception " + e.toString());
		}
	}

	private static RenderMaterial defaultMaterial;
	private static MaterialMap defaultMaterialMap;


	public static RenderMaterial defaultMaterial() {
		assert defaultMaterial != null;
		return defaultMaterial;
	}

	public static MaterialMap defaultMaterialMap() {
		assert defaultMaterialMap != null;
		return defaultMaterialMap;
	}

	private static final IdentityHashMap<BlockState, MaterialMap> MAP = new IdentityHashMap<>();
	private static List<Identifier> DEPS = ImmutableList.of(ResourceReloadListenerKeys.MODELS, ResourceReloadListenerKeys.TEXTURES);
	private static final Identifier id =new Identifier("frex:material_map");

	public static final MaterialMapImpl INSTANCE = new MaterialMapImpl();
}
