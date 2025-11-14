package net.chase.ah.Data;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.NbtIo;
import net.minecraft.world.item.ItemStack;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

public class AuctionHouseData {
    private static final Path PATH = Paths.get("world", "data", "auction_house.dat");
    private static CompoundTag tag;
    private static ListTag itemsTag;

    public static CompoundTag getRootTag(){
        return tag;
    }

    public static ListTag getItemsTag(){
        return itemsTag;
    }

    public static void append(ItemStack pStack){
        CompoundTag pTag = new CompoundTag();
        pStack.save(pTag);
        itemsTag.add(pTag);
    }

    public static void load() {
        try {
            if (Files.exists(PATH)) {
                tag = NbtIo.readCompressed(PATH.toFile());
                return;
            } else {
                save();
            }
            return;
        } catch (IOException e) {
            e.printStackTrace();
        }
        tag = new CompoundTag();
    }

    public static void save() {
        try {
            Files.createDirectories(PATH.getParent());
            NbtIo.writeCompressed(tag, PATH.toFile());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
