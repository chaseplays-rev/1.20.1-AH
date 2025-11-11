package net.chase.ah.Commands;

import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import net.chase.ah.Data.AuctionSavedData;
import net.chase.ah.Menus.AuctionHouseMenu;
import net.minecraft.ChatFormatting;
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.item.ItemArgument;
import net.minecraft.commands.arguments.item.ItemInput;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.RegisterCommandsEvent;

public final class AhCommand {

    public static void register(RegisterCommandsEvent e) {
        e.getDispatcher().register(sell(e.getBuildContext()));
        e.getDispatcher().register(open(e.getBuildContext()));
    }

    private static LiteralArgumentBuilder<CommandSourceStack> sell(CommandBuildContext ctx) {
        return Commands.literal("ah")
                .then(Commands.literal("sell")
                        .then(Commands.argument("item", ItemArgument.item(ctx))           // autocompletes every item id
                                .then(Commands.argument("cost", IntegerArgumentType.integer(1, 2304)) // cap 1..2304
                                        .executes(AhCommand::handleSell)
                                )
                        )
                );
    }

    private static LiteralArgumentBuilder<CommandSourceStack> open(CommandBuildContext ctx) {
        return Commands.literal("ah").executes(AhCommand::openAh);
    }

    private static int openAh(CommandContext<CommandSourceStack> ctx){
        ServerPlayer player = ctx.getSource().getPlayer();
        AuctionHouseMenu.openMenu(player);
        return 1;
    }

    private static int handleSell(CommandContext<CommandSourceStack> ctx) {
        ServerPlayer player = ctx.getSource().getPlayer();
        ItemInput wantedInput = ItemArgument.getItem(ctx, "item");
        Item wantedItem = wantedInput.getItem();
        int count = IntegerArgumentType.getInteger(ctx, "cost");

        ItemStack stack = player.getMainHandItem();
        AuctionSavedData data = AuctionSavedData.get(player.server);
        data.addListing(stack);
        player.displayClientMessage(Component.literal(ChatFormatting.GREEN + "Item was added to auction house"), true);
        sendItemLink(player, stack, ChatFormatting.AQUA);
        return 1;
    }
    public static void sendItemLink(ServerPlayer to, ItemStack stack, ChatFormatting color) {
        // Visible label: use the stack’s hover name so it respects anvil renames & italics
        MutableComponent label = ComponentUtils.wrapInSquareBrackets(stack.getHoverName().copy());

        // Hover: full item tooltip (enchants, lore, attributes, etc.)
        HoverEvent hover = new HoverEvent(
                HoverEvent.Action.SHOW_ITEM,
                new HoverEvent.ItemStackInfo(stack) // includes id, count, full NBT
        );

        // Optional: make it clickable (e.g., suggest a command)
        ClickEvent click = new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/ah sell ");

        MutableComponent msg = label.withStyle(s -> s
                .withColor(color)
                .withHoverEvent(hover)
                .withClickEvent(click)
        );

        to.sendSystemMessage(msg); // or broadcast via player.server.getPlayerList().broadcastSystemMessage(msg, false)
    }
}