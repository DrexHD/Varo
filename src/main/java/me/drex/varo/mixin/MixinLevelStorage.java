package me.drex.varo.mixin;

import me.drex.varo.Mod;
import me.drex.varo.session.SessionManager;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtIo;
import net.minecraft.util.registry.DynamicRegistryManager;
import net.minecraft.world.SaveProperties;
import net.minecraft.world.level.storage.LevelStorage;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.io.FileOutputStream;
import java.io.IOException;

@Mixin(LevelStorage.Session.class)
public class MixinLevelStorage {

    @Inject(method = "backupLevelDataFile(Lnet/minecraft/util/registry/DynamicRegistryManager;Lnet/minecraft/world/SaveProperties;Lnet/minecraft/nbt/CompoundTag;)V", at = @At(value = "RETURN"))
    public void onServerSave(DynamicRegistryManager dynamicRegistryManager, SaveProperties saveProperties, @Nullable CompoundTag compoundTag, CallbackInfo ci) {
        try {
            CompoundTag tag = SessionManager.toNBT();
            Mod.DATA.createNewFile();
            NbtIo.writeCompressed(tag, new FileOutputStream(Mod.DATA));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
