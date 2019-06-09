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

package grondag.frex.impl.material;

import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

import grondag.frex.Frex;
import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;
import net.fabricmc.fabric.api.renderer.v1.Renderer;
import net.fabricmc.fabric.api.renderer.v1.RendererAccess;
import net.fabricmc.fabric.api.renderer.v1.material.RenderMaterial;
import net.minecraft.client.MinecraftClient;
import net.minecraft.resource.Resource;
import net.minecraft.resource.ResourceManager;
import net.minecraft.util.Identifier;

public final class MaterialLoaderImpl {
private MaterialLoaderImpl() {}
    private static final ObjectOpenHashSet<Identifier> CAUGHT = new ObjectOpenHashSet<>();
    
    public static synchronized RenderMaterial loadMaterial(Identifier idIn) {
        Renderer r = RendererAccess.INSTANCE.getRenderer();
        RenderMaterial result = r.materialById(idIn);
        if(result == null) {
            result = loadMaterialInner(idIn);
            r.registerMaterial(idIn, result);
        }
        return result;
    }
    
    private static RenderMaterial loadMaterialInner(Identifier idIn) {
        Identifier id = new Identifier(idIn.getNamespace(), "materials/" + idIn.getPath() + ".json");
        
        RenderMaterial result = null;
        ResourceManager rm = MinecraftClient.getInstance().getResourceManager();
        try(Resource res = rm.getResource(id)) {
            result = MaterialDeserializer.deserialize(new InputStreamReader(res.getInputStream(), StandardCharsets.UTF_8));
        } catch (Exception e) {
            if(CAUGHT.add(idIn)) {
                Frex.LOG.info("Unable to load render material " + idIn.toString() + " due to exception " + e.toString()); 
            }
        }
        
        return result;
    }
}