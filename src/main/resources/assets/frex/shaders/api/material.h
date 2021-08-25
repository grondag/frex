/**************************************************************************
 * Specifies the variables and methods available in the
 * FREX shader API to describe the material properties of
 * the current primitive.in
 *
 * See FREX Shader API.md for license and general informaiton.
 *************************************************************************/

/*
 * Equals 1 when material is emissive, zero otherwise.
 * For emissive materials, this is on/off, not a range.
 */
const int frx_matEmissive;

/*
 * Equals 1 when material is cutout. When 1,
 * fragments will be discarded if alpha < 0.5.
 */
const int frx_matCutout;

/*
 * Equals 1 when material has Level of Detail (mip mapping) disabled. Zero otherwise.
 * Currently the RenderMaterail finder only allows this for cutout materials.
 */
const int frx_matUnmipped;

/*
 * Equals 1 when material is marked to disable ambient occlusion shading. Zero otherwise.
 */
const int frx_matDisableAo;

/*
 * Equals 1 when material is marked to disable "diffuse" shading. Zero otherwise.
 * This may have a different or no effect in non-vanilla lighting models.
 */
const int frx_matDisableDiffuse;

/*
 * Equals 1 when should render the red "hurt" overlay. Zero otherwise.
 * Mostly for use in pipeline shaders - material shaders aren't expected to handle.
 */
const int frx_matHurt;

/*
 * Equals 1 when should render the white "flash" overlay. Zero otherwise.
 * Mostly for use in pipeline shaders - material shaders aren't expected to handle.
 */
const int frx_matFlash;

/*
 * Equals 1 if material has enchantment glint, 0 otherwise.
 */
const int frx_matGlint;

/*
 * RESERVED FOR FUTURE FEATURE - not yet implemented.
 *
 * Coarse indication of where the surface is located.
 * Zero means the surface is not exposed to the sky and
 * cannot get wet or otherwise be affected from directly above.
 *
 * Values 1.0, 2.0 and 3.0 indicate icy, temperate or hot biome
 * temperatures, respectively.
 *
 * Values > 0 also mean the surface exposed to the sky.
 * Does not mean it is facing up - check normals for that -
 * but does it mean it could get wet/icy/etc.
 */
const float frx_matExposure;
