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

import java.io.FileNotFoundException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.IdentityHashMap;
import java.util.Iterator;
import java.util.List;

import com.google.common.collect.ImmutableList;
import org.jetbrains.annotations.ApiStatus.Internal;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.EntityType;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.FluidState;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.particle.ParticleType;
import net.minecraft.resource.Resource;
import net.minecraft.resource.ResourceManager;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

import net.fabricmc.fabric.api.resource.ResourceReloadListenerKeys;
import net.fabricmc.fabric.api.resource.SimpleSynchronousResourceReloadListener;

import grondag.frex.Frex;
import grondag.frex.api.material.BlockEntityMaterialMap;
import grondag.frex.api.material.EntityMaterialMap;
import grondag.frex.api.material.MaterialMap;

@Internal
public class MaterialMapLoader implements SimpleSynchronousResourceReloadListener {
	private MaterialMapLoader() { }

	public MaterialMap get(BlockState state) {
		return BLOCK_MAP.getOrDefault(state, DEFAULT_MAP);
	}

	public MaterialMap get(FluidState fluidState) {
		return FLUID_MAP.getOrDefault(fluidState, DEFAULT_MAP);
	}

	public MaterialMap get(ItemStack itemStack) {
		return ITEM_MAP.getOrDefault(itemStack.getItem(), DEFAULT_MAP);
	}

	public MaterialMap get(ParticleType<?> particleType) {
		return PARTICLE_MAP.getOrDefault(particleType, DEFAULT_MAP);
	}

	public BlockEntityMaterialMap get(BlockEntityType<?> blockEntityType) {
		return BLOCK_ENTITY_MAP.getOrDefault(blockEntityType, BlockEntityMaterialMap.IDENTITY);
	}

