package net.maven.malady.currency.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.maven.malady.currency.api.CurrencyAPI;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class CurrencyCommands {

    // Simple storage - you can replace this with your own implementation
    private static final Map<UUID, Integer> playerBalances = new HashMap<>();

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(
                Commands.literal("currency")
                        .requires(source -> source.hasPermission(2))

                        .then(Commands.argument("player", EntityArgument.player())
                                .then(Commands.literal("add")
                                        .then(Commands.argument("amount", IntegerArgumentType.integer(0))
                                                .executes(context -> executeAdd(context,
                                                        EntityArgument.getPlayer(context, "player"),
                                                        IntegerArgumentType.getInteger(context, "amount")))
                                        )
                                )
                                .then(Commands.literal("remove")
                                        .then(Commands.argument("amount", IntegerArgumentType.integer(0))
                                                .executes(context -> executeRemove(context,
                                                        EntityArgument.getPlayer(context, "player"),
                                                        IntegerArgumentType.getInteger(context, "amount")))
                                        )
                                )
                                .then(Commands.literal("set")
                                        .then(Commands.argument("amount", IntegerArgumentType.integer(0))
                                                .executes(context -> executeSet(context,
                                                        EntityArgument.getPlayer(context, "player"),
                                                        IntegerArgumentType.getInteger(context, "amount")))
                                        )
                                )
                        )
        );
    }


    private static int executeAdd(CommandContext<CommandSourceStack> context, ServerPlayer target, int amount) {
        CurrencyAPI.addCurrency(target, amount);
        return 1;
    }


    private static int executeRemove(CommandContext<CommandSourceStack> context, ServerPlayer target, int amount) {
        CurrencyAPI.removeCurrency(target, amount);
        return 1;
    }

    private static int executeSet(CommandContext<CommandSourceStack> context, ServerPlayer target, int amount) {
        CurrencyAPI.setCurrency(target, amount);
        return 1;
    }

}
