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

package grondag.frex.api.fluid;

import java.util.Random;
import java.util.function.Supplier;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.LeavesBlock;
import net.minecraft.block.TransparentBlock;
import net.minecraft.client.texture.Sprite;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.FluidState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockRenderView;
import net.minecraft.world.BlockView;

import net.fabricmc.fabric.api.client.render.fluid.v1.FluidRenderHandler;
import net.fabricmc.fabric.api.renderer.v1.material.RenderMaterial;
import net.fabricmc.fabric.api.renderer.v1.mesh.QuadEmitter;
import net.fabricmc.fabric.api.renderer.v1.render.RenderContext;

/**
 * Implementation of {@link FluidQuadSupplier}  with vanilla-like geometry.
 */
public abstract class AbstractFluidModel implements FluidQuadSupplier, FluidRenderHandler {
	protected final RenderMaterial material;
	protected final boolean blendColors;

	protected AbstractFluidModel(RenderMaterial material,  boolean blendColors) {
		this.material = material;
		this.blendColors = blendColors;
	}

	@Override
	public void emitBlockQuads(BlockRenderView world, BlockState state, BlockPos centerPos, Supplier<Random> randomSupplier, RenderContext context) {
		final QuadEmitter qe = context.getEmitter();
		final FluidState fluidState = state.getFluidState();
		final BlockState blockState = world.getBlockState(centerPos);
		final Sprite[] sprites = getFluidSprites(world, centerPos, fluidState);
		final BlockPos.Mutable searchPos = SEARCH_POS.get();

		final int centerColor = getFluidColor(world, centerPos, fluidState) | 0xFF000000;

		final int nwColor, swColor, neColor, seColor;

		if (blendColors) {
			final int n = getFluidColor(world, centerPos.offset(Direction.NORTH), fluidState);
			final int w = getFluidColor(world, centerPos.offset(Direction.WEST), fluidState);
			final int s = getFluidColor(world, centerPos.offset(Direction.SOUTH), fluidState);
			final int e = getFluidColor(world, centerPos.offset(Direction.EAST), fluidState);

			final int ne = getFluidColor(world, centerPos.offset(Direction.NORTH).offset(Direction.EAST), fluidState);
			final int nw = getFluidColor(world, centerPos.offset(Direction.NORTH).offset(Direction.WEST), fluidState);
			final int se = getFluidColor(world, centerPos.offset(Direction.SOUTH).offset(Direction.EAST), fluidState);
			final int sw = getFluidColor(world, centerPos.offset(Direction.SOUTH).offset(Direction.WEST), fluidState);

			nwColor = colorMix4(centerColor, n, w, nw) | 0xFF000000;
			swColor = colorMix4(centerColor, s, w, sw) | 0xFF000000;
			neColor = colorMix4(centerColor, n, e, ne) | 0xFF000000;
			seColor = colorMix4(centerColor, s, e, se) | 0xFF000000;
		} else {
			nwColor = centerColor;
			swColor = centerColor;
			neColor = centerColor;
			seColor = centerColor;
		}

		final Fluid fluid = fluidState.getFluid();
		final boolean isUpVisible = !isSameFluid(world, centerPos, Direction.UP, fluid);
		final boolean isDownVisible = notBlockedNotSame(world, centerPos, fluid, blockState, Direction.DOWN)
				&& !isBlockedAtOffset(world, searchPos.set(centerPos, Direction.DOWN), Direction.DOWN, 0.8888889F);
		final boolean isNorthVisible = notBlockedNotSame(world, centerPos, fluid, blockState, Direction.NORTH);
		final boolean isSouthVisible = notBlockedNotSame(world, centerPos, fluid, blockState, Direction.SOUTH);
		final boolean isWestVisible = notBlockedNotSame(world, centerPos, fluid, blockState, Direction.WEST);
		final boolean isEastVisible = notBlockedNotSame(world, centerPos, fluid, blockState, Direction.EAST);

		if (isUpVisible || isDownVisible || isEastVisible || isWestVisible || isNorthVisible || isSouthVisible) {
			final Sprite stillSprite = sprites[0];
			float centerNwHeight = nwHeight(world, centerPos, fluidState.getFluid());
			float southNwHeight = nwHeight(world, centerPos.south(), fluidState.getFluid());
			float southEastNwHeight = nwHeight(world, centerPos.east().south(), fluidState.getFluid());
			float eastNwHeight = nwHeight(world, centerPos.east(), fluidState.getFluid());
			final float downBasedOffset = isDownVisible ? 0.001F : 0.0F;

			if (isUpVisible && !isBlockedAtOffset(world, searchPos.set(centerPos, Direction.UP), Direction.UP, Math.min(Math.min(centerNwHeight, southNwHeight), Math.min(southEastNwHeight, eastNwHeight)))) {
				centerNwHeight -= 0.001F;
				southNwHeight -= 0.001F;
				southEastNwHeight -= 0.001F;
				eastNwHeight -= 0.001F;
				final Vec3d velocity = fluidState.getVelocity(world, centerPos);
				Sprite topSprite;

				float u0, u1, u2, u3, v0, v1, v2, v3;

				if (velocity.x == 0.0D && velocity.z == 0.0D) {
					topSprite = stillSprite;
					u0 = topSprite.getFrameU(0.0D);
					v0 = topSprite.getFrameV(0.0D);
					u1 = u0;
					v1 = topSprite.getFrameV(16.0D);
					u2 = topSprite.getFrameU(16.0D);
					v2 = v1;
					u3 = u2;
					v3 = v0;
				} else {
					topSprite = sprites[1];
					final float angle = (float)MathHelper.atan2(velocity.z, velocity.x) - 1.5707964F;
					final float dx = MathHelper.sin(angle) * 0.25F;
					final float dy = MathHelper.cos(angle) * 0.25F;
					u0 = topSprite.getFrameU(8.0F + (-dy - dx) * 16.0F);
					v0 = topSprite.getFrameV(8.0F + (-dy + dx) * 16.0F);
					u1 = topSprite.getFrameU(8.0F + (-dy + dx) * 16.0F);
					v1 = topSprite.getFrameV(8.0F + (dy + dx) * 16.0F);
					u2 = topSprite.getFrameU(8.0F + (dy + dx) * 16.0F);
					v2 = topSprite.getFrameV(8.0F + (dy - dx) * 16.0F);
					u3 = topSprite.getFrameU(8.0F + (dy - dx) * 16.0F);
					v3 = topSprite.getFrameV(8.0F + (-dy - dx) * 16.0F);
				}

				final float uCentroid = (u0 + u1 + u2 + u3) / 4.0F;
				final float vCentroid = (v0 + v1 + v2 + v3) / 4.0F;

				final float dx = stillSprite.getWidth() / (stillSprite.getMaxU() - stillSprite.getMinU());
				final float dy = stillSprite.getHeight() / (stillSprite.getMaxV() - stillSprite.getMinV());
				final float centerScale = 4.0F / Math.max(dy, dx);

				u0 = MathHelper.lerp(centerScale, u0, uCentroid);
				u1 = MathHelper.lerp(centerScale, u1, uCentroid);
				u2 = MathHelper.lerp(centerScale, u2, uCentroid);
				u3 = MathHelper.lerp(centerScale, u3, uCentroid);
				v0 = MathHelper.lerp(centerScale, v0, vCentroid);
				v1 = MathHelper.lerp(centerScale, v1, vCentroid);
				v2 = MathHelper.lerp(centerScale, v2, vCentroid);
				v3 = MathHelper.lerp(centerScale, v3, vCentroid);

				qe.pos(0, 0, centerNwHeight, 0).sprite(0, 0, u0, v0).spriteColor(0, 0, nwColor)
				.pos(1, 0, southNwHeight, 1).sprite(1, 0, u1, v1).spriteColor(1, 0, swColor)
				.pos(2, 1, southEastNwHeight, 1).sprite(2, 0, u2, v2).spriteColor(2, 0, seColor)
				.pos(3, 1, eastNwHeight, 0).sprite(3, 0, u3, v3).spriteColor(3, 0, neColor)
				.material(material).emit();

				// backface
				if (fluidState.method_15756(world, centerPos.up())) {
					qe.pos(0, 0, centerNwHeight, 0).sprite(0, 0, u0, v0).spriteColor(0, 0, nwColor)
					.pos(1, 1, eastNwHeight, 0).sprite(1, 0, u3, v3).spriteColor(1, 0, neColor)
					.pos(2, 1, southEastNwHeight, 1).sprite(2, 0, u2, v2).spriteColor(2, 0, seColor)
					.pos(3, 0, southNwHeight, 1).sprite(3, 0, u1, v1).spriteColor(3, 0, swColor)
					.material(material).emit();
				}
			}

			if (isDownVisible) {
				float u0, u1, v1, v0;

				u0 = stillSprite.getMinU();
				u1 = stillSprite.getMaxU();
				v1 = stillSprite.getMinV();
				v0 = stillSprite.getMaxV();

				qe.pos(0, 0, downBasedOffset, 1).sprite(0, 0, u0, v0).spriteColor(0, 0, swColor)
				.pos(1, 0, downBasedOffset, 0).sprite(1, 0, u0, v1).spriteColor(1, 0, nwColor)
				.pos(2, 1, downBasedOffset, 0).sprite(2, 0, u1, v1).spriteColor(2, 0, neColor)
				.pos(3, 1, downBasedOffset, 1).sprite(3, 0, u1, v0).spriteColor(3, 0, seColor)
				.material(material).emit();
			}

			for(int sideIndex = 0; sideIndex < 4; ++sideIndex) {
				float y0, y1, x0, x1, z0, z1;
				Direction face;
				int c0, c1;

				if (sideIndex == 0) {
					if (!isNorthVisible) continue;
					y0 = centerNwHeight;
					y1 = eastNwHeight;
					c0 = nwColor;
					c1 = neColor;
					x0 = 0;
					x1 = 1;
					z0 = 0.0010000000474974513F;
					z1 = 0.0010000000474974513F;
					face = Direction.NORTH;
				} else if (sideIndex == 1) {
					if (!isSouthVisible) continue;
					face = Direction.SOUTH;
					y0 = southEastNwHeight;
					y1 = southNwHeight;
					c0 = seColor;
					c1 = swColor;
					x0 = 1F;
					x1 = 0;
					z0 = 1.0F - 0.0010000000474974513F;
					z1 = 1.0F - 0.0010000000474974513F;
				} else if (sideIndex == 2) {
					if (!isWestVisible) continue;
					face = Direction.WEST;
					y0 = southNwHeight;
					y1 = centerNwHeight;
					c0 = swColor;
					c1 = nwColor;
					x0 = 0.0010000000474974513F;
					x1 = 0.0010000000474974513F;
					z0 = 1.0F;
					z1 = 0;
				} else {
					if (!isEastVisible) continue;
					face = Direction.EAST;
					y0 = eastNwHeight;
					y1 = southEastNwHeight;
					c0 = neColor;
					c1 = seColor;
					x0 = 1.0F - 0.0010000000474974513F;
					x1 = 1.0F - 0.0010000000474974513F;
					z0 = 0;
					z1 = 1F;
				}

				if (!isBlockedAtOffset(world, searchPos.set(centerPos, face), face, Math.max(y0, y1))) {
					final boolean overlay;
					final Sprite sideSprite;

					if (needsOverlay()) {
						final Block sideBlock = world.getBlockState(searchPos.set(centerPos, face)).getBlock();
						if (sideBlock instanceof TransparentBlock || sideBlock instanceof LeavesBlock) {
							sideSprite = overlaySprite();
							overlay = true;
						} else {
							sideSprite = sprites[1];
							overlay = false;
						}
					} else {
						sideSprite = sprites[1];
						overlay = false;
					}

					final float u0 = sideSprite.getFrameU(0.0D);
					final float u1 = sideSprite.getFrameU(8.0D);
					final float v0 = sideSprite.getFrameV((1.0F - y0) * 16.0F * 0.5F);
					final float v1 = sideSprite.getFrameV((1.0F - y1) * 16.0F * 0.5F);
					final float vCenter = sideSprite.getFrameV(8.0D);

					qe.pos(0, x0, y0, z0).sprite(0, 0, u0, v0).spriteColor(0, 0, c0)
					.pos(1, x1, y1, z1).sprite(1, 0, u1, v1).spriteColor(1, 0, c1)
					.pos(2, x1, downBasedOffset, z1).sprite(2, 0, u1, vCenter).spriteColor(2, 0, c1)
					.pos(3, x0, downBasedOffset, z0).sprite(3, 0, u0, vCenter).spriteColor(3, 0, c0)
					.spriteColor(0, centerColor, centerColor, centerColor, centerColor)
					.material(material).emit();

					if (!overlay) {
						qe.pos(0, x0, downBasedOffset, z0).sprite(0, 0, u0, vCenter).spriteColor(0, 0, c0)
						.pos(1, x1, downBasedOffset, z1).sprite(1, 0, u1, vCenter).spriteColor(1, 0, c1)
						.pos(2, x1, y1, z1).sprite(2, 0, u1, v1).spriteColor(2, 0, c1)
						.pos(3, x0, y0, z0).sprite(3, 0, u0, v0).spriteColor(3, 0, c0)
						.spriteColor(0, centerColor, centerColor, centerColor, centerColor)
						.material(material).emit();
					}
				}
			}
		}
	}

