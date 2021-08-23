package net.sushiclient.chatcontrol.data.gson;

import net.sushiclient.chatcontrol.data.IgnorationList;
import net.sushiclient.chatcontrol.data.IgnorationRecord;
import net.sushiclient.chatcontrol.data.IgnorationType;

import java.util.*;
import java.util.function.Consumer;

class GsonIgnorationList implements IgnorationList {

    private transient UUID owner;
    private transient Consumer<GsonIgnorationList> saveCallback;
    private Collection<GsonIgnorationRecord> ignoreElements;

    public GsonIgnorationList() {
    }

    public GsonIgnorationList(UUID owner, Consumer<GsonIgnorationList> saveCallback, List<GsonIgnorationRecord> ignoreElements) {
        this.owner = owner;
        this.saveCallback = saveCallback;
        this.ignoreElements = Collections.synchronizedCollection(new HashSet<>(ignoreElements));
    }

    @Override
    public UUID getOwner() {
        return owner;
    }

    @Override
    public Collection<? extends IgnorationRecord> getIgnoreRecords() {
        return new HashSet<>(ignoreElements);
    }

    @Override
    public void addIgnorationRecord(UUID target, IgnorationType ignorationType) {
        removeIgnorationRecord(target);
        ignoreElements.add(new GsonIgnorationRecord(target, ignorationType));
        saveCallback.accept(this);
    }

    @Override
    public boolean removeIgnorationRecord(UUID target) {
        boolean result = ignoreElements.removeIf(it -> it.getTarget().equals(target));
        saveCallback.accept(this);
        return result;
    }

    @Override
    public boolean removeIgnorationRecords(IgnorationType type) {
        boolean result = ignoreElements.removeIf(it -> it.getIgnorationType() == type);
        saveCallback.accept(this);
        return result;
    }

    void onLoad(UUID owner, Consumer<GsonIgnorationList> saveCallback) {
        this.owner = owner;
        this.saveCallback = saveCallback;
    }
}
