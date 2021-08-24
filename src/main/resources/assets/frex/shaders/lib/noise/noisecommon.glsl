/******************************************************
  frex:shaders/lib/noise/noisecommon.glsl

  External MIT noise library - bundled for convenience.

  Modifications include:
  + remove the #version header
  + add this comment block
  + move some shared functions to this file
  + use #define for some small functions
******************************************************/

#define mod289(x) (x - floor(x * (1.0 / 289.0)) * 289.0) // Modulo 289 without a division (only multiplications)
#define mod7(x) (x - floor(x * (1.0 / 7.0)) * 7.0) // Modulo 7 without a division
#define taylorInvSqrt(r) (1.79284291400159 - 0.85373472095314 * r)

// Permutation polynomial: (34x^2 + x) mod 289, ring size 289 = 17*17
// Can't be defined because used recursively
vec4 permute(vec4 x) { return mod289((34.0 * x + 1.0) * x); }
vec3 permute(vec3 x) { return mod289((34.0 * x + 1.0) * x); }
float permute(float x) { return mod289(((x*34.0)+1.0)*x); }
