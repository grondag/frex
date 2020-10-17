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

package grondag.frex.api.mesh;

import org.jetbrains.annotations.ApiStatus.ScheduledForRemoval;
import org.jetbrains.annotations.Nullable;

import net.minecraft.client.render.model.BakedQuad;
import net.minecraft.client.texture.Sprite;
import net.minecraft.client.util.math.Vector3f;
import net.minecraft.util.math.Direction;

import net.fabricmc.fabric.api.renderer.v1.material.RenderMaterial;

public interface MutableQuadView extends net.fabricmc.fabric.api.renderer.v1.mesh.MutableQuadView {
	@Override
	MutableQuadView material(RenderMaterial material);

	@Override
	@Nullable
	MutableQuadView cullFace(@Nullable Direction face);

	@Override
	@Nullable
	MutableQuadView nominalFace(Direction face);

	@Override
	MutableQuadView colorIndex(int colorIndex);

	@Override
	@Deprecated
	MutableQuadView fromVanilla(int[] quadData, int startIndex, boolean isItem);

	@Override
	MutableQuadView fromVanilla(BakedQuad quad, RenderMaterial material, Direction cullFace);

	@Override
	MutableQuadView tag(int tag);

	@Override
	MutableQuadView pos(int vertexIndex, float x, float y, float z);

	@Override
	default MutableQuadView pos(int vertexIndex, Vector3f vec) {
		return pos(vertexIndex, vec.getX(), vec.getY(), vec.getZ());
	}

	@Override
	MutableQuadView normal(int vertexIndex, float x, float y, float z);

	@Override
	default MutableQuadView normal(int vertexIndex, Vector3f vec) {
		return normal(vertexIndex, vec.getX(), vec.getY(), vec.getZ());
	}

	@Override
	MutableQuadView lightmap(int vertexIndex, int lightmap);

	@Override
	default MutableQuadView lightmap(int b0, int b1, int b2, int b3) {
		lightmap(0, b0);
		lightmap(1, b1);
		lightmap(2, b2);
		lightmap(3, b3);
		return this;
	}

	@Override
	@Deprecated
	@ScheduledForRemoval
	default MutableQuadView spriteColor(int vertexIndex, int spriteIndex, int color) {
		return vertexColor(vertexIndex, color);
	}

	/**
	 * Set color for given vertex.
	 */
	MutableQuadView vertexColor(int vertexIndex, int color);

	@Override
	@Deprecated
	@ScheduledForRemoval
	default MutableQuadView spriteColor(int spriteIndex, int c0, int c1, int c2, int c3) {
		return quadColor(c0, c1, c2, c3);
	}

	/**
	 * Convenience: set color for all vertices at once.
	 */
	default MutableQuadView quadColor(int c0, int c1, int c2, int c3) {
		vertexColor(0, c0);
		vertexColor(1, c1);
		vertexColor(2, c2);
		vertexColor(3, c3);
		return this;
	}

	@Override
	@Deprecated
	@ScheduledForRemoval
	default MutableQuadView sprite(int vertexIndex, int spriteIndex, float u, float v) {
		return sprite(vertexIndex, u, v);
	}

	/**
	 * Set sprite atlas coordinates.
	 */
	MutableQuadView sprite(int vertexIndex, float u, float v);

	@Override
	@Deprecated
	@ScheduledForRemoval
	default MutableQuadView spriteBake(int spriteIndex, Sprite sprite, int bakeFlags) {
		return spriteBake(sprite, bakeFlags);
	}

	/**
	 * Assigns sprite atlas u,v coordinates to this quad for the given sprite.
	 * Can handle UV locking, rotation, interpolation, etc. Control this behavior
	 * by passing additive combinations of the BAKE_ flags defined in this interface.
	 * Behavior for {@code spriteIndex > 0} is currently undefined.
	 */
	MutableQuadView spriteBake(Sprite sprite, int bakeFlags);
}
