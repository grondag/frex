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

package grondag.frex;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apiguardian.api.API;

import net.fabricmc.fabric.api.renderer.v1.Renderer;
import net.fabricmc.fabric.api.renderer.v1.RendererAccess;

public class Frex {
    public static Logger LOG = LogManager.getLogger("FREX");
    
    private static int checkVal = -1;
    
    @API(status = API.Status.STABLE)
    public static boolean isAvailable() {
        if(checkVal == -1) {
            Renderer renderer = RendererAccess.INSTANCE.getRenderer();
            checkVal = renderer != null && renderer instanceof grondag.frex.api.Renderer ? 1 : 0;
        }
        return checkVal == 1;
    }
}
