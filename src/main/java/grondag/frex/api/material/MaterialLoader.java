/*******************************************************************************
 * Copyright 2019 grondag
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License.  You may obtain a copy
 * of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the
 * License for the specific language governing permissions and limitations under
 * the License.
 ******************************************************************************/

package grondag.frex.api.material;

import org.apiguardian.api.API;

import grondag.frex.impl.material.MaterialLoaderImpl;
import net.fabricmc.fabric.api.renderer.v1.material.RenderMaterial;
import net.minecraft.util.Identifier;

/**
 * For use by model loading libraries - handles deserialization of material JSON
 * files, creating and registering them in the renderer.  Note that resource reload
 * events do not cause materials to be reloaded, but shader source will be refreshed
 * if the renderer supports that feature.<p>
 *
 * Loaded materials will be Fabric API materials if no FREX compliant renderer is present
 * and the materials will (obviously) not have shaders in that case.<p>
 * 
 * Renderer Authors: This interface is implemented by FREX - you do not need to implement it.
 */
@API(status = API.Status.EXPERIMENTAL)
public interface MaterialLoader {
    /**
     * Material files should be in assets/<mod-id>/materials and have a .json suffix.<p>
     * 
     * @param id domain and path of material json. See notes above.
     * @return  Loaded material if successful, null if file not found or specified features are unsupported.
     */
    public static RenderMaterial loadMaterial(Identifier id) {
        return MaterialLoaderImpl.loadMaterial(id);
    }

}
