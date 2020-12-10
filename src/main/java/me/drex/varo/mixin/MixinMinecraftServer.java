package me.drex.varo.mixin;

import me.drex.varo.session.SessionManager;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.PlayerManager;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.LiteralText;
import net.minecraft.util.Formatting;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.function.BooleanSupplier;

@Mixin(MinecraftServer.class)
public class MixinMinecraftServer {

    @Shadow
    private PlayerManager playerManager;

    @Inject(method = "tick", at = @At(value = "HEAD"))
    public void onServerTick(BooleanSupplier shouldKeepTicking, CallbackInfo ci) {
        for (ServerPlayerEntity player : this.playerManager.getPlayerList()) {
            long timeLeft = SessionManager.getTimeLeft(player.getUuid());
            if (timeLeft <= 0) {
                player.networkHandler.disconnect(new LiteralText("Time over!").formatted(Formatting.RED));
            }
        }
    }

}
