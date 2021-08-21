/**************************************************************************
 * Specifies the variables and methods available in the
 * FREX shader API to access samplers directly.
 *
 * See FREX Shader API.md for license and general informaiton.
 *************************************************************************/

uniform sampler2D frxs_baseColor;

/*
 * When a texture atlas is in use, the renderer will automatically
 * map from normalized coordinates to texture coordinates before the
 * fragment shader runs. But this doesn't help if you want to
 * re-sample during fragment shading using normalized coordinates.
 *
 * This function will remap normalized coordinates to atlas coordinates.
 * It has no effect when the bound texture is not an atlas texture.
 */
vec2 frx_mapNormalizedUV(vec2 coord);

/*
 * Takes texture atlas coordinates and remaps them to normalized.
 * Has no effect when the bound texture is not an atlas texture.
 */
vec2 frx_normalizeMappedUV(vec2 coord);

uniform sampler2D frxs_lightmap;

/*
 * Shadow-type sampler for shadow map texture, useful for final map testing
 * and exploits hardware accumulation of shadow test results
 * 
 * Same underlying image as frxs_shadowMapTexture.
 * 
 * Available only in fragment shader when SHADOW_MAP_PRESENT is defined.
 */
uniform sampler2DArrayShadow frxs_shadowMap;

/*
 * Regular sampler for shadow map texture, useful  for
 * probing depth at specific points for PCSS or Contact-Hardening Shadows.
 * 
 * Same underlying image as frxs_shadowMapTexture.
 * 
 * Available only in fragment shader when SHADOW_MAP_PRESENT is defined.
 */
uniform sampler2DArray frxs_shadowMapTexture;
