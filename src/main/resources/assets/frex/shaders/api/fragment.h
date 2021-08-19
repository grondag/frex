/******************************************************
  ABOUT THIS FILE
  ---------------

  This file (fragment.h) documents the API specification for FREX
  material fragment shaders. To use this API, put this
  at the top of your shader code:

  #include frex:shaders/api/fragment.glsl

  Do NOT include this .h file. FREX uses c-style header files
  to document the shader API in a way that can be view with
  c-style syntax highlighting in almost any code editor.
  OpenGL shaders do not use header files.

  Note that implementations are provided by renderers.
  FREX does not include a fragment.glsl implementation file.
  Renderers must ship the implementation in the FREX namespace.


  HOW FREX MATERIAL FRAGMENT SHADERS WORK
  ---------------------------------------

  1) The renderer will test for discard caused by material condition.

  2) If discard does not happen, the renderer will initialize
  the variables used during fragment processing but will not
  make any computations that potentially depend on values output
  or changed by frx_materialFragment().

  3) The renderer will call frx_materialFragment() code for the
  material associated with the current triangle.  If the current
  material does not define a custom material shader,
  no special processing will happen.

  4) The renderer will test again for discard based on conditional rendering
  and cutout, discarding the fragment if appropriate.

  5) If discard does not happen, the renderer will call frx_pipelineFragment().
  Every pipeline must implement this method and use it to update
  all framebuffer attachments needed by the pipeline in this pass.

  The pipeline shader is responsible for ALL WRITES.
  The renderer will not update depth or any color attachment.


  HOW TO USE THIS SPECIFICATION
  -----------------------------
  Shader authors should use this file as a reference document
  for the variables available to them during fragment processing.
  Material shader authors will generally inspect and modify the
  variables, while pipeline authors will consume them for shading
  and fragment output.

  All of the definitions here are TYPED SYMBOLIC TOKENS.  The implementation
  of a token may vary between renderer implementations.  For example,
  frx_fragEmissive is a float in this specification but may be a component
  of a float vector in the implementation. In that example, the implementation
  would #define frx_fragEmissive so that it references the vector component.
  Variable implementations might be uniform, an input variable, or a global
  variable, at the discretion of the renderer implementation.

  Generally, this does not matter in any practical way, but if you think of a
  way to write code that would somehow depend on a specific implementation - don't!

  Note that `const` is used to signify that a variable is read-only.  This may or
  may not be enforced in the implementation, but const variables should never be
  modified by material or pipeline shaders.

******************************************************/

/******************************************************
 * VERTEX INPUTS
******************************************************/

/**
 * Interpolated vertex position in camera space.
 */
const vec4 frx_vertex;

/**
 * Interpolated texture coordinate. For atlas textures, this
 * is in mapped (non-normalized) coordinates. Material fragment
 * shaders that need normalized UV coordinates should capture
 * them in the vertex shader using one of the generic variables
 * defined below.
 */
const vec2 frx_texcoord;

/**
 * Interpolated vertex color from vertex shader.
 */
const vec4 frx_vertexColor;

/**
 * Interpolated vertex normal in world/camera space.
 * Not available in depth pass.
 */
const vec3 frx_vertexNormal;

/*
 * Interpolated block and sky light intensity for this
 * fragment. Block is X and sky is Y.
 * Encoding may depend on renderer configuration.
 *
 * Depending on the context or lighting model in effect, this
 * may be an interpolated vertex value, a value from
 * a texture lookup, or it may not be populated or used.
 *
 * Avoid using or modifying this value unless VANILLA_LIGHTING is defined.
 * The emissive flag is generally a better alternative.
 *
 * Not available in depth pass.
 */
const vec2 frx_vertexLight;

/*
 * Interpolated diffuse shading value from CPU lighting, 0 to 1.
 * Value from CPU is retained even if frx_matDisableDiffuse is true.
 *
 * Depending on the context or lighting model in effect,
 * this may not be populated or used.
 * Avoid using or modifying this value unless VANILLA_LIGHTING is defined.
 *
 * Not available in depth pass.
 */
const float frx_vertexDiffuse;

