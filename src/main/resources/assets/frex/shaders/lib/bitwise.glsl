/******************************************************
  frex:shaders/lib/bitwise.glsl
  DEPRECATED - No longer useful now that we have 
  reliable bitwide operations in GLSL.
******************************************************/

#define frx_bitValue(byteValue, bitIndex) float((byteValue >> bitIndex) & 1u)
