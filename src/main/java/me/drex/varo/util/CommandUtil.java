package me.drex.varo.util;

import com.mojang.authlib.GameProfile;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import net.minecraft.command.argument.GameProfileArgumentType;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.LiteralText;

import java.util.Collection;

public class CommandUtil {

    public static GameProfile getProfile(CommandContext<ServerCommandSource> ctx, String name) throws CommandSyntaxException {
        Collection<GameProfile> gameProfile = GameProfileArgumentType.getProfileArgument(ctx, name);
        if (gameProfile.isEmpty()) throw new SimpleCommandExceptionType(new LiteralText("No player found")).create();
        else if (gameProfile.size() > 1) throw new SimpleCommandExceptionType(new LiteralText("Not more than 1 player allowed")).create();
        return gameProfile.iterator().next();
    }

}
