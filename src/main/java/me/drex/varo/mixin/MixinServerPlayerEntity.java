package me.drex.varo.mixin;

import com.mojang.authlib.GameProfile;
import me.drex.varo.Mod;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.BannedPlayerEntry;
import net.minecraft.server.BannedPlayerList;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.LiteralText;
import net.minecraft.util.Formatting;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerPlayerEntity.class)
public abstract class MixinServerPlayerEntity extends PlayerEntity {

    @Shadow
    public ServerPlayNetworkHandler networkHandler;

    public MixinServerPlayerEntity(World world, BlockPos pos, float yaw, GameProfile profile) {
        super(world, pos, yaw, profile);
    }

    @Inject(method = "onDeath", at = @At(value = "RETURN"))
    public void varo$onDeath(DamageSource source, CallbackInfo ci) {
        BannedPlayerList bannedPlayerList = Mod.server.getPlayerManager().getUserBanList();
        BannedPlayerEntry bannedPlayerEntry = new BannedPlayerEntry(this.getGameProfile(), null, source.getName(), null, "Du bist ausgeschieden!");
        bannedPlayerList.add(bannedPlayerEntry);
        this.networkHandler.disconnect(new LiteralText("Du bist ausgeschieden!").formatted(Formatting.RED));
    }
}