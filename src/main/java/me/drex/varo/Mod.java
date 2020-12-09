package me.drex.varo;

import me.drex.varo.session.SessionManager;
import net.fabricmc.api.DedicatedServerModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.minecraft.SharedConstants;

import java.io.File;
import java.io.IOException;

public class Mod implements DedicatedServerModInitializer {

    public static final File DATA = new File(System.getProperty("user.dir") + "/config/sessions.dat");

    @Override
    public void onInitializeServer() {
        ServerLifecycleEvents.SERVER_STARTED.register(server -> {
            SharedConstants.isDevelopment = true;
            try {
                SessionManager.load();
            } catch (IOException e) {
                throw new RuntimeException("Could not load data.");
            }
        });
    }
}
