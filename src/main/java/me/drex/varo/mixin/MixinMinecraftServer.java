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

import java.util.Iterator;
import java.util.function.BooleanSupplier;

@Mixin(MinecraftServer.class)
public class MixinMinecraftServer {

    @Shadow
    private PlayerManager playerManager;

    @Inject(method = "tick", at = @At(value = "HEAD"))
    public void onServerTick(BooleanSupplier shouldKeepTicking, CallbackInfo ci) {
        Iterator<ServerPlayerEntity> iterator = this.playerManager.getPlayerList().iterator();
        while (iterator.hasNext()) {
            ServerPlayerEntity player = iterator.next();
            long timeLeft = SessionManager.getTimeLeft(player.getUuid());
            if (timeLeft <= 0) {
                player.networkHandler.disconnect(new LiteralText("Zeit vorbei!").formatted(Formatting.RED));
            } else {
                String message = "";
                String timeLeftSeconds = "Du wirst in %s Sekunden vom Server gekickt!";
                String timeLeftMinutes = "Du wirst in %s Minuten vom Server gekickt!";
                switch ((int) timeLeft) {
                    case 900000: message = String.format(timeLeftMinutes, 15); break;
                    case 600000: message = String.format(timeLeftMinutes, 10); break;
                    case 300000: message = String.format(timeLeftMinutes, 5); break;
                    case 240000: message = String.format(timeLeftMinutes, 4); break;
                    case 180000: message = String.format(timeLeftMinutes, 3); break;
                    case 120000: message = String.format(timeLeftMinutes, 2); break;
                    case 60000: message = String.format(timeLeftMinutes, 1); break;
                    case 30000: message = String.format(timeLeftSeconds, 30); break;
                    case 20000: message = String.format(timeLeftSeconds, 20); break;
                    case 10000: message = String.format(timeLeftSeconds, 10); break;
                    case 5000: message = String.format(timeLeftSeconds, 5); break;
                    case 4000: message = String.format(timeLeftSeconds, 4); break;
                    case 3000: message = String.format(timeLeftSeconds, 3); break;
                    case 2000: message = String.format(timeLeftSeconds, 2); break;
                    case 1000: message = String.format(timeLeftSeconds, 1); break;
                }
                if (!message.equals("")) player.sendMessage(new LiteralText(message).formatted(Formatting.RED), false);
            }
        }
    }

}
