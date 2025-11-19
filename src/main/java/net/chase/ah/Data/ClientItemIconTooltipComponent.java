package net.chase.ah.Data;

import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.world.item.ItemStack;

public class ClientItemIconTooltipComponent implements ClientTooltipComponent {

    private final ItemIconTooltipComponent component;

    public ClientItemIconTooltipComponent(ItemIconTooltipComponent component) {
        this.component = component;
    }

    @Override
    public int getHeight() {
        return 18;
    }

    @Override
    public int getWidth(Font font) {
        return 18;
    }

    @Override
    public void renderImage(Font font, int x, int y, GuiGraphics gfx) {
        ItemStack stack = component.getStack();
        gfx.renderItem(stack, x, y);
        gfx.renderItemDecorations(font, stack, x, y);
    }
}