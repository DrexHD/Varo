package me.drex.varo.commands;

import com.mojang.authlib.GameProfile;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import me.drex.varo.session.SessionManager;
import me.drex.varo.util.CommandUtil;
import me.drex.varo.util.TimeDifferenceUtil;
import net.minecraft.command.argument.GameProfileArgumentType;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.LiteralText;
import net.minecraft.text.MutableText;
import net.minecraft.util.Formatting;

public class TimeLeftCommand {

    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        RequiredArgumentBuilder<ServerCommandSource, GameProfileArgumentType.GameProfileArgument> player = RequiredArgumentBuilder.argument("player", GameProfileArgumentType.gameProfile());
        player.requires(src -> src.hasPermissionLevel(2));
        player.executes(TimeLeftCommand::executeOther);
        LiteralArgumentBuilder<ServerCommandSource> timeleft = LiteralArgumentBuilder.literal("timeleft");
        timeleft.then(player);
        timeleft.executes(TimeLeftCommand::execute);
        dispatcher.register(timeleft);
    }

    public static int execute(CommandContext<ServerCommandSource> ctx) throws CommandSyntaxException {
        MutableText text = new LiteralText("You have ").formatted(Formatting.WHITE)
                .append(new LiteralText(TimeDifferenceUtil.formatDiff(SessionManager.getTimeLeft(ctx.getSource().getPlayer().getUuid()))).formatted(Formatting.GRAY))
                .append(new LiteralText(" left.").formatted(Formatting.WHITE));
        ctx.getSource().getPlayer().sendMessage(text, false);
        return 1;
    }

    public static int executeOther(CommandContext<ServerCommandSource> ctx) throws CommandSyntaxException {
        GameProfile profile = CommandUtil.getProfile(ctx, "player");
        MutableText text = new LiteralText(profile.getName() + " has ").formatted(Formatting.WHITE)
                    .append(new LiteralText(TimeDifferenceUtil.formatDiff(SessionManager.getTimeLeft(profile.getId()))).formatted(Formatting.GRAY))
                    .append(new LiteralText(" left.").formatted(Formatting.WHITE));
        ctx.getSource().getPlayer().sendMessage(text, false);
        return 1;
    }

}
