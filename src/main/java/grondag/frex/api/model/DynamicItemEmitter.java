package grondag.frex.api.model;

import org.apiguardian.api.API;

import grondag.frex.api.render.RenderContext;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;

@API(status = API.Status.EXPERIMENTAL)
@FunctionalInterface
public interface DynamicItemEmitter {
    public void emit(ItemStack stack, PlayerEntity player, RenderContext context);
}
