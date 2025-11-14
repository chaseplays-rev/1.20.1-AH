package net.chase.ah.Data;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.client.event.RenderTooltipEvent;

public class AuctionHouse {
    public static class Listing{
        ItemStack Item;
        ItemStack Price;
        int Cost;
        public Listing(ItemStack item, ItemStack price, int cost){
            this.Item = item; this.Price = price; this.Cost = cost;
        }
    }
    public static void addListing(ServerPlayer p, Listing listing){

    }
}
