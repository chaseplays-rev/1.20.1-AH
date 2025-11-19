package net.chase.ah.Data;

import com.mojang.datafixers.util.Either;
import net.chase.ah.Main;
import net.minecraft.client.gui.Font;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.inventory.tooltip.TooltipComponent;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.client.event.RenderTooltipEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.Objects;

@Mod.EventBusSubscriber(modid = Main.MODID)
public class AuctionHouse {
    public static final String SELL_PRICE = Main.MODID + ":sell_price";
    public static final String SELL_ITEM = Main.MODID + ":sell_item";
    public static class Listing{
        ItemStack Item;
        ItemStack Price;
        int Cost;
        public Listing(ItemStack item, ItemStack price, int cost){
            this.Item = item; this.Price = price; this.Cost = cost;
            CompoundTag tag = this.Item.getOrCreateTag();
            tag.putInt(SELL_PRICE, this.Cost);
            tag.putString(SELL_ITEM, Objects.requireNonNull(ForgeRegistries.ITEMS.getKey(Price.getItem())).toString());
        }
    }
    public static void addListing(ServerPlayer p, Listing listing){
        AuctionHouseData.append(listing.Item);
        p.displayClientMessage(Component.literal(listing.Item.getOrCreateTag().getString(SELL_ITEM)), false);
    }
    public static boolean isSameItem(String resourceLoc, ItemStack oItem){
        return Objects.requireNonNull(ForgeRegistries.ITEMS.getKey(oItem.getItem())).toString().equals(resourceLoc);
    }
    public static boolean isSameItem(ItemStack mItem, ItemStack oItem){
        return Objects.equals(ForgeRegistries.ITEMS.getKey(mItem.getItem()), ForgeRegistries.ITEMS.getKey(oItem.getItem()));
    }
    public static ItemStack fromString(String resourceLoc){
        Item item = ForgeRegistries.ITEMS.getValue(new ResourceLocation(resourceLoc));
        assert item != null;
        return new ItemStack(item);
    }
    @SubscribeEvent
    public static void onTooltip(RenderTooltipEvent.GatherComponents event) {
        ItemStack hovered = event.getItemStack();
        CompoundTag tag = hovered.getOrCreateTag();
        if(!tag.contains(SELL_ITEM)) return;
        String id = tag.getString(SELL_ITEM);
        ItemStack iconStack = fromString(id);

        event.getTooltipElements().add(
                Either.right(new ItemIconTooltipComponent(iconStack))
        );
    }
}
