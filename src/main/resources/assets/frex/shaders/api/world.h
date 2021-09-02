/**************************************************************************
 * Specifies the variables and methods available in the FREX shader API
 * for access to world information.
 *
 * See FREX Shader API.md for license and general informaiton.
 *************************************************************************/

/*
 * The number of seconds this world has been rendering since the last render
 * reload, including fractional seconds.
 *
 * Use this for effects that need a smoothly increasing counter.
 */
const float frx_renderSeconds;

/*
 * The number of frames this world has been rendering since the last render
 * reload.
 *
 * Use this for effects that need a discrete increasing counter.
 */
const uint frx_renderFrames;

/*
 * Day of the currently rendering world - integer portion only.
 * This is the apparent day, not the elapsed play time, which can
 * be different due to sleeping, /set time, etc.
 *
 * Use this for effects that depend somehow on the season or age of the world.
 * Received from server - may not be smoothly incremented.
 */
const float frx_worldDay;

/*
 * Time of the currently rendering world with values 0 to 1.
 * Zero represents the morning / start of the day cycle in Minecraft.
 *
 * Use this for effects that depend on the time of day.
 * Received from server - may not be smoothly incremented.
 */
const float frx_worldTime;

/*
 * Size of the moon the currently rendering world. Values are 0 to 1.
 */
const float frx_moonSize;

/*
 * Rotation of the sky dome as part of the day/night cycle.
 * Will not advance if doDaylightCycle game rule is turned off.
 *
 * In vanilla dimensions, zero represents the condition at
 * tick = 0 (just after sunrise) and rotation is around the Z axis.
 *
 * Will be zero at noon, (world time = 6000) but vanilla adjusts the progression
 * so that the sun is slightly over the horizon at world time = 0 and
 * also takes its time setting as well.
 */
const float frx_skyAngleRadians;

/*
 * Normalized vector of the primary sky light (the sun or moon) used for
 * shadow mapping and useful for direct sky lighting. Points towards the skylight.
 * Will not advance if doDaylightCycle game rule is turned off.
 *
 * See notes on frx_skyAngleRadians() regarding asymmetry.
 */
const vec3 frx_skyLightVector;

/*
 * Linear RGB color of the most prevalent sky light - the one that is
 * used for shadow map and intended for directional lighting.
 *
 * Does not vary by time of day or for atmospheric effects but can vary
 * based on season or other celestial variables for modded dimensions
 * or if configured for vanilla dimensions.
 *
 * Not adjusted for night vision or any player effect. Does not account
 * for underwater or any other occlusion.
 * Not adjusted for rain, thunder, cloud cover, or lightning.
 *
 * Use the FRX_WORLD_IS_MOONLIT flag to query if this is sun or moonlight.
 * Will be unit vector if the world has no skylight.
 */
const vec3 frx_skyLightColor;

/*
 * Measures intensity of direct sky light in lumens/square meter. (lux)
 * Does not vary by time of day or for atmospheric effects but can vary
 * based on season or other celestial variables for modded dimensions
 * or if configured for vanilla dimensions. Also varies by moon phase
 * for moonlight if that feature is enabled.
 *
 * Not adjusted for night vision or any player effect. Does not account
 * for underwater or any other occlusion.
 * Not adjusted for rain, thunder, cloud cover, or lightning.
 *
 * FREX pegs vanilla sunlight illuminace at 32,000 lux.  Value could be
 * different for modded dimensions or if configured via resource pack.
 *
 * Moonlight in Vanilla appears to be more intense than in the real world.
 * If configured to mimic vanilla, moonlight will be ~100lux but
 * can be very small or zero (or perhaps higher) in other configurations.
 *
 * Values assume an idealized, non-specific reference white light.
 * When multiplied by frx_skyLightColor() the effective luminance
 * will be somewhat less, but shaders are not expected to compensate
 * for this. Most celestial light sources are some flavor of white,
 * and stongly-colored lights can be handled by adjusting the illuminance
 * in the mod/pack configuration if needed.
 *
 * Effective illuminance will also be reduced if light is multiplied by
 * frx_skyLightAtmosphericColor() but that should not require any
 * compensation because it mimics the scattering of light. Pipelines
 * that strive for physical realism will not use that factor anyway.
 *
 * Use the FRX_WORLD_IS_MOONLIT flag to query if this is sun or moonlight.
 * Will be zero if the world has no skylight.
 */
