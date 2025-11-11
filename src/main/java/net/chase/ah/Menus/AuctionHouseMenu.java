package net.chase.ah.Menus;

import net.chase.ah.Data.AuctionSavedData;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ClickType;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

import java.util.List;
import java.util.function.BiConsumer;

public class AuctionHouseMenu extends AbstractContainerMenu {
    private static final int ROWS = 6; // 9 x 6
    private final SimpleContainer backing = new SimpleContainer(ROWS * 9);

    public AuctionHouseMenu(int id, Inventory ignored) {
        super(MenuType.GENERIC_9x6, id);

        // Only our 54 UI slots (no player inv)
        for (int r = 0; r < ROWS; r++) {
            for (int c = 0; c < 9; c++) {
                int idx = c + r * 9;
                this.addSlot(new NonInteractiveSlot(backing, idx, 8 + c * 18, 18 + r * 18));
            }
        }
        // Fill once; contents are set when opened (we don’t hold a Server ref here)
        fillBackground();
    }

    /** Open and populate from SavedData on the server thread. */
    public static void openMenu(ServerPlayer player) {
        // snapshot listings now (server thread)
        List<ItemStack> snapshot = AuctionSavedData.get(player.server).getListings();

        player.openMenu(new SimpleMenuProvider(
                (id, inv, p) -> {
                    AuctionHouseMenu m = new AuctionHouseMenu(id, inv);
                    m.drawListings(snapshot);
                    return m;
                },
                Component.literal("Auction House")
        ));
    }

    private void fillBackground() {
        ItemStack filler = named(Items.GRAY_STAINED_GLASS_PANE, " ");
        for (int i = 0; i < backing.getContainerSize(); i++) backing.setItem(i, filler.copy());
        backing.setItem(4, named(Items.NAME_TAG, "Browse Listings"));
        broadcastChanges();
    }

    /** Place listings starting at slot index 10 (0-based), then 11, 12, ... */
    private void drawListings(List<ItemStack> listings) {
        // Ensure background exists
        fillBackground();

        int slot = 10;                       // start position as requested
        final int max = backing.getContainerSize(); // 54
        for (ItemStack s : listings) {
            if (slot >= max) break;
            backing.setItem(slot, s.copy());
            slot++;
        }
        broadcastChanges();
    }

    private static ItemStack named(Item item, String name) {
        ItemStack s = new ItemStack(item);
        s.setHoverName(Component.literal(name));
        return s;
    }

    // ===== Protections =====
    @Override public ItemStack quickMoveStack(Player player, int index) { return ItemStack.EMPTY; }
    @Override public boolean canDragTo(Slot slot) { return false; }
    @Override public boolean canTakeItemForPickAll(ItemStack stack, Slot slot) { return false; }
    @Override protected boolean moveItemStackTo(ItemStack stack, int start, int end, boolean reverse) { return false; }
    @Override public boolean stillValid(Player player) { return true; }

    @Override
    public void clicked(int slotId, int button, ClickType clickType, Player player) {
        if(clickType != ClickType.PICKUP)
        {
            if (!getCarried().isEmpty()) {
                setRemoteCarried(ItemStack.EMPTY);
                setCarried(ItemStack.EMPTY); broadcastChanges();
            }
        }
    }

    /** Non-interactive slot. */
    static class NonInteractiveSlot extends Slot {
        public NonInteractiveSlot(Container container, int index, int x, int y) { super(container, index, x, y); }
        @Override public boolean mayPlace(ItemStack stack) { return false; }
        @Override public boolean mayPickup(Player player) { return false; }
    }
}