/*
 * Interpolated AO shading value from CPU lighting. 0 to 1.
 * Will be 1.0 if frx_matDisableAo is true or AO is disabled in the game.
 * Models macro-scale shadowing of world objects on each other.
 *
 * Depending on the context or lighting model in effect,
 * this may not be used and/or will always be 1.0.
 *
 * Not available in depth pass.
 */
const float frx_vertexAo;

/**
 * Interpolated camera distance. Used for fog.
 * Not available in depth pass.
 */
const float frx_distance;

/*
 * Varying variables for generic use. See comments in vertex.glsl.
 *
 * Not available in depth pass.
 */
const vec4 frx_var0;
const vec4 frx_var1;
const vec4 frx_var2;
const vec4 frx_var3;

/******************************************************
 * FRAGMENT INPUT SAMPLES
******************************************************/

/**
 * The unmodified color sampled from the base color texture.
 *
 * Interpretation of this color depends on frx_fragMetalness,
 * as described in the comments for that variable.
 */
const vec4 frx_sampleColor;

/******************************************************
 * FRAGMENT VARIABLES
 *
 * These can be modified by material shaders and are
 * consumed by pipelline shaders for shading and
 * framebuffer output.
******************************************************/

/**
 * The base color output for this fragment.
 * Initial value when frx_materialFragment() is called will
 * be frx_vertexColor * frx_sampleColor.
 *
 * Interpretation of this color depends on frx_fragMetalness,
 * as described in the comments for that variable.
 */
vec4 frx_fragColor;

/**
 * Controls if the fragment material is dialectric or metallic.
 * For dialectric materials, this provides the F0 value.
 *
 * The range of values is not continuous to maximize floating point
 * precision in useful ranges.  Dialectic materials
 * will have values ranging from 0.0 to 0.1 and metals will
 * have the value 1.0.
 *
 * When the material is dialectric, frx_fragColor models
 * diffuse reflected color and frx_fragSpecularReflectance
 * is the linear monochrome specular reflectance (F0).
 *
 * When the material is metallic, frx_fragColor
 * models specular reflectance, which for metals is often not
 * uniform across the light spectrum and responsible for the perceived
 * color of metals.
 *
 * Initial value when frx_materialFragment() is called will
 * be sampled from the metalness texture map if one is available, or
 * otherwise set to frx_matSpecularReflectance.
 *
 * Not available in depth pass.
 */
float frx_fragSpecularReflectance;

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
 * Block and sky light intensity for this fragment.
 * Block is X and sky is Y.
 * Encoding may depend on renderer configuration.
 * Initial value when frx_materialFragment() is called will
 * be frx_vertexLight.
 *
 * Depending on the context or lighting model in effect, this
 * may be an interpolated vertex value, a value from
 * a texture lookup, or it may not be populated or used.
 *
 * Avoid using or modifying this value unless VANILLA_LIGHTING is defined.
 * The emissive flag is generally a better alternative.
 *
 * Not available in depth pass.
 */
vec2 frx_fragLight;

/*
 * Diffuse shading value from CPU lighting, 0 to 1.
 * Initial value when frx_materialFragment() is called will
 * be frx_vertexDiffuse, or 1.0 if frx_matDisableDiffuse is true
 * or if the current pipeline disables vertex diffuse shading.
 *
 * Depending on the context or lighting model in effect,
 * this may not be used.
 *
 * Not available in depth pass.
 */
float frx_fragDiffuse;

/*
 * Base AO shading value to be consumed by pipeline for lighting.
 * This is a normalized value where 0 represents full
 * occlusion and 1 represents none.
 *
 * This value models self-shading of small variations in irregular surfaces.
 * It should not be used for shading by other objects in the world.
 * Macro-scale AO is controlled and applied by the pipeline but can be
 * controlled at a material level via frx_matDisableAo.
 *
 * Initial value when frx_materialFragment() is called will be sampled
 * from the AO texture map if one is available, or will otherwise be 1.0.
 * This is unaffected by frx_matDisableAo.
 *
 * The primary use for this by material shaders is to provide PBR support for
 * animated, proecedural surfaces.
 *
 * Not available in depth pass.
 */
float frx_fragAo;


/******************************************************
 * API METHODS
******************************************************/
