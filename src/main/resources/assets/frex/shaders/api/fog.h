
/******************************************************
  This file (fog.h) specifies the variables available in
  the FREX shader API to describe fog parameters for the
  current primitive.

  To use this API, put this at the top of your shader code:

  #include frex:shaders/api/fog.glsl

  Do NOT include this .h file. FREX uses c-style header files
  to document the shader API in a way that can be viewed with
  c-style syntax highlighting in almost any code editor.
  OpenGL shaders do not use header files.

  Note that implementations are provided by renderers.
  FREX does not include a fog.glsl implementation file.
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

/**
 * True if fog is enabled for the current render pass.
 */
const bool frx_fogEnabled;

/**
 * The starting distance for fog, following the conventions
 * of vanilla fog rendering. Some pipelines with realistic
 * fog modeling may not use this or may use it differently.
 */
const float frx_fogStart;

/**
 * The end distance for fog, following the conventions
 * of vanilla fog rendering. Some pipelines with realistic
 * fog modeling may not use this or may use it differently.
 */
const float frx_fogEnd;

/**
 * The color of fog used in vanilla fog rendering for the
 * current rendering pass. Some pipelines with realistic
 * fog modeling may not use this or may use it differently.
 */
const vec4 frx_fogColor;
