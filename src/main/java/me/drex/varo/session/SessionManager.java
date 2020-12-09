package me.drex.varo.session;

import me.drex.varo.Mod;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.NbtIo;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.*;

public class SessionManager {

    public static HashMap<UUID, List<SessionEntry>> data = new HashMap<>();

    public static CompoundTag toNBT() {
        CompoundTag tag = new CompoundTag();
        for (Map.Entry<UUID, List<SessionEntry>> entry : data.entrySet()) {
            UUID uuid = entry.getKey();
            ListTag playerEntry = new ListTag();
            for (SessionEntry e : entry.getValue()) {
                playerEntry.add(e.toNBT());
            }
            tag.put(uuid.toString(), playerEntry);
        }
        return tag;
    }

    public static void load() throws IOException {
        CompoundTag tag = Mod.DATA.exists() ? NbtIo.readCompressed(new FileInputStream(Mod.DATA)) : new CompoundTag();
        for (String key : tag.getKeys()) {
            ListTag playerEntry = (ListTag) tag.get(key);
            UUID uuid = UUID.fromString(key);
            List<SessionEntry> entries = new ArrayList<>();
            for (int i = 0; i < playerEntry.size(); i++) {
                entries.add(new SessionEntry(playerEntry.getCompound(i)));
            }
            data.put(uuid, entries);
        }
    }

    public static SessionEntry getCurrentSession(UUID uuid) {
        for (SessionEntry sessionEntry : data.get(uuid)) {
            if (!sessionEntry.isComplete()) return sessionEntry;
        }
        throw new RuntimeException("Couldn't find the current session for " + uuid);
    }

    public static void addSession(UUID uuid) {
        List<SessionEntry> list = data.get(uuid);
        SessionEntry entry = new SessionEntry(new Date().getTime());
        if (list != null) {
            list.add(entry);
        } else {
            data.put(uuid, new ArrayList<>(Collections.singletonList(entry)));
        }
    }

}
