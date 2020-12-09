package me.drex.varo.mixin;

import me.drex.varo.session.SessionManager;
import net.minecraft.network.ClientConnection;
import net.minecraft.server.PlayerManager;
import net.minecraft.server.network.ServerPlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerManager.class)
public class MixinPlayerManager {

    @Inject(method = "onPlayerConnect", at = @At(value = "RETURN"))
    public void onPlayerConnect(ClientConnection connection, ServerPlayerEntity player, CallbackInfo info) {
        SessionManager.addSession(player.getUuid());
    }

}
