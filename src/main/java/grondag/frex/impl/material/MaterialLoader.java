package grondag.frex.impl.material;

import java.util.Collection;

import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.ModContainer;
import net.minecraft.client.MinecraftClient;
import net.minecraft.resource.ResourceManager;

public final class MaterialLoader {
    private MaterialLoader() {}
    
    public static void loadMaterials() {
        Collection<ModContainer> mods = FabricLoader.getInstance().getAllMods();
        ResourceManager rm = MinecraftClient.getInstance().getResourceManager();
        for(ModContainer mod : mods) {
            System.out.println(mod.getRootPath().toString());
        }
    }
}