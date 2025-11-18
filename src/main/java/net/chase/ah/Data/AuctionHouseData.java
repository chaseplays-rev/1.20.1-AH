package net.chase.ah.Data;

import net.chase.ah.Main;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.NbtIo;
import net.minecraft.nbt.Tag;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

import java.awt.*;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class AuctionHouseData {
    private static final Path PATH = Paths.get("config", "auctionhouse", "auction_house.dat");
    private static final String ITEMS_TAG = Main.MODID + ":items_list_tag";
    private static CompoundTag tag;

    public static ListTag getItemsTag() {
        if (!tag.contains(ITEMS_TAG)) {
            Main.LOGGER.info("AuctionHouse: Made Tag List");
            ListTag listTag = new ListTag();
            tag.put(ITEMS_TAG, listTag);
        }
        return (ListTag) tag.get(ITEMS_TAG);
    }

    public static List<ItemStack> getItemList(){
        List<ItemStack> list = new ArrayList<>();
        ListTag listTag = getItemsTag();
        if(listTag.isEmpty()) return list;
        Iterator<Tag> iter = listTag.iterator();
        try {
            while(iter.hasNext()){
                Tag itemTag = iter.next();
                if(itemTag == null){
                    Main.LOGGER.info("Caught null itemTag");
                    continue;
                }
                ItemStack stack = ItemStack.of((CompoundTag) itemTag);
                if(stack.getItem().equals(Items.AIR)) {
                    Main.LOGGER.info("Caught some null/air entries in items list");
                    continue;
                }
                list.add(ItemStack.of((CompoundTag) itemTag));
            }
        } catch(Exception e){
            Main.LOGGER.info("getItemList caught exception! -> {}", String.valueOf(e.getCause()));
        }
        return list;
    }

    public static void append(ItemStack pStack){
        CompoundTag pTag = new CompoundTag();
        pStack.save(pTag);
        getItemsTag().add(pTag);
        save();
    }

    public static void load() {
        try {
            if (Files.exists(PATH)) {
                Main.LOGGER.info("AuctionHouse: Properly Read Tag");
                tag = NbtIo.readCompressed(PATH.toFile());
            } else {
                Main.LOGGER.info("AuctionHouse: Failed To Read Tag");
                save();
                tag = NbtIo.readCompressed(PATH.toFile());
            }
        } catch (IOException e) {
            Main.LOGGER.info("Caught exception in io load! -> {}", String.valueOf(e.getCause()));
            e.printStackTrace();
        }
    }

    public static void save() {
        try {
            Files.createDirectories(PATH.getParent());
            if(tag == null)
                NbtIo.writeCompressed(new CompoundTag(), PATH.toFile());
            else NbtIo.writeCompressed(tag, PATH.toFile());
        } catch (IOException e) {
            Main.LOGGER.info("Caught exception in io save! -> {}", String.valueOf(e.getCause()));
            e.printStackTrace();
        }
    }

}
