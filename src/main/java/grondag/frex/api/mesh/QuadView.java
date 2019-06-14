package grondag.frex.api.mesh;

import org.apiguardian.api.API;

import net.minecraft.client.texture.Sprite;

@API(status = API.Status.EXPERIMENTAL)
public interface QuadView extends net.fabricmc.fabric.api.renderer.v1.mesh.QuadView {
    /** Retrieve the sprite associated with the given sprite layer. */
    Sprite sprite(int spriteIndex);
}