	public EntityMaterialMap get(EntityType<?> entityType) {
		return ENTITY_MAP.getOrDefault(entityType, EntityMaterialMap.IDENTITY);
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
	public void reload(ResourceManager manager) {
		BLOCK_MAP.clear();
		final Iterator<Block> blocks = Registry.BLOCK.iterator();

		while (blocks.hasNext()) {
			loadBlock(manager, blocks.next());
		}

		FLUID_MAP.clear();
		final Iterator<Fluid> fluids = Registry.FLUID.iterator();

		while (fluids.hasNext()) {
			loadFluid(manager, fluids.next());
		}

		// NB: must come after block because uses block maps for block items
		ITEM_MAP.clear();
		final Iterator<Item> items = Registry.ITEM.iterator();

		while (items.hasNext()) {
			loadItem(manager, items.next());
		}

		PARTICLE_MAP.clear();
		final Iterator<ParticleType<?>> particles = Registry.PARTICLE_TYPE.iterator();

		while (particles.hasNext()) {
			loadParticle(manager, particles.next());
		}

		BLOCK_ENTITY_MAP.clear();
		final Iterator<BlockEntityType<?>> blockEntities = Registry.BLOCK_ENTITY_TYPE.iterator();

		while (blockEntities.hasNext()) {
			loadBlockEntity(manager, blockEntities.next());
		}

		ENTITY_MAP.clear();
		final Iterator<EntityType<?>> entities = Registry.ENTITY_TYPE.iterator();

		while (entities.hasNext()) {
			loadEntity(manager, entities.next());
		}
	}

	private void loadBlock(ResourceManager manager, Block block) {
		final Identifier blockId = Registry.BLOCK.getId(block);

		final Identifier id = new Identifier(blockId.getNamespace(), "materialmaps/block/" + blockId.getPath() + ".json");

		try (Resource res = manager.getResource(id)) {
			MaterialMapDeserializer.deserialize(block.getStateManager().getStates(), id, new InputStreamReader(res.getInputStream(), StandardCharsets.UTF_8), BLOCK_MAP);
		} catch (final FileNotFoundException e) {
			// eat these, material maps are not required
		} catch (final Exception e) {
			Frex.LOG.info("Unable to load block material map " + id.toString() + " due to exception " + e.toString());
		}
	}

	private void loadFluid(ResourceManager manager, Fluid fluid) {
		final Identifier blockId = Registry.FLUID.getId(fluid);

		final Identifier id = new Identifier(blockId.getNamespace(), "materialmaps/fluid/" + blockId.getPath() + ".json");

		try (Resource res = manager.getResource(id)) {
			MaterialMapDeserializer.deserialize(fluid.getStateManager().getStates(), id, new InputStreamReader(res.getInputStream(), StandardCharsets.UTF_8), FLUID_MAP);
		} catch (final FileNotFoundException e) {
			// eat these, material maps are not required
		} catch (final Exception e) {
			Frex.LOG.info("Unable to load fluid material map " + id.toString() + " due to exception " + e.toString());
		}
	}

	private void loadItem(ResourceManager manager, Item item) {
		final Identifier itemId = Registry.ITEM.getId(item);

		final Identifier id = new Identifier(itemId.getNamespace(), "materialmaps/item/" + itemId.getPath() + ".json");

		try (Resource res = manager.getResource(id)) {
			ItemMaterialMapDeserializer.deserialize(item, id, new InputStreamReader(res.getInputStream(), StandardCharsets.UTF_8), ITEM_MAP);
		} catch (final FileNotFoundException e) {
			// fall back to block map for block items
			// otherwise eat these, material maps are not required

			if (item instanceof BlockItem) {
				final MaterialMap map = BLOCK_MAP.get(((BlockItem) item).getBlock().getDefaultState());

				if (map != null) {
					ITEM_MAP.put(item, map);
				}
			}
		} catch (final Exception e) {
			Frex.LOG.info("Unable to load block material map " + id.toString() + " due to exception " + e.toString());
		}
	}

	private void loadParticle(ResourceManager manager, ParticleType<?> particleType) {
		final Identifier particleId = Registry.PARTICLE_TYPE.getId(particleType);

		final Identifier id = new Identifier(particleId.getNamespace(), "materialmaps/particle/" + particleId.getPath() + ".json");

		try (Resource res = manager.getResource(id)) {
			ParticleMaterialMapDeserializer.deserialize(particleType, id, new InputStreamReader(res.getInputStream(), StandardCharsets.UTF_8), PARTICLE_MAP);
		} catch (final FileNotFoundException e) {
			// eat these, material maps are not required
		} catch (final Exception e) {
			Frex.LOG.info("Unable to load particle material map " + id.toString() + " due to exception " + e.toString());
		}
	}

	private void loadBlockEntity(ResourceManager manager, BlockEntityType<?> blockEntityType) {
		final Identifier blockEntityId = Registry.BLOCK_ENTITY_TYPE.getId(blockEntityType);
		final Identifier id = new Identifier(blockEntityId.getNamespace(), "materialmaps/block_entity/" + blockEntityId.getPath() + ".json");

		try (Resource res = manager.getResource(id)) {
			BlockEntityMaterialMapDeserializer.deserialize(blockEntityType, id, new InputStreamReader(res.getInputStream(), StandardCharsets.UTF_8), BLOCK_ENTITY_MAP);
		} catch (final FileNotFoundException e) {
			// eat these, material maps are not required
		} catch (final Exception e) {
			Frex.LOG.info("Unable to load block entity material map " + id.toString() + " due to exception " + e.toString());
		}
	}

	private void loadEntity(ResourceManager manager, EntityType<?> entityType) {
		final Identifier entityId = Registry.ENTITY_TYPE.getId(entityType);
		final Identifier id = new Identifier(entityId.getNamespace(), "materialmaps/entity/" + entityId.getPath() + ".json");

		try (Resource res = manager.getResource(id)) {
			EntityMaterialMapDeserializer.deserialize(entityType, id, new InputStreamReader(res.getInputStream(), StandardCharsets.UTF_8), ENTITY_MAP);
		} catch (final FileNotFoundException e) {
			// eat these, material maps are not required
		} catch (final Exception e) {
			Frex.LOG.info("Unable to load block entity material map " + id.toString() + " due to exception " + e.toString());
		}
	}

	public static final MaterialMap DEFAULT_MAP = new SingleMaterialMap(null);

	private static final IdentityHashMap<BlockState, MaterialMap> BLOCK_MAP = new IdentityHashMap<>();
	private static final IdentityHashMap<FluidState, MaterialMap> FLUID_MAP = new IdentityHashMap<>();
	private static final IdentityHashMap<Item, MaterialMap> ITEM_MAP = new IdentityHashMap<>();
	private static final IdentityHashMap<ParticleType<?>, MaterialMap> PARTICLE_MAP = new IdentityHashMap<>();
	private static final IdentityHashMap<BlockEntityType<?>, BlockEntityMaterialMap> BLOCK_ENTITY_MAP = new IdentityHashMap<>();
	private static final IdentityHashMap<EntityType<?>, EntityMaterialMap> ENTITY_MAP = new IdentityHashMap<>();

	private static List<Identifier> DEPS = ImmutableList.of(ResourceReloadListenerKeys.MODELS, ResourceReloadListenerKeys.TEXTURES);
	private static final Identifier id = new Identifier("frex:material_map");

	public static final MaterialMapLoader INSTANCE = new MaterialMapLoader();
}
