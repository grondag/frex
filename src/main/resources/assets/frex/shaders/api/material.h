/******************************************************
  This file (material.h) specifies the variables available in
  the FREX shader API to describe the material properties of
  the current primitive.

  To use this API, put this at the top of your shader code:

  #include frex:shaders/api/material.glsl

  Do NOT include this .h file. FREX uses c-style header files
  to document the shader API in a way that can be viewed with
  c-style syntax highlighting in almost any code editor.
  OpenGL shaders do not use header files.

  Note that implementations are provided by renderers.
  FREX does not include a material.glsl implementation file.
  Renderers must ship the implementation in the FREX namespace.

  HOW TO USE THIS SPECIFICATION
  -----------------------------
  Shader authors should use this file as a reference document.
  All of the definitions here are TYPED SYMBOLIC TOKENS.
  The implementation of a token may vary between renderer implementations.
  Variable implementations might be uniform, an input variable, or a global
  variable, at the discretion of the renderer implementation.

  Generally, this does not matter in any practical way, but if you think of a
  way to write code that would somehow depend on a specific implementation - don't!

  Note that `const` is used to signify that a variable is read-only.  This may or
  may not be enforced in the implementation, but const variables should never be
  modified by material or pipeline shaders.

******************************************************/

/*
 * True when material is emissive.
 * For emissive materials, this is on/off, not a range.
 */
const bool frx_matEmissive;

/**
 * Multiplicative version frx_matEmissive(), true returns 1, false returns 0
 */
const float frx_matEmissiveFactor;

/*
 * True when material is cutout. When enabled,
 * fragments will be discarded if alpha < 0.5.
 */
const bool frx_matCutout;

/** Multiplicative version frx_matCutout(), true return 1, false returns 0 */
const float frx_matCutoutFactor;

/*
 * True when material is has Level of Detail (mip mapping) disabled.
 * Currently the RenderMaterail finder only allows this for cutout materials.
 */
const bool frx_matUnmipped;

/** Multiplicative version frx_matUnmipped(), true return 1, false returns 0 */
const float frx_matUnmippedFactor;

/*
 * True when material is marked to disable ambient occlusion shading.
 */
const bool frx_matDisableAo;

/**
 * True when material is marked to disable "diffuse" shading.
 * This may have a different or no effect in non-vanilla lighting models.
 */
const bool frx_matDisableDiffuse;

/**
 * True when should render the red "hurt" overlay.
 * Mostly for use in pipeline shaders - material shaders aren't expected to handle.
 */
const bool frx_matHurt;

/**
 * True when should render the white "flash" overlay.
 * Mostly for use in pipeline shaders - material shaders aren't expected to handle.
 */
const bool frx_matFlash;

/**
 * Returns 1.0 if material has enchantment glint, 0.0 otherwise.
 */
const float frx_matGlint;

/**
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

/**
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
