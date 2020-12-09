package me.drex.varo;

import me.drex.varo.commands.TimePlayedCommand;
import me.drex.varo.session.SessionManager;
import net.fabricmc.api.DedicatedServerModInitializer;
import net.fabricmc.fabric.api.command.v1.CommandRegistrationCallback;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;

import java.io.File;
import java.io.IOException;

public class Mod implements DedicatedServerModInitializer {

    public static final File DATA = new File(System.getProperty("user.dir") + "/config/sessions.dat");

    @Override
    public void onInitializeServer() {
        ServerLifecycleEvents.SERVER_STARTED.register(server -> {
            try {
                SessionManager.load();
            } catch (IOException e) {
                throw new RuntimeException("Could not load data.");
            }
        });
        CommandRegistrationCallback.EVENT.register((dispatcher, b) -> {
            TimePlayedCommand.register(dispatcher);
        });
    }
}
