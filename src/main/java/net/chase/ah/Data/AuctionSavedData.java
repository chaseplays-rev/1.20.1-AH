package net.chase.ah.Data;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.saveddata.SavedData;

import java.util.ArrayList;
import java.util.List;

public class AuctionSavedData extends SavedData {
    public static final String DATA_NAME = "auctionhouse:ah";

    private final List<ItemStack> listings = new ArrayList<>();

    public synchronized void addListing(ItemStack stack) {
        listings.add(stack.copy());
        setDirty(); // mark for save
    }

    public synchronized List<ItemStack> getListings() {
        return new ArrayList<>(listings);
    }

    @Override
    public CompoundTag save(CompoundTag tag) {
        ListTag list = new ListTag();
        for (ItemStack s : listings) {
            CompoundTag e = new CompoundTag();
            s.save(e);
            list.add(e);
        }
        tag.put("listings", list);
        return tag;
    }

    public static AuctionSavedData load(CompoundTag tag) {
        AuctionSavedData data = new AuctionSavedData();
        ListTag list = tag.getList("listings", Tag.TAG_COMPOUND);
        for (int i = 0; i < list.size(); i++) {
            CompoundTag e = list.getCompound(i);
            ItemStack s = ItemStack.of(e);
            if (!s.isEmpty()) data.listings.add(s);
        }
        return data;
    }

    // ----- Accessor -----
    public static AuctionSavedData get(ServerLevel level) {
        return level.getDataStorage().computeIfAbsent(AuctionSavedData::load, AuctionSavedData::new, DATA_NAME);
    }
    public static AuctionSavedData get(MinecraftServer server) {
        return get(server.overworld());
    }
}