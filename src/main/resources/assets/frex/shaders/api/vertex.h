/****************************************************************
 * Specifies the variables and methods in the FREX shader
 * API sepcific to vertex shaders.
 *
 * See FREX Shader API.md for license and general informaiton.
 ***************************************************************/

/*
 * Vertex position in camera space. Transformation in frx_materialVertex()
 * is the primary means for achieving animation effects.
 * Remember that normals must be transformed separately!
 */
out vec4 frx_vertex;

/*
 * The texture coordinates for the vertex. Used for base color and PBR texture maps.
 * For atlas textures, the renderer will initialize this with normalized 0-1 coordinates 
 * before calling frx_materialVertex() and convert them to mapped (non-normalized) 
 * atlas coordinates before calling frx_pipelineVertex();
 */
out vec2 frx_texcoord;

/*
 * RGB vertex color, with alpha.
 * Usage of alpha is controlled by the material blend mode:
 * 		if cutout is enabled, fragment will be discarded (see: api/material.glsl)
 * 		if SHADER_PASS != SHADER_PASS_SOLID fragment will be blended using the alpha channel
 *		in other cases (solid pass rendering) alpha is ignored and could be used for other purposes
 *
 * Vertex color is not transformed by the renderer.
 * The renderer must not modify vertex alpha values given from the model
 * in the solid render pass, even if the data is not used. This means mod 
 * authors can use that value for other purposes in a material shader.
 */
out vec4 frx_vertexColor;

/*
 * Vertex normal in camera/world space. Transformation in frx_materialVertex()
 * is the primary means for achieving animation effects.
 * Transforming normal in addition to vertex is important for correct lighting.
 */
out vec3 frx_vertexNormal;

/*
 * Initialized with vanilla lighting data from CPU.
 * Depending on the context or lighting model in effect,
 * this may not be a unit vector and/or unused.
 * The emissive flag or an emissive texture map (when supported)
 * are generally better alternatives for achieving emissive lighting effects.
 *
 * All values are normalized (0-1).
 *
 * Components are as follows:
 * X: Block light intensity.
 * Y: Sky light intensity.
 * Z: AO shading. Will be initialized to 1.0 if frx_matDisableAo is true or AO is disabled in the game.
 *
 * Not available in depth pass.
 * Gate usage with #ifndef DEPTH_PASS.
 */
out vec3 frx_vertexLight;


/*
 * Varying variables for generic use in material shaders. Material
 * shader authors are encouraged to exhaust these before creating new
 * custom out variables.
 *
 * This is necessary because custom shaders may be consolidated
 * into a single shader with logic controlled via uniforms or vertex data.
 * This is done either to reduce draw calls or as a way to achieve
 * sorted translucency with mixed custom shaders.
 *
 * If we do not reuse these variables, then three bad things can happen:
 *   1) Naming conflicts (could be avoided with care)
 *   2) Exceed hardware/driver limits
 *   3) Wasteful interpolation if unused varyings aren't stripped by the compiler.
 *
 * Authors do not need to worry about conflicting usage of these variables
 * by other shaders in the same compilation - only a single pair of custom
 * vertex/fragment shaders will be active for a single polygon.
 *
 * Note that pipeline shader devs should NOT use these.  There will only
 * ever be a single pipeline active at any time - piplines can define as
 * many out variables as needed, within reason.
 *
 * Not available in depth pass.
 */
out vec4 frx_var0;
out vec4 frx_var1;
out vec4 frx_var2;
out vec4 frx_var3;

/**
 * Interpolated camera distance. Used for fog.
 * Set by renderer after material shader runs. Do not modify.
 *
 * Not available in depth pass.
 */
out float frx_distance;

/****************************************************************
 * API METHODS
****************************************************************/

 /*
  * Called by renderer after all variables are initialized and before
  * frx_pipelineVertex() is called to update vertex outputs.
  *
  * The running vertex shader will have multiple, renamed versions of
  * this method - one for each unique material vertex shader present
  * in the game. The specific method called is controlled by the material
  * associated with the current triangle.  If the current material does
  * not define a custom material shader, no extra processing will happen
  * before frx_pipelineVertex() is called.
  */
void frx_materialVertex();

/*
 * Called by renderer after frx_materialVertex() completes.
 * Pipeline authors implement this method to read vertex data,
 * apply transformation and update as needed. Pipeline vertex shaders
 * may also capture additional vertex outputs as neeed.
 * These steps are specific to the design of each pipeline.
 *
 * The pipeline shader is responsible for ALL updates to vertex outputs,
 * including those defined by the FREX API.
 */
void frx_pipelineVertex();

