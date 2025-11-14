package net.chase.ah.Events;

import com.mojang.datafixers.util.Either;
import net.chase.ah.Data.PriceTooltipComponent;
import net.chase.ah.Main;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraftforge.client.event.RenderTooltipEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = Main.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class MainEvents {
    @SubscribeEvent
    public static void onGatherTooltip(RenderTooltipEvent.GatherComponents event) {
        ItemStack stack = event.getItemStack();
        CompoundTag tag = stack.getOrCreateTag();
        if (!tag.contains("AH_COUNT")) return;
        int count = tag.getInt("AH_COUNT");
        event.getTooltipElements().add(
                com.mojang.datafixers.util.Either.right(
                        new PriceTooltipComponent(new ItemStack(Items.DIRT), count)
                )
        );
    }
}