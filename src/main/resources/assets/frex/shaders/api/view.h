/**************************************************************************
 * Specifies the variables and methods available in the FREX shader API
 * for coordinate spaces, transformations and camera information.
 *
 * See FREX Shader API.md for license and general informaiton.
 *************************************************************************/

/*
 * The view vector of the current camera in world space, normalised.
 */
const vec3 frx_cameraView;

/*
 * The view vector of the current entity focused by the camera, in world space, normalised.
 */
const vec3 frx_entityView;

/*
 * Current position of the camera in world space.
 */
const vec3 frx_cameraPos;

/*
 * Prior-frame position of the camera in world space.
 */
const vec3 frx_lastCameraPos;

/*
 * Translation from inbound model coordinates to world space. Conventionally
 * this is handled with a matrix, but because inbound coordinates outside of
 * overlay and GUI rendering are always world-aligned, this can avoid a
 * matrix multiplication.
 *
 * When frx_modelOriginType() == MODEL_ORIGIN_CAMERA, inbound coordinates are
 * relative to the camera position and this will equal frx_cameraPos().
 *
 * In overlay rendering, when frx_modelOriginType() == MODEL_ORIGIN_SCREEN, this
 * will always be zero.  The fourth component is always zero and included for
 * ease of use.
 */
const vec4 frx_modelToWorld;

/*
 * Translation from in-bound model coordinates to view space. Conventionally
 * this is handled with a matrix, but because inbound coordinates outside of
 * overlay and GUI rendering are always world-algined, this can avoid a
 * matrix multiplication.
 *
 * When frx_modelOriginCemera == 1, inbound coordinates are
 * already relative to the camera position and this will always be zero.
 *
 * In overlay rendering, when frx_modelOriginType() == MODEL_ORIGIN_SCREEN, this
 * will always be zero.  The fourth component is always zero and included for
 * ease of use.
 */
const vec4 frx_modelToCamera;

/*
 * When true, vertex coordinates are relative to the camera position.
 * Coordinates and normals are unrotated and frx_modelToWorld holds the camera position.
 *
 * Mutually exclusive with frx_modelOriginScreen and frx_modelOriginRegion.
 */
const bool frx_modelOriginCamera;

/*
 * When true, vertex coordinates are relative are relative to the origin of a
 * "cluster" of world render regions. Coordinates and normals are unrotated and
 * frx_modelToWorld holds the cluster origin.
 *
 * Mutually exclusive with frx_modelOriginScreen and frx_modelOriginCamera.
 */
const bool frx_modelOriginRegion;

/*
 * When true, vertex coordinates are relative to the screen and no transformations
 * should be applied. Intended for Hand//GUI rendering.
 *
 * Mutually exclusive with frx_modelOriginCamera and frx_modelOriginRegion.
 */
const bool frx_modelOriginScreen;

/*
 * True when rendering hand.
 */
const bool frx_isHand;

/*
 * True when rendering GUI.
 */
const bool frx_isGui;

const mat4 frx_guiViewProjectionMatrix;

/*
 * Converts camera/world space normals to view space.
 * Incoming vertex normals are always in camera/world space.
 */
const mat3 frx_normalModelMatrix;

const mat4 frx_viewMatrix;

const mat4 frx_inverseViewMatrix;

const mat4 frx_lastViewMatrix;

const mat4 frx_projectionMatrix;

const mat4 frx_lastProjectionMatrix;

const mat4 frx_inverseProjectionMatrix;

const mat4 frx_viewProjectionMatrix;

const mat4 frx_inverseViewProjectionMatrix;

const mat4 frx_lastViewProjectionMatrix;

// No view bobbing or other effects that alter projection
const mat4 frx_cleanProjectionMatrix;

// No view bobbing or other effects that alter projection
const mat4 frx_lastCleanProjectionMatrix;

// No view bobbing or other effects that alter projection
const mat4 frx_inverseCleanProjectionMatrix;

// No view bobbing or other effects that alter projection
const mat4 frx_cleanViewProjectionMatrix;

// No view bobbing or other effects that alter projection
const mat4 frx_inverseCleanViewProjectionMatrix;

// No view bobbing or other effects that alter projection
const mat4 frx_lastCleanViewProjectionMatrix;

const mat4 frx_shadowViewMatrix;

const mat4 frx_inverseShadowViewMatrix;

/*
 * Orthogonal projection matrix on light space for given cascade index 0-3.
 */
mat4 frx_shadowProjectionMatrix(int index);

/*
 * Combined lightspace view and orthogonal projection for given cascade index 0-3.
 */
mat4 frx_shadowViewProjectionMatrix(int index);

/*
 * Center and radius of other projection in light space for given cascade index 0-3.
 */
vec4 frx_shadowCenter(int index);

/*
 * Framebuffer width, in pixels.
 */
const float frx_viewWidth;

/*
 * Framebuffer height, in pixels.
 */
const float frx_viewHeight;

/*
 * Framebuffer width / height.
 */
const float frx_viewAspectRatio;

/*
 * User-configured brightness from game options.
 * Values 0.0 to 1.0, with 1.0 being max brightness.
 */
const float frx_viewBrightness;

/*
 * When == 1, shader is targetting the solid-pass framebuffer.
 * Mutually exclusive with all other frx_renderTarget_ variables.
 */
const bool frx_renderTargetSolid;

/*
 * When == 1, shader is targetting the translucent-pass framebuffer for terrain.
 * Mutually exclusive with all other frx_renderTarget_ variables.
 */
const bool frx_renderTargetTranslucent;

/*
 * When == 1, shader is targetting the translucent-pass framebuffer for particles.
 * Mutually exclusive with all other frx_renderTarget_ variables.
 */
const bool frx_renderTargetParticles;

/*
 * When == 1, shader is targetting the translucent-pass framebuffer for entities.
 * Mutually exclusive with all other frx_renderTarget_ variables.
 */
const bool frx_renderTargetEntity;

/*
 * Currently configured terrain view distance, in blocks.
 */
const float frx_viewDistance;

/*
 * Returns 1 when camera is in fluid. Zero otherwise.
 */
const int frx_cameraInFluid;

/*
 * Returns 1 when camera is in water. Zero otherwise.
 */
const int frx_cameraInWater;

/*
 * Returns 1 when camera is in lava. Zero otherwise.
 */
const int frx_cameraInLava;

