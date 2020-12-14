package me.drex.varo.mixin;

import com.mojang.authlib.GameProfile;
import me.drex.varo.session.SessionManager;
import net.minecraft.network.ClientConnection;
import net.minecraft.server.PlayerManager;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.net.SocketAddress;

@Mixin(PlayerManager.class)
public class MixinPlayerManager {

    @Inject(method = "onPlayerConnect", at = @At(value = "RETURN"))
    public void onPlayerConnect(ClientConnection connection, ServerPlayerEntity player, CallbackInfo info) {
        SessionManager.addSession(player.getUuid());
    }

    @Inject(method = "checkCanJoin", at = @At(value = "HEAD"))
    public void blockIfNoPlaytime(SocketAddress address, GameProfile profile, CallbackInfoReturnable<Text> cir) {
        if (SessionManager.getTimeLeft(profile.getId()) < 0) cir.setReturnValue(new LiteralText("You don't have any time left, wait until tomorrow to rejoin."));
    }

}