const float frx_skyLightIlluminance;

/*
 * Linear RGB color modifier for the most prevalent sky light - the one
 * that is used for shadow map and intended for directional lighting.
 *
 * Also adjusted by time of day to account for atmospheric effects.
 * Pipelines that model atmospheric scattering will not want this.
 *
 * Not adjusted for weather, lightning, night vision or any player effect.
 * Does not account for underwater or any other occlusion.
 *
 * Use the FRX_WORLD_IS_MOONLIT flag to query if this is sun or moonlight.
 * Will be unit vector if the world has no skylight.
 */
const vec3 frx_skyLightAtmosphericColor;

/*
 * Smoothing factor to help with the transition from sun to/from moon light.
 *
 * Ramps down to zero down as the moon set and then back up to
 * one as the sun rises. In future, will be configurable by dimension.
 *
 * Use the FRX_WORLD_IS_MOONLIT flag to query if this is sun or moonlight.
 * Will be 1.0 (and mean nothing) if the world has no skylight.
 */
const float frx_skyLightTransitionFactor;

/*
 * Ambient light intensity of the currently rendering world.
 * Zero represents the morning / start of the day cycle in Minecraft.
 *
 * Experimental, likely to change.
 */
const float frx_ambientIntensity;

/*
 * Gamma-corrected max light color from lightmap texture.
 * Updated whenever lightmap texture is updated.
 *
 * Multiply emissive outputs by this to be consistent
 * with the game's brightness settings.
 *
 * Note that Canvas normally handles this automatically.
 * It is exposed for exotic use cases.
 */
const vec4 frx_emissiveColor;

/*
 * MC rain strength. Values 0 to 1.
 */
const float frx_rainGradient;

/*
 * MC thunder strength. Values 0 to 1.
 */
const float frx_thunderGradient;

/**
 * Same as frx_rainGradient but with exponential smoothing.
 * Speed is controlled in pipeline config.
 */
const float frx_smoothedRainGradient;

/*
 * The background clear color as computed by vanilla logic. Incorporates
 * many different factors.  For use by pipelines that may want to modify
 * and do their own clearing operations. Pipelines can disable the vanilla
 * clear pass in pipeline config.
 *
 *
 * Material shader authors note: this may not be the actual clear color used
 * by the in-effect pipeline. (It might not clear with a single color at all!)
 */
const vec3 frx_vanillaClearColor;

/*
 * True when the currently rendering world has a sky with a light source. Zero otherwise.
 */
const int frx_worldHasSkylight;

/*
 * Equals 1 when the currently rendering world is the Overworld. Zero otherwise.
 */
const int frx_worldIsOverworld;

/*
 * Equals 1 when the currently rendering world is the Nether. Zero otherwise.
 */
const int frx_worldIsNether;

/*
 * Equals 1 when the currently rendering world is the End. Zero otherwise.
 */
const int frx_worldIsEnd;

/*
 * Equals 1 when world.isRaining() is true for the currently rendering world.
 */
const int frx_worldIsRaining;

/*
 * Bet you can guess.
 */
const int frx_worldIsThundering;

/*
 * Equals 1 when world.getSkyProperties().isDarkened() is true for the currently rendering world.
 * Zero otherwise. 
 * 
 * True (==1) in Nether - indicates diffuse lighting bottom face is same as top, not as bright.
 */
const int frx_worldIsSkyDarkened;

/*
 *  Equals 1 when the sky light modeled by frx_skyLightStrength and frx_skyLightVector is the moon. Zero otherwise.
 */
const int frx_worldIsMoonlit;

/*
 * Returns 1 when the condition is true, zero otherwise.
 */
int frx_conditionTest(int conditionIndex);
