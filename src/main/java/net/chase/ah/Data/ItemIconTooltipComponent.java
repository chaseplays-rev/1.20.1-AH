package net.chase.ah.Data;

import net.minecraft.world.inventory.tooltip.TooltipComponent;
import net.minecraft.world.item.ItemStack;

public class ItemIconTooltipComponent implements TooltipComponent {
    private final ItemStack stack;

    public ItemIconTooltipComponent(ItemStack stack) {
        this.stack = stack;
    }

    public ItemStack getStack() {
        return this.stack;
    }
}