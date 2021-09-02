#version 330

/****************************************************************
 * Specifies the definitions used in the FREX shader
 * API to indicate the operating mode of renderer.
 *
 * Typical usage is to control conditional compilation of
 * features that may not work or work differently in,
 * for example, GUI vs world rendering.
 *
 * These definitions will be automatically included by the
 * renderer implementation at the top of the combined source
 * file. It is never necessary to include header.h or header.glsl.
 *
 * Do not declare versions in any pipeline or material shader
 * source files.  GLSL version is controlled by the active pipeline
 * and will be AT LEAST 330 but can be higher if the active pipeline
 * requests it.  The renderer will automatically pre-pend the
 * configured version at the top of the combined source file and
 * will strip any version declarations from input source files.
 *
 * See FREX Shader API.md for license and general informaiton.
 ***************************************************************/

// If not present, lightmaps and other vanilla-specific data will not be valid or may not present.
// Access to vanilla lighting data should be guarded by #ifdef on this constant.
// Controlled by the active pipeline.
#define VANILLA_LIGHTING

// present in world context only when feature is enabled - if not present then foliage shaders should NOOP
#define ANIMATED_FOLIAGE

// Will define VERTEX_SHADER or FRAGMENT_SHADER - useful for checks in common libraries
#define VERTEX_SHADER

// Present only when pipeline supports the shadowmap feature and it is enabled
#define SHADOW_MAP_PRESENT

// Present only when shadow map enabled
#define SHADOW_MAP_SIZE 1024

// Present when material shaders are being run to generate a shadow map or depth math
#define DEPTH_PASS

// Present when extended texture maps to support Physically-Based Rendering are available
// Will not be defined during depth/shadow pass.
#define PBR_ENABLED
