package net.chase.ah.Commands;

import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.item.ItemArgument;
import net.minecraft.commands.arguments.item.ItemInput;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.OutgoingChatMessage;
import net.minecraft.network.chat.PlayerChatMessage;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.RegisterCommandsEvent;

public final class AhCommand {

    public static void register(RegisterCommandsEvent e) {
        e.getDispatcher().register(build(e.getBuildContext()));
    }

    private static LiteralArgumentBuilder<CommandSourceStack> build(CommandBuildContext ctx) {
        return Commands.literal("ah")
                .then(Commands.literal("sell")
                        .then(Commands.argument("item", ItemArgument.item(ctx))           // autocompletes every item id
                                .then(Commands.argument("cost", IntegerArgumentType.integer(1, 2304)) // cap 1..2304
                                        .executes(AhCommand::handleSell)
                                )
                        )
                );
    }

    private static int handleSell(CommandContext<CommandSourceStack> ctx) {
        ServerPlayer player = ctx.getSource().getPlayer();
        ItemInput wantedInput = ItemArgument.getItem(ctx, "item");
        Item wantedItem = wantedInput.getItem();
        int count = IntegerArgumentType.getInteger(ctx, "cost");

        ItemStack stack = new ItemStack(wantedItem, count);
        player.displayClientMessage(stack.getHighlightTip(stack.getDisplayName()), false);
        return 1;
    }
}