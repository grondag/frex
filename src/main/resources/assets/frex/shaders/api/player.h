/**************************************************************************
 * Specifies the variables and methods available in the
 * FREX shader API to access infomration about the player.
 *
 * See FREX Shader API.md for license and general informaiton.
 *************************************************************************/

/*
 * Magnitude of effects that affect vision: night vision, being in fluid, etc.
 * Experimental, likely to change.
 */
const float frx_effectModifier;

/*
 * Color and magnitude of light source held by player in either hand.
 * RGB are the light color, alpha channel holds the 0-1 magnitude.
 *
 * Magnitude 1 currently represents a source that can reach 15 blocks.
 * This scale is subject to change.
 *
 * If the player is not holding a light source, all values are zero.
 */
const vec4 frx_heldLight;

/**
 * A value less than 2PI radians should create a spot light effect.
 * This is the angle of full brightness within the light cone.
 * Attenuation is assumed to be the same as for non-spot lights.
 */
const float frx_heldLightInnerRadius;

/*
 * The angle of reduced brightness around the inner light cone.
 * If greater than frx_heldLightInnerConeAngle should create a
 * fall-off effect around a spot light.
 * Attenuation is assumed to be the same as for non-spot lights.
 */
const float frx_heldLightOuterRadius;

// Player effect indicators.
// Will equal 1 when player has the given effect.
// Includes all vanilla player effects as of 1.16.4
const int frx_effectSpeed;
const int frx_effectSlowness;
const int frx_effectHast;
const int frx_effectMiningFatigue;
const int frx_effectStrength;
const int frx_effectInstantHealth;
const int frx_effectInstantDamage;
const int frx_effectJumpBoost;
const int frx_effectNausea;
const int frx_effectRegeneration;
const int frx_effectResistance;
const int frx_effectFireResistance;
const int frx_effectWaterBreathing;
const int frx_effectInvisibility;
const int frx_effectBlindness;
const int frx_effectNightVision;
const int frx_effectHunger;
const int frx_effectWeakness;
const int frx_effectPoison;
const int frx_effectWither;
const int frx_effectHealthBoost;
const int frx_effectAbsorption;
const int frx_effectSaturation;
const int frx_effectGlowing;
const int frx_effectLevitation;
const int frx_effectLuck;
const int frx_effectUnluck;
const int frx_effectSlowFalling;
const int frx_effectConduitPower;
const int frx_effectDolphinsGrace;
const int frx_effectBadOmen;
const int frx_effectHeroOfTheVillage;

// Player situation indicators.
// Will equal 1 when the situation is true.
const int frx_playerEyeInFluid;
const int frx_playerEyeInWater;
const int frx_playerEyeInLava;
const int frx_playerSneaking;
const int frx_playerSwimming;
const int frx_playerSneakingPose;
const int frx_playerSwimmingPose;
const int frx_playerCreative;
const int frx_playerSpectator;
const int frx_playerRiding;
const int frx_playerOnFire;
const int frx_playerSleeping;
const int frx_playerSprinting;
const int frx_playerWet;

/*
 * Value of timer that triggers "spooky" sounds when player is underground. Range 0-1.
 */
const float frx_playerMood;

/*
 * Eye position in world coordinates.
 */
const vec3 frx_eyePos;

/*
 * Normalized, linear light level at player/viewer eye position.
 * Zero is no light and 1 is max. No correction for gamma, dimension, etc.
 * Component x is block and y is sky.
 */
const vec2 frx_eyeBrightness;

/*
 * Same as frx_eyeBrightness but with exponential smoothing.
 * Optionally, can smooth only decreases, leaving increases instant.
 * Speed & bidirectionality are controlled in pipeline config.
 */
const vec2 frx_smoothedEyeBrightness;
