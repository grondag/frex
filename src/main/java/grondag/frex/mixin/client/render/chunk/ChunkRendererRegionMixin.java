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

package grondag.frex.mixin.client.render.chunk;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.client.render.chunk.ChunkRendererRegion;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.chunk.WorldChunk;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

import grondag.frex.api.event.BakedChunkSectionRenderEvent;
import grondag.frex.impl.event.ChunkRenderConditionContextImpl;

@Environment(EnvType.CLIENT)
@Mixin(ChunkRendererRegion.class)
public class ChunkRendererRegionMixin {
	@Inject(method = "method_30000", at = @At("HEAD"), cancellable = true)
	private static void isChunkEmpty(BlockPos startPos, BlockPos endPos, int i, int j, WorldChunk[][] worldChunks, CallbackInfoReturnable<Boolean> cir) {
		if (BakedChunkSectionRenderEvent.shouldAnyFire(new ChunkRenderConditionContextImpl(startPos, endPos))) {
			cir.setReturnValue(false);
		}
	}
}
