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

package grondag.frex.impl.light;

import java.io.FileNotFoundException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.IdentityHashMap;
import java.util.Iterator;
import java.util.List;

import com.google.common.collect.ImmutableList;
import grondag.frex.Frex;
import grondag.frex.api.light.ItemLight;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.resource.Resource;
import net.minecraft.resource.ResourceManager;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

import net.fabricmc.fabric.api.resource.SimpleSynchronousResourceReloadListener;

public class ItemLightLoader implements SimpleSynchronousResourceReloadListener {
	private ItemLightLoader() { }

	@Override
	public void apply(ResourceManager manager) {
		MAP.clear();
		final Iterator<Item> items = Registry.ITEM.iterator();

		while(items.hasNext()) {
			loadItem(manager, items.next());
		}
	}

	private void loadItem(ResourceManager manager, Item item) {
		final Identifier itemId = Registry.ITEM.getId(item);
		final Identifier id = new Identifier(itemId.getNamespace(), "lights/item/" + itemId.getPath() + ".json");

		try(Resource res = manager.getResource(id)) {
			final ItemLight light = ItemLightDeserializer.deserialize(new InputStreamReader(res.getInputStream(), StandardCharsets.UTF_8));

			if (light != ItemLight.NONE) {
				MAP.put(item, light);
			}
		} catch (final FileNotFoundException e) {
			// eat these, lights are not required
		} catch (final Exception e) {
			Frex.LOG.info("Unable to load item light data for " + id.toString() + " due to exception " + e.toString());
		}
	}

	@Override
	public Identifier getFabricId() {
		return id;
	}

	@Override
	public Collection<Identifier> getFabricDependencies() {
		return DEPS;
	}

	private static List<Identifier> DEPS = ImmutableList.of();
	private static final Identifier id =new Identifier("frex:item_light");
	public static final ItemLightLoader INSTANCE = new ItemLightLoader();
	private static final IdentityHashMap<Item, ItemLight> MAP = new IdentityHashMap<>();

	public static ItemLight get(ItemStack stack) {
		return MAP.getOrDefault(stack.getItem(), ItemLight.NONE);
	}
}
