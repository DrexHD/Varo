package me.drex.varo.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import me.drex.varo.session.SessionManager;
import net.minecraft.server.command.ServerCommandSource;

public class ClearSessionsCommand {

    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        LiteralArgumentBuilder<ServerCommandSource> clearsession = LiteralArgumentBuilder.literal("clearsession");
        clearsession.requires(src -> src.hasPermissionLevel(2));
        clearsession.executes(ClearSessionsCommand::execute);
        dispatcher.register(clearsession);
    }

    public static int execute(CommandContext<ServerCommandSource> ctx) {
        SessionManager.reset();
        return 1;
    }

}
