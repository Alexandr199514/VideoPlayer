package com.github.NGoedix.watchvideo.commands;

import com.github.NGoedix.watchvideo.network.PacketHandler;
import com.github.NGoedix.watchvideo.network.message.SendVideoMessage;
import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;

import java.util.Collection;

public class PlayVideoCommand {
    public static void register(CommandDispatcher<CommandSourceStack> dispatcher){
        dispatcher.register(Commands.literal("playvideo")
                .then(Commands.argument("target", EntityArgument.players())
                .then(Commands.argument("url", StringArgumentType.greedyString())
                .executes(PlayVideoCommand::execute))));
    }

    private static int execute(CommandContext<CommandSourceStack> command){
        Collection<ServerPlayer> players;
        try {
            players = EntityArgument.getPlayers(command, "target");
        } catch (CommandSyntaxException e) {
            command.getSource().sendFailure(Component.literal("Error with target parameter."));
            return Command.SINGLE_SUCCESS;
        }
        for (ServerPlayer player : players) {
            PacketHandler.sendTo(new SendVideoMessage(StringArgumentType.getString(command, "url")), player);
        }return Command.SINGLE_SUCCESS;
    }
}
