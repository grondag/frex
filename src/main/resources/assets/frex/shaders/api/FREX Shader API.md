**LICENSE**

Licensed under the Apache License, Version 2.0 (the "License"); you may not
use this file except in compliance with the License.  You may obtain a copy
of the License at

  http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the
License for the specific language governing permissions and limitations under
the License.


**ABOUT FREX HEADER FILES**

Shader authors should use these header files as reference documents for the
variables and methods available to them in shaders.

FREX uses c-style header files to document the shader API in a way that can be
viewed with c-style syntax highlighting in almost any code editor.
OpenGL shaders do not use header files and FREX API header files are not 
distributed as part of built jars. (Obtain them from the git repo directly.)

Declarations in header files convey data type, mutability and sybolic token.
They do NOT convey implementation.  The implementation of a token may vary
between renderer implementations.  For example, a float variable in a header
specification could be implemented as a uniform, input variable or global variable
and may be a component within a float vector, or decoded as needed from some
other type or structure, at the discretion of the renderer implementation.

Generally, this does not matter in any practical way, but if you think of a
way to write code that would somehow depend on a specific implementation - don't!

Note that `const` is used to signify that a variable is read-only.  This may or
may not be enforced in the implementation, but const variables should never be
modified by material or pipeline shaders.


**HOW FREX SHADERS WORK**

FREX shaders have three actors:
1) A FREX-compliant renderer that implements the API, marshalls data from 
the game into the GPU, and manages shader compilation, activation and draw
calls.

2) Material shaders that are created by mod and resource pack makers and 
then attached to surfaces using the FREX API or JSON loaders. 

3) Pipeline shaders that consume vertex and fragment output to implement
lighting and visual effects and ultimately write to framebuffer attachements.
The "look" of the game is largely controlled by the pipeline and it's 
configuration.  Players choose which pipeline is active and how it is 
configured.


Vertex shader processing happens as follows:

  1) The renderer will initialize vertex variable from buffer
  data. (Vertex formats are managed entirely by the renderer.)
  
  2) The renderer will call frx_materialVertex() code for the
  material associated with the current triangle.  If the current
  material does not define a custom material shader,
  no special processing will happen.
  
  3) The renderer will remap `frx_texcoord` to atlas coordinates for
  materials that use atlas textures.
  
  4) The renderer will call frx_pipelineVertex().
  Every pipeline must implement this method and use it to update 
  all vertex variables needed by the pipeline and by subsequent fragment
  processing, INCLUDING `frx_distance` and `gl_Position`.
 
Fragment shader processing happens as follows:
 
  1) The renderer will test for discard caused by material condition.

  2) If discard does not happen, the renderer will initialize
  the variables used during fragment processing but will not
  make any computations that potentially depend on values output
  or changed by frx_materialFragment().

  3) The renderer will call frx_materialFragment() code for the
  material associated with the current triangle.  If the current
  material does not define a custom material shader,
  no special processing will happen.

  4) The renderer will test again for discard based on conditional rendering
  and cutout, discarding the fragment if appropriate.

  5) If discard does not happen, the renderer will call frx_pipelineFragment().
  Every pipeline must implement this method and use it to update
  all framebuffer attachments needed by the pipeline in this pass.

  The pipeline shader is responsible for ALL WRITES.
  The renderer will not update depth or any color attachment.
  
**COORDINATE SPACES**
Coordinate system terminology used in this API includes:

*Model Space*
Block-aligned coordinates without camera rotation, relative
to an origin that may vary. Translate to camera or world space
with frx_modelToWorld() or frx_modelToCamera().

*Camera Space*
Block-aligned coordinates without camera rotation, relative
to the camera position. This is the coordinate system for
inbound coordinates other than terrain and overlays.
Translate to world space by adding frx_cameraPos().
Translate to view space with frx_viewMatrix().

*World Space*
Block-algined coordinates in absolute MC world space.
These coordinates will suffer the limits of floating point
precision when the player is far from the world origin and
thus they are impractical for some use cases.
Translate to camera space by subtracting frx_cameraPos().

*View Space*
Like camera space, but view is rotates so that the Z axis extends
in the direction of the camera view.
Translate to screen space with frx_projectionMatrix().

*Tangent Space*
The space used by normal maps and height maps.  In this coordinate
system X and Y are within the plane of the primitive and Z is normal
to the plane.

*Screen Space*
2D projection of the scene with depth.


**HOW TO USE THIS SPECIFICATION**

To use the API described in a header files, replace the ".h" suffix with ".glsl"
and put this at the top of your shader source files:

  #include frex:shaders/api/<header_file_name>.glsl
 
Implementations files are provided by renderers and a FREX-compliant renderer
must be present for APIs to function. FREX does not include implementation files.
Renderer jars must include the implementation in 'assets/frex/shaders/api/'.
  
Material and pipeline shaders use the same FREX API specifications. 
Mutable (those without `const` in front of them) are generally inpsected and 
modified by material shaders to achieve some effect for that material, and then 
consumed by pipeline shaders for shadind and fragment output.
