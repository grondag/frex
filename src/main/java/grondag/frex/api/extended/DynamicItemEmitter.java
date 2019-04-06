package grondag.frex.api.extended;

import grondag.frex.api.core.RenderContext;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;

@FunctionalInterface
public interface DynamicItemEmitter {
    public void emit(ItemStack stack, PlayerEntity player, RenderContext context);
}
