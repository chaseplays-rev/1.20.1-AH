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
    private static final Path PATH = Paths.get("world", "data", "auction_house.dat");
    private static final String ITEMS_TAG = Main.MODID + ":items_list_tag";
    private static CompoundTag tag;

    public static ListTag getItemsTag(){
        if(tag.contains(ITEMS_TAG))
            return tag.getList(ITEMS_TAG, Tag.TAG_LIST);
        else {
            ListTag listTag = new ListTag();
            tag.put(ITEMS_TAG, listTag);
            return tag.getList(ITEMS_TAG, Tag.TAG_LIST);
        }
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
                    Main.LOGGER.debug("Caught null itemTag");
                    continue;
                }
                ItemStack stack = ItemStack.of((CompoundTag) itemTag);
                if(stack.getItem().equals(Items.AIR)) {
                    Main.LOGGER.debug("Caught some null/air entries in items list");
                    continue;
                }
                list.add(ItemStack.of((CompoundTag) iter.next()));
            }
        } catch(Exception e){
            Main.LOGGER.debug("getItemList caught exception! -> " + e.getCause());
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
                tag = NbtIo.readCompressed(PATH.toFile());
            } else {
                save();
                tag = NbtIo.readCompressed(PATH.toFile());
            }
        } catch (IOException e) {
            Main.LOGGER.debug("Caught exception in io load! -> " + e.getCause());
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
            Main.LOGGER.debug("Caught exception in io save! -> " + e.getCause());
            e.printStackTrace();
        }
    }

}
