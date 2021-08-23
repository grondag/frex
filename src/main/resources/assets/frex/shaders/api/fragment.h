/****************************************************************
 * Specifies the variables and methods in the FREX shader
 * API sepcific to fragment shaders.
 *
 * See FREX Shader API.md for license and general informaiton.
 ***************************************************************/

/****************************************************************
 * VERTEX INPUTS
 ***************************************************************/

/*
 * Interpolated vertex position in camera space.
 */
in vec4 frx_vertex;

/*
 * Interpolated texture coordinate. For atlas textures, this
 * is in mapped (non-normalized) coordinates. Material fragment
 * shaders that need normalized UV coordinates should capture
 * them in the vertex shader using one of the generic variables
 * defined below.
 */
in vec2 frx_texcoord;

/*
 * Interpolated vertex color from vertex shader.
 */
in vec4 frx_vertexColor;

/*
 * Interpolated vertex normal in world/camera space.
 * Not available in depth pass.
 */
in vec3 frx_vertexNormal;

/*
 * Interpolated vanilla lighting data from CPU.
 * Depending on the context or lighting model in effect,
 * this may not be a unit vector and/or unused.
 * The emissive flag or an emissive texture map (when supported)
 * are generally better alternatives for achieving emissive lighting effects.
 *
 * All values are normalized (0-1).
 * X: Block light intensity from vertex shader.
 * Y: Sky light intensity from vertex shader.
 * Z: AO shading from vertex shader.
 *
 * Not available in depth pass.
 */
in vec3 frx_vertexLight;

/*
 * Interpolated camera distance. Used for fog.
 * Not available in depth pass.
 */
in float frx_distance;

/*
 * Varying variables for generic use. See comments in vertex.glsl.
 *
 * Not available in depth pass.
 */
in vec4 frx_var0;
in vec4 frx_var1;
in vec4 frx_var2;
in vec4 frx_var3;

/****************************************************************
 * FRAGMENT INPUT SAMPLES
 ***************************************************************/

/*
 * The unmodified color sampled from the base color texture.
 *
 * Interpretation of this color depends on frx_fragMetalness,
 * as described in the comments for that variable.
 */
const vec4 frx_sampleColor;

/****************************************************************
 * FRAGMENT VARIABLES
 *
 * These can be modified by material shaders and are
 * consumed by pipelline shaders for shading and
 * framebuffer output.
 ***************************************************************/

/*
 * The base color output for this fragment.
 * Initial value when frx_materialFragment() is called will
 * be frx_vertexColor * frx_sampleColor.
 *
 * Interpretation of this color depends on frx_fragMetalness,
 * as described in the comments for that variable.
 */
vec4 frx_fragColor;

/*
 * Controls if the fragment material is dialectric or metallic.
 * For dialectric materials, this provides the F0 value.
 *
 * The range of values is not continuous to maximize floating point
 * precision in useful ranges.  Dialectic materials
 * will have values ranging from 0.0 to 0.1 and metals will
 * have the value 1.0.
 *
 * When the material is dialectric, frx_fragColor models
 * diffuse reflected color and frx_fragReflectance
 * is the linear monochrome specular reflectance (F0).
 *
 * When the material is metallic, frx_fragColor
 * models specular reflectance, which for metals is often not
 * uniform across the light spectrum and responsible for the perceived
 * color of metals.
 *
 * Initial value when frx_materialFragment() is called will
 * be sampled from the metalness texture map if one is available, or
 * otherwise set to frx_matReflectance.
 *
 * Not available in depth pass.
 */
float frx_fragReflectance;

/*
 * Fragment normal in tangent space.
 *
 * Initial value when frx_materialFragment() is called will
 * be sampled from the normal map if one is available, or
 * otherwise (0, 0, 1).
 *
 * The pipeline shader will consume this for lighting. If any
 * transformation is needed because of smoothed vertex normals or
 * because the pipeline computes lighting in world space, those
 * transformations will be done by the pipeline shader
 * after frx_materialFragment() runs.
 *
 * Not available in depth pass.
 */
