package net.chase.ah.Data;

import net.minecraft.client.gui.Font;
import net.minecraft.world.inventory.tooltip.TooltipComponent;
import net.minecraft.world.item.ItemStack;

public class PriceClientTooltipComponent implements TooltipComponent {

    private final ItemStack currency;
    private final int amount;

    public PriceClientTooltipComponent(PriceTooltipComponent data) {
        this.currency = data.currency();
        this.amount = data.amount();
    }

}