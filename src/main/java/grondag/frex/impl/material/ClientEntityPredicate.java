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

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import org.jetbrains.annotations.Nullable;

import net.minecraft.entity.Entity;
import net.minecraft.entity.passive.CatEntity;
import net.minecraft.predicate.NbtPredicate;
import net.minecraft.predicate.PlayerPredicate;
import net.minecraft.predicate.entity.EntityEffectPredicate;
import net.minecraft.predicate.entity.EntityEquipmentPredicate;
import net.minecraft.predicate.entity.EntityFlagsPredicate;
import net.minecraft.predicate.entity.FishingHookPredicate;
import net.minecraft.scoreboard.AbstractTeam;
import net.minecraft.util.Identifier;
import net.minecraft.util.JsonHelper;

/**
 * Stripped-down adaptation of vanilla class used for entity loot predicates.
 */
public class ClientEntityPredicate {
	public static final ClientEntityPredicate ANY;
	private final EntityEffectPredicate effects;
	private final NbtPredicate nbt;
	private final EntityFlagsPredicate flags;
	private final EntityEquipmentPredicate equipment;
	private final PlayerPredicate player;
	private final FishingHookPredicate fishingHook;
	@Nullable private final String team;
	@Nullable private final Identifier catType;

	private ClientEntityPredicate(EntityEffectPredicate effects, NbtPredicate nbt, EntityFlagsPredicate flags, EntityEquipmentPredicate equipment, PlayerPredicate player, FishingHookPredicate fishingHook, @Nullable String team, @Nullable Identifier catType) {
		this.effects = effects;
		this.nbt = nbt;
		this.flags = flags;
		this.equipment = equipment;
		this.player = player;
		this.fishingHook = fishingHook;
		this.team = team;
		this.catType = catType;
	}

	public boolean test(Entity entity) {
		if (this == ANY) {
			return true;
		} else if (entity == null) {
			return false;
		} else {
			if (!effects.test(entity)) {
				return false;
			} else if (!nbt.test(entity)) {
				return false;
			} else if (!flags.test(entity)) {
				return false;
			} else if (!equipment.test(entity)) {
				return false;
			} else if (!player.test(entity)) {
				return false;
			} else if (!fishingHook.test(entity)) {
				return false;
			} else {
				if (team != null) {
					final AbstractTeam abstractTeam = entity.getScoreboardTeam();
					if (abstractTeam == null || !team.equals(abstractTeam.getName())) {
						return false;
					}
				}

				return catType == null || entity instanceof CatEntity && ((CatEntity)entity).getTexture().equals(catType);
			}
		}
	}

	public static ClientEntityPredicate fromJson(@Nullable JsonElement json) {
		if (json != null && !json.isJsonNull()) {
			final JsonObject jsonObject = JsonHelper.asObject(json, "entity");
			final EntityEffectPredicate effects = EntityEffectPredicate.fromJson(jsonObject.get("effects"));
			final NbtPredicate nbt = NbtPredicate.fromJson(jsonObject.get("nbt"));
			final EntityFlagsPredicate flags = EntityFlagsPredicate.fromJson(jsonObject.get("flags"));
			final EntityEquipmentPredicate equipment = EntityEquipmentPredicate.fromJson(jsonObject.get("equipment"));
			final PlayerPredicate player = PlayerPredicate.fromJson(jsonObject.get("player"));
			final FishingHookPredicate fishHook = FishingHookPredicate.fromJson(jsonObject.get("fishing_hook"));
			final String team = JsonHelper.getString(jsonObject, "team", (String)null);
			final Identifier catType = jsonObject.has("catType") ? new Identifier(JsonHelper.getString(jsonObject, "catType")) : null;

			return new ClientEntityPredicate(effects, nbt, flags, equipment, player, fishHook, team, catType);
		} else {
			return ANY;
		}
	}

	static {
		ANY = new ClientEntityPredicate(EntityEffectPredicate.EMPTY, NbtPredicate.ANY, EntityFlagsPredicate.ANY, EntityEquipmentPredicate.ANY, PlayerPredicate.ANY, FishingHookPredicate.ANY, (String)null, (Identifier)null);
	}
}
