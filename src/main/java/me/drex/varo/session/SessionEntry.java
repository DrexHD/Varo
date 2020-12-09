package me.drex.varo.session;


import net.minecraft.nbt.CompoundTag;

import java.util.Date;
import java.util.Optional;

public class SessionEntry {

    public final long start;
    public Optional<Long> end = Optional.empty();

    public SessionEntry(long start) {
        this.start = start;
    }

    public SessionEntry(CompoundTag tag) {
        this.start = tag.getLong("start");
        end = tag.contains("end") ? Optional.of(tag.getLong("end")) : Optional.empty();
    }

    public CompoundTag toNBT() {
        CompoundTag tag = new CompoundTag();
        tag.putLong("start", this.start);
        if (end.isPresent()) tag.putLong("end", this.end.get());
        return tag;
    }

    public void complete() {
        this.end = Optional.of(new Date().getTime());
    }

    public boolean isComplete() {
        return end.isPresent();
    }

    public long getPlaytime() {
        return end.map(aLong -> aLong - start).orElseGet(() -> new Date().getTime() - start);
    }

}
