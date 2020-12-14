package me.drex.varo.commands;

import com.mojang.authlib.GameProfile;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import me.drex.varo.session.SessionEntry;
import me.drex.varo.session.SessionManager;
import me.drex.varo.util.CommandUtil;
import me.drex.varo.util.TimeDifferenceUtil;
import net.minecraft.command.argument.GameProfileArgumentType;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.HoverEvent;
import net.minecraft.text.LiteralText;
import net.minecraft.text.MutableText;
import net.minecraft.util.Formatting;

import java.text.SimpleDateFormat;
import java.util.Date;

public class TimePlayedCommand {

    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        RequiredArgumentBuilder<ServerCommandSource, GameProfileArgumentType.GameProfileArgument> player = RequiredArgumentBuilder.argument("player", GameProfileArgumentType.gameProfile());
        player.requires(src -> src.hasPermissionLevel(2));
        player.executes(TimePlayedCommand::executeOther);
        LiteralArgumentBuilder<ServerCommandSource> playtime = LiteralArgumentBuilder.literal("playtime");
        playtime.then(player);
        LiteralArgumentBuilder<ServerCommandSource> pt = LiteralArgumentBuilder.literal("pt");
        pt.then(player);
        playtime.executes(TimePlayedCommand::execute);
        pt.executes(TimePlayedCommand::execute);
        dispatcher.register(playtime);
        dispatcher.register(pt);
    }

    public static int execute(CommandContext<ServerCommandSource> ctx) throws CommandSyntaxException {
        MutableText hover = new LiteralText("");
        long pt = 0;
        for (SessionEntry entry : SessionManager.getEntries(ctx.getSource().getPlayer().getUuid())) {
            pt += entry.getPlaytime();
            hover.append(new LiteralText(new SimpleDateFormat("dd MMM yy HH:mm:ss").format(new Date(entry.start)) + " - " + new SimpleDateFormat("HH:mm:ss").format(entry.end.map(Date::new).orElseGet(Date::new)) + ": ").formatted(Formatting.YELLOW))
                    .append(new LiteralText(TimeDifferenceUtil.formatDiff(entry.getPlaytime()) + "\n").formatted(Formatting.WHITE));
        }
        MutableText text = new LiteralText("You have played ").formatted(Formatting.WHITE)
                .append(new LiteralText(TimeDifferenceUtil.formatDiff(pt)).formatted(Formatting.GRAY)).styled(style -> style.withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, hover)))
                .append(new LiteralText(".").formatted(Formatting.WHITE));
        ctx.getSource().getPlayer().sendMessage(text, false);
        return (int) pt;
    }

    public static int executeOther(CommandContext<ServerCommandSource> ctx) throws CommandSyntaxException {
        GameProfile profile = CommandUtil.getProfile(ctx, "player");
        MutableText hover = new LiteralText("");
        long pt = 0;
        for (SessionEntry entry : SessionManager.getEntries(profile.getId())) {
            pt += entry.getPlaytime();
            hover.append(new LiteralText(new SimpleDateFormat("dd MMM yy HH:mm:ss").format(new Date(entry.start)) + " - " + new SimpleDateFormat("HH:mm:ss").format(entry.end.map(Date::new).orElseGet(Date::new)) + ": ").formatted(Formatting.YELLOW))
                    .append(new LiteralText(TimeDifferenceUtil.formatDiff(entry.getPlaytime()) + "\n").formatted(Formatting.WHITE));
        }
        MutableText text = new LiteralText(profile.getName() + " has played ").formatted(Formatting.WHITE)
                .append(new LiteralText(TimeDifferenceUtil.formatDiff(pt)).formatted(Formatting.GRAY)).styled(style -> style.withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, hover)))
                .append(new LiteralText(".").formatted(Formatting.WHITE));
        ctx.getSource().getPlayer().sendMessage(text, false);
        return (int) pt;
    }

}
