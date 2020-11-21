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

import net.minecraft.client.render.Camera;
import net.minecraft.client.render.Frustum;
import net.minecraft.client.render.WorldRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.Matrix4f;

import grondag.frex.api.event.WorldRenderContext;

public class WorldRenderContextImpl implements WorldRenderContext {
	protected WorldRenderer worldRenderer;
	protected MatrixStack matrixStack;
	protected float tickDelta;
	protected long limitTime;
	protected boolean blockOutlines;
	protected Camera camera;
	protected Matrix4f projectionMatrix;
	protected Frustum frustum;

	public void prepare(
		WorldRenderer worldRenderer,
		MatrixStack matrixStack,
		float tickDelta,
		long limitTime,
		boolean blockOutlines,
		Camera camera,
		Matrix4f projectionMatrix
	) {
		this.worldRenderer = worldRenderer;
		this.matrixStack = matrixStack;
		this.tickDelta = tickDelta;
		this.limitTime = limitTime;
		this.blockOutlines = blockOutlines;
		this.camera = camera;
		this.projectionMatrix = projectionMatrix;
	}

	public void setFrustum(Frustum frustum) {
		this.frustum = frustum;
	}

	@Override
	public WorldRenderer worldRenderer() {
		return worldRenderer;
	}

	@Override
	public MatrixStack matrixStack() {
		return matrixStack;
	}

	@Override
	public float tickDelta() {
		return tickDelta;
	}

	@Override
	public long limitTime() {
		return limitTime;
	}

	@Override
	public boolean blockOutlines() {
		return blockOutlines;
	}

	@Override
	public Camera camera() {
		return camera;
	}

	@Override
	public Matrix4f projectionMatrix() {
		return projectionMatrix;
	}

	@Override
	public Frustum frustum() {
		return frustum;
	}
}
