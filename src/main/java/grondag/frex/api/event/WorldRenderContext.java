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

package grondag.frex.api.event;

import org.jetbrains.annotations.Nullable;

import net.minecraft.client.render.Camera;
import net.minecraft.client.render.Frustum;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.WorldRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.Matrix4f;

public interface WorldRenderContext {
	WorldRenderer worldRenderer();

	MatrixStack matrixStack();

	float tickDelta();

	long limitTime();

	boolean blockOutlines();

	Camera camera();

	Matrix4f projectionMatrix();

	@Nullable Frustum frustum();

	/**
	 * The {@code VertexConsumerProvider} instance being used by the world renderer for most non-terrain renders.
	 * Generally this will be better for any direct renders because quads for the same layer can be cached
	 * and then drawn all at once by the world renderer.
	 *
	 * <p>IMPORTANT - all vertex coordinates sent to consumers should be relative to the camera to
	 * be consistent with other quads emitted by the world renderer and other mods.  If this isn't
	 * possible, caller should use a separate "immediate" instance.
	 *
	 * <p>If the renderer implementation supports {@code FrexVertexConsumerProvider} this instance can be
	 * cast to that type, and caller can use the extended features available in FREX materials.
	 */
	VertexConsumerProvider consumers();
}
