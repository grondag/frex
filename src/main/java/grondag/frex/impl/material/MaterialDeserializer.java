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

import java.io.Reader;
import java.util.Locale;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import grondag.frex.Frex;
import grondag.frex.api.Renderer;
import grondag.frex.api.material.MaterialFinder;
import grondag.frex.api.material.ShaderBuilder;
import net.fabricmc.fabric.api.renderer.v1.RendererAccess;
import net.fabricmc.fabric.api.renderer.v1.material.RenderMaterial;
import net.minecraft.block.BlockRenderLayer;
import net.minecraft.util.Identifier;
import net.minecraft.util.JsonHelper;

public class MaterialDeserializer {
    private MaterialDeserializer() {}
    
    public static RenderMaterial deserialize(Reader reader) {
        net.fabricmc.fabric.api.renderer.v1.material.MaterialFinder finder = RendererAccess.INSTANCE.getRenderer().materialFinder();
        JsonObject json = JsonHelper.deserialize(reader);

        if(Frex.isAvailable()) {
            if(json.has("spriteDepth")) {
                finder.spriteDepth(JsonHelper.getInt(json, "spriteDepth", 1));
            }
            
            if(json.has("fragmentSource") && json.has("vertexSource")) {
                ShaderBuilder sb = Renderer.get().shaderBuilder();
                sb.fragmentSource(new Identifier(JsonHelper.getString(json, "fragmentSource")));
                sb.vertexSource(new Identifier(JsonHelper.getString(json, "vertexSource")));
                ((MaterialFinder)finder).shader(sb.build());
            }
        }
        
        if(json.has("layers")) {
            JsonArray layers = JsonHelper.asArray(json.get("layers"), "layers");
            layers.forEach(e -> readLayer(e.getAsJsonObject(), finder));
        }
        
        return finder.find();
    }
    
    private static void readLayer(JsonObject layer, net.fabricmc.fabric.api.renderer.v1.material.MaterialFinder finder) {
        if(layer.has("spriteIndex")) {
            int spriteIndex = JsonHelper.getInt(layer, "spriteIndex", 0);
            
            if(spriteIndex > 1 && !Frex.isAvailable()) return;
            
            if(layer.has("disableAo")) {
                finder.disableAo(spriteIndex, JsonHelper.getBoolean(layer, "disableAo", true));
            }
            
            if(layer.has("disableColorIndex")) {
                finder.disableColorIndex(spriteIndex, JsonHelper.getBoolean(layer, "disableColorIndex", true));
            }
            
            if(layer.has("disableDiffuse")) {
                finder.disableDiffuse(spriteIndex, JsonHelper.getBoolean(layer, "disableDiffuse", true));
            }
            
            if(layer.has("emissive")) {
                finder.emissive(spriteIndex, JsonHelper.getBoolean(layer, "emissive", true));
            }
            
            if(layer.has("blendMode")) {
                finder.blendMode(spriteIndex, readBlendMode(JsonHelper.getString(layer, "blendMode")));
            }
        }
    }
    
    private static BlockRenderLayer readBlendMode(String val) {
        val = val.toLowerCase(Locale.ROOT);
        switch(val) {
        case "solid":
        default:
            return BlockRenderLayer.SOLID;
        case "cutout":
            return BlockRenderLayer.CUTOUT;
        case "cutout_mipped":
            return BlockRenderLayer.CUTOUT_MIPPED;
        case "translucent":
            return BlockRenderLayer.TRANSLUCENT;
        }
    }
}
