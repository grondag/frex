
/****************************************************************
 * Specifies the variables available in the FREX shader
 * API to describe fog parameters for the current primitive.
 *
 * See FREX Shader API.md for license and general informaiton.
 ***************************************************************/

/*
 * Equals 1 if fog is enabled for the current render pass.
 */
const int frx_fogEnabled;

/*
 * The starting distance for fog, following the conventions
 * of vanilla fog rendering. Some pipelines with realistic
 * fog modeling may not use this or may use it differently.
 */
const float frx_fogStart;

/*
 * The end distance for fog, following the conventions
 * of vanilla fog rendering. Some pipelines with realistic
 * fog modeling may not use this or may use it differently.
 */
const float frx_fogEnd;

/*
 * The color of fog used in vanilla fog rendering for the
 * current rendering pass. Some pipelines with realistic
 * fog modeling may not use this or may use it differently.
 */
const vec4 frx_fogColor;