vec3 frx_fragNormal;

/*
 * Base height map value to be consumed by pipeline for lighting.
 * Values range from -0.25 to 0.25 and represent distance below or above
 * the triangle plane in tangent space.
 *
 * Initial value when frx_materialFragment() is called will be sampled
 * from the height texture map if one is available, or will otherwise be 0.0.
 *
 * The primary use for this by material shaders is to provide PBR support for
 * animated, proecedural surfaces.
 *
 * Not available in depth pass.
 */
float frx_fragHeight;

/*
 * Models random light scattering caused by tiny surface irregularities.
 * Values range from 0.0 (perfectly smooth) to 1.0 (rough as possible.)
 *
 * Initial value when frx_materialFragment() is called will be sampled
 * from the roughness texture map if one is available, or will otherwise be
 * set to frx_matRoughness.
 *
 * Not available in depth pass.
 */
float frx_fragRoughness;

/*
 * Emissive value of this fragment.
 *
 * Initial value when frx_materialFragment() is called will
 * be frx_sampleEmissive if material maps are available, or
 * frx_matEmissiveFactor otherwise.
 *
 * The pipeline shader will consume this for lighting after
 * frx_materialFragment() runs.
 *
 * Not available in depth pass.
 */
float frx_fragEmissive;

/*
 * Lighting data for this fragment.
 * Depending on the context or lighting model in effect,
 * these values may not be used or may be used differently.
 * The emissive flag or an emissive texture map (when supported)
 * are generally better alternatives for achieving emissive lighting effects.
 *
 * All values are normalized (0-1).
 * X: Block light intensity. Initialized to frx_vertexLight.x.
 * Y: Sky light intensity. Initialized to frx_vertexLight.y;
 * Z: Macro AO shading. Initialized to frx_vertexLight.z
 *
 * Not available in depth pass.
 */
vec3 frx_fragLight;

/*
 * Models self-shading of small variations in irregular surfaces.
 * It should not be used for shading by other objects in the world.
 *
 * Initial value when frx_materialFragment() is called will be sampled
 * from the AO texture map if one is available, or will otherwise be 1.0.
 * This is unaffected by frx_matDisableAo.
 *
 * The primary use for this by material shaders is to provide PBR support for
 * animated, proecedural surfaces.
 *
 * Macro-scale AO is controlled and applied by the pipeline.  See frx_fragDisableAo.
 */
float frx_fragAo;

/*
 * Controls macro-scale AO shading applied by the pipeline based on
 * proxmity of other objects in the world. Initialized to !frx_matDisableAo.
 * Depending on the context or lighting model in effect,
 * this may not be used or may be used differently.
 */
bool frx_fragEnableAo;

/*
 * Controls direcional shading applied by the pipeline based on
 * surface normals and light sources. Initialized to !frx_matDisableDiffuse.
 * Depending on the context or lighting model in effect,
 * this may not be used or may be used differently.
 */
bool frx_fragEnableDiffuse;

/****************************************************************
 * API METHODS
****************************************************************/

 /*
  * Called by renderer after all variables are initialized and before
  * frx_pipelineFragment() is called to output a fragment.
  *
  * The running fragment shader will have multiple, renamed versions of
  * this method - one for each unique material fragment shader present
  * in the game. The specific method called is controlled by the material
  * associated with the current triangle.  If the current material does
  * not define a custom material shader, no extra processing will happen
  * before frx_pipelineFragment() is called.
  */
void frx_materialFragment();

/*
 * Called by renderer after frx_materialFragment() completes.
 * Pipeline authors implement this method to read fragment data,
 * apply transformation or shading and output to one or more framebuffer
 * attachments.  These steps are specific to the design of each pipeline.
 *
 * The pipeline shader is responsible for ALL WRITES.
 * The renderer will not update depth or any color attachment.
 */
void frx_pipelineFragment();
