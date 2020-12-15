package me.drex.varo.mixin;

import me.drex.varo.Mod;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.rcon.RconCommandOutput;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(RconCommandOutput.class)
public class MixinRconCommandOutput {

    @Inject(method = "<init>", at = @At("RETURN"))
    private void saveInstance(MinecraftServer server, CallbackInfo ci) {
        Mod.server = server;
    }

}
