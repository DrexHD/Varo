package me.drex.varo.mixin;

import me.drex.varo.session.SessionManager;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerPlayNetworkHandler.class)
public class MixinServerPlayNetworkHandler {

    @Shadow public ServerPlayerEntity player;

    @Inject(method = "onDisconnected", at = @At(value = "RETURN"))
    public void onPlayerDisconnect(Text reason, CallbackInfo ci) {
        SessionManager.getCurrentSession(this.player.getUuid()).complete();
    }

}
