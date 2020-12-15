package me.drex.varo.commands;

import com.mojang.authlib.GameProfile;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import me.drex.varo.session.SessionManager;
import me.drex.varo.util.CommandUtil;
import me.drex.varo.util.TimeDifferenceUtil;
import net.minecraft.command.argument.GameProfileArgumentType;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.HoverEvent;
import net.minecraft.text.LiteralText;
import net.minecraft.text.MutableText;
import net.minecraft.util.Formatting;

import java.util.UUID;

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
        UUID uuid = ctx.getSource().getPlayer().getUuid();
        MutableText hover = new LiteralText("Gestern: " + TimeDifferenceUtil.formatDiff(SessionManager.yesterday(uuid))).append(new LiteralText("\nHeute: " + TimeDifferenceUtil.formatDiff(SessionManager.today(uuid)))).formatted(Formatting.RED, Formatting.ITALIC);
        MutableText text = new LiteralText("Du hast ").formatted(Formatting.WHITE)
                .append(new LiteralText(TimeDifferenceUtil.formatDiff(SessionManager.getTimeLeft(uuid))).formatted(Formatting.GRAY)).styled(style -> style.withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, hover)))
                .append(new LiteralText(" übrig.").formatted(Formatting.WHITE));
        ctx.getSource().getPlayer().sendMessage(text, false);
        return 1;
    }

    public static int executeOther(CommandContext<ServerCommandSource> ctx) throws CommandSyntaxException {
        GameProfile profile = CommandUtil.getProfile(ctx, "player");
        UUID uuid = profile.getId();
        MutableText hover = new LiteralText("Gestern: " + TimeDifferenceUtil.formatDiff(SessionManager.yesterday(uuid))).append(new LiteralText("\nHeute: " + TimeDifferenceUtil.formatDiff(SessionManager.today(uuid)))).formatted(Formatting.RED, Formatting.ITALIC);
        MutableText text = new LiteralText(profile.getName() + " hat ").formatted(Formatting.WHITE)
                    .append(new LiteralText(TimeDifferenceUtil.formatDiff(SessionManager.getTimeLeft(profile.getId()))).formatted(Formatting.GRAY)).styled(style -> style.withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, hover)))
                    .append(new LiteralText(" übrig.").formatted(Formatting.WHITE));
        ctx.getSource().getPlayer().sendMessage(text, false);
        return 1;
    }

}
