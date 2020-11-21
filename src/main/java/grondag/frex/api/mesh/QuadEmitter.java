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

package grondag.frex.api.mesh;

import org.jetbrains.annotations.ApiStatus.ScheduledForRemoval;

import net.minecraft.client.texture.Sprite;
import net.minecraft.client.util.math.Vector3f;
import net.minecraft.util.math.Direction;

import net.fabricmc.fabric.api.renderer.v1.material.RenderMaterial;

public interface QuadEmitter extends net.fabricmc.fabric.api.renderer.v1.mesh.QuadEmitter, MutableQuadView {
	@Override
	QuadEmitter material(RenderMaterial material);

	@Override
	QuadEmitter cullFace(Direction face);

	@Override
	QuadEmitter nominalFace(Direction face);

	@Override
	QuadEmitter colorIndex(int colorIndex);

	@Override
	QuadEmitter fromVanilla(int[] quadData, int startIndex, boolean isItem);

	@Override
	QuadEmitter tag(int tag);

	@Override
	QuadEmitter pos(int vertexIndex, float x, float y, float z);

	@Override
	default QuadEmitter pos(int vertexIndex, Vector3f vec) {
		MutableQuadView.super.pos(vertexIndex, vec);
		return this;
	}

	@Override
	default QuadEmitter normal(int vertexIndex, Vector3f vec) {
		MutableQuadView.super.normal(vertexIndex, vec);
		return this;
	}

	@Override
	QuadEmitter lightmap(int vertexIndex, int lightmap);

	@Override
	default QuadEmitter lightmap(int b0, int b1, int b2, int b3) {
		MutableQuadView.super.lightmap(b0, b1, b2, b3);
		return this;
	}

	@Override
	@Deprecated
	@ScheduledForRemoval
	default QuadEmitter spriteColor(int vertexIndex, int spriteIndex, int color) {
		vertexColor(vertexIndex, color);
		return this;
	}

	@Override
	@Deprecated
	@ScheduledForRemoval
	default QuadEmitter spriteColor(int spriteIndex, int c0, int c1, int c2, int c3) {
		quadColor(c0, c1, c2, c3);
		return this;
	}

	@Override
	@Deprecated
	@ScheduledForRemoval
	default QuadEmitter sprite(int vertexIndex, int spriteIndex, float u, float v) {
		sprite(vertexIndex, u, v);
		return this;
	}

	@Override
	@Deprecated
	@ScheduledForRemoval
	default QuadEmitter spriteUnitSquare(int spriteIndex) {
		sprite(0, 0, 0);
		sprite(1, 0, 1);
		sprite(2, 1, 1);
		sprite(3, 1, 0);
		return this;
	}

	@Override
	@Deprecated
	@ScheduledForRemoval
	default QuadEmitter spriteBake(int spriteIndex, Sprite sprite, int bakeFlags) {
		spriteBake(sprite, bakeFlags);
		return this;
	}

	@Override
	default QuadEmitter square(Direction nominalFace, float left, float bottom, float right, float top, float depth) {
		net.fabricmc.fabric.api.renderer.v1.mesh.QuadEmitter.super.square(nominalFace, left, bottom, right, top, depth);
		return this;
	}

	@Override
	QuadEmitter emit();
}
