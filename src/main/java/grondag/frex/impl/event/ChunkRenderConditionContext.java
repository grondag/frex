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

package grondag.frex.impl.event;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import org.jetbrains.annotations.Nullable;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockRenderView;

import grondag.frex.api.event.RenderRegionBakeListener;
import grondag.frex.api.event.RenderRegionBakeListener.RenderRegionContext;

public class ChunkRenderConditionContext implements RenderRegionContext {
	public final ObjectArrayList<RenderRegionBakeListener> listeners = new ObjectArrayList<>();
	private final BlockPos.Mutable origin = new BlockPos.Mutable();

	public ChunkRenderConditionContext prepare(int x, int y, int z) {
		listeners.clear();
		origin.set(x, y, z);
		return this;
	}

	public @Nullable RenderRegionBakeListener[] getListeners() {
		if (listeners.isEmpty()) {
			return null;
		} else {
			return listeners.toArray(new RenderRegionBakeListener[listeners.size()]);
		}
	}

	@Override
	public BlockRenderView blockView() {
		return null;
	}

	@Override
	public BlockPos origin() {
		return origin;
	}

	public interface RenderRegionListenerProvider {
		@Nullable RenderRegionBakeListener[] frex_getRenderRegionListeners();
	}
}
