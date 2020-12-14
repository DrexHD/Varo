package me.drex.varo.session;

import me.drex.varo.Mod;
import me.drex.varo.util.TimeDifferenceUtil;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.NbtIo;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.*;

public class SessionManager {

    public static HashMap<UUID, List<SessionEntry>> data = new HashMap<>();
    public static boolean loaded = false;
    private static long MAXIMUM_TIME = 5400000L;

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
        loaded = true;
    }

    public static SessionEntry getCurrentSession(UUID uuid) {
        for (SessionEntry sessionEntry : data.get(uuid)) {
            if (!sessionEntry.isComplete()) return sessionEntry;
        }
        throw new RuntimeException("Couldn't find the current session for " + uuid);
    }

    public static List<SessionEntry> getEntries(UUID uuid) {
        return data.containsKey(uuid) ? data.get(uuid) : new ArrayList<>();
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

    public static long getPlaytimeBetween(UUID uuid, long from, long to) {
        long playtime = 0;
        for (SessionEntry entry : getEntries(uuid)) {
            playtime += Math.max(0, Math.min(entry.getEnd(), to) - Math.max(from, entry.start));
        }
        return playtime;
    }

    public static long yesterday(UUID uuid) {
        Date date = new Date();
        date.setHours(0);
        date.setMinutes(0);
        date.setSeconds(0);
        date.setDate(date.getDate() - 1);
        long from = date.getTime();
        date.setDate(date.getDate() + 1);
        long to = date.getTime();
        return SessionManager.getPlaytimeBetween(uuid, from, to);
    }

    public static long today(UUID uuid) {
        Date date = new Date();
        date.setHours(0);
        date.setMinutes(0);
        date.setSeconds(0);
        long from = date.getTime();
        date.setDate(date.getDate() + 1);
        long to = date.getTime();
        return SessionManager.getPlaytimeBetween(uuid, from, to);
    }

    public static long getTimeLeft(UUID uuid) {
        return MAXIMUM_TIME - (Math.min(MAXIMUM_TIME / 2, yesterday(uuid)) + today(uuid));
    }

}