	protected boolean needsOverlay() {
		return false;
	}

	protected Sprite overlaySprite() {
		return null;
	}

	private static boolean isSameFluid(BlockView world, BlockPos pos, Direction side, Fluid fluid) {
		final BlockPos blockPos = pos.offset(side);
		final FluidState fluidState = world.getFluidState(blockPos);
		return fluidState.getFluid().matchesType(fluid);
	}

	private static boolean isBlockedAtOffset(BlockView blockView, Direction direction, float offset, BlockPos blockPos, BlockState blockState) {
		if (blockState.isOpaque()) {
			final VoxelShape voxelShape = VoxelShapes.cuboid(0.0D, 0.0D, 0.0D, 1.0D, offset, 1.0D);
			final VoxelShape voxelShape2 = blockState.getCullingShape(blockView, blockPos);
			return VoxelShapes.isSideCovered(voxelShape, voxelShape2, direction);
		} else {
			return false;
		}
	}

	private static boolean isBlockedAtOffset(BlockView world, BlockPos offsetPos, Direction direction, float offset) {
		final BlockState blockState = world.getBlockState(offsetPos);
		return isBlockedAtOffset(world, direction, offset, offsetPos, blockState);
	}

	private static boolean isSideBlocked(BlockView blockView, BlockPos blockPos, BlockState blockState, Direction direction) {
		return isBlockedAtOffset(blockView, direction.getOpposite(), 1.0F, blockPos, blockState);
	}

