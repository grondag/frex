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

/*
 * Controls if the fragment material is dialectric or metallic.
 * For dialectric materials, this provides the F0 value.
 *
 * The range of values is not continuous to maximize floating point
 * precision in useful ranges.  Dialectic materials
 * will have values ranging from 0.0 to 0.1 and metals will
 * have the value 1.0.
 *
 * When the material is dialectric, the sprite texture color models
 * diffuse reflected color and this value is the linear monochrome
 * specular reflectance (F0).
 *
 * When the material is metallic, the sprite texture color models
 * models specular reflectance, which for metals is often not
 * uniform across the light spectrum and responsible for the perceived
 * color of metals.
 *
 * This value is only used when PBR rendering is active and a specular
 * reflectance map texture is not not available.
 */
const float frx_matReflectance;

/*
 * Models random light scattering caused by tiny surface irregularities.
 * Values range from 0.0 (perfectly smooth) to 1.0 (rough as possible.)
 *
 * This value is only used when PBR rendering is active and a roughness
 * map texture is not not available.
 */
const float frx_matRoughness;