	private static boolean notBlockedNotSame(BlockRenderView blockRenderView, BlockPos blockPos, Fluid fluid, BlockState blockState, Direction direction) {
		return !isSideBlocked(blockRenderView, blockPos, blockState, direction) && !isSameFluid(blockRenderView, blockPos, direction, fluid);
	}

	private static float nwHeight(BlockView world, BlockPos pos, Fluid fluid) {
		int i = 0;
		float f = 0.0F;

		for(int j = 0; j < 4; ++j) {
			final BlockPos blockPos = pos.add(-(j & 1), 0, -(j >> 1 & 1));
			if (world.getFluidState(blockPos.up()).getFluid().matchesType(fluid)) {
				return 1.0F;
			}

			final FluidState fluidState = world.getFluidState(blockPos);
			if (fluidState.getFluid().matchesType(fluid)) {
				final float g = fluidState.getHeight(world, blockPos);
				if (g >= 0.8F) {
					f += g * 10.0F;
					i += 10;
				} else {
					f += g;
					++i;
				}
			} else if (!world.getBlockState(blockPos).getMaterial().isSolid()) {
				++i;
			}
		}

		return f / i;
	}

	public static int colorMix4(int a, int b, int c,  int d) {
		final int blue = (((a & 0xFF) + (b & 0xFF) + (c & 0xFF) + (d & 0xFF) + 1) >> 2) & 0xFF;
		final int green = (((a & 0xFF00) + (b & 0xFF00) + (c & 0xFF00) + (d & 0xFF00) + 0x100) >> 2) & 0xFF00;
		final int red = (((a & 0xFF0000) + (b & 0xFF0000) + (c & 0xFF0000) + (d & 0xFF0000) + 0x10000) >> 2) & 0xFF0000;
		return red | green | blue;
	}

	private static final ThreadLocal<BlockPos.Mutable> SEARCH_POS = ThreadLocal.withInitial(BlockPos.Mutable::new);
}
