package net.sushiclient.chatcontrol.data.gson;

import net.sushiclient.chatcontrol.data.IgnoreElement;
import net.sushiclient.chatcontrol.data.IgnoreList;
import net.sushiclient.chatcontrol.data.IgnoreType;

import java.util.*;
import java.util.function.Consumer;

class GsonIgnoreList implements IgnoreList {

    private transient UUID owner;
    private transient Consumer<GsonIgnoreList> saveCallback;
    private Collection<GsonIgnoreElement> ignoreElements;

    public GsonIgnoreList() {
    }

    public GsonIgnoreList(UUID owner, Consumer<GsonIgnoreList> saveCallback, List<GsonIgnoreElement> ignoreElements) {
        this.owner = owner;
        this.saveCallback = saveCallback;
        this.ignoreElements = Collections.synchronizedCollection(new HashSet<>(ignoreElements));
    }

    @Override
    public UUID getOwner() {
        return owner;
    }

    @Override
    public Collection<? extends IgnoreElement> getIgnoreElements() {
        return new HashSet<>(ignoreElements);
    }

    @Override
    public void addIgnoreElement(UUID target, IgnoreType ignoreType) {
        removeIgnoreElement(target);
        ignoreElements.add(new GsonIgnoreElement(target, ignoreType));
        saveCallback.accept(this);
    }

    @Override
    public boolean removeIgnoreElement(UUID target) {
        boolean result = ignoreElements.removeIf(it -> it.getTarget().equals(target));
        saveCallback.accept(this);
        return result;
    }

    @Override
    public void clearSoftIgnoreElements() {
        ignoreElements.removeIf(it -> it.getIgnoreType() == IgnoreType.SOFT);
        saveCallback.accept(this);
    }

    void onLoad(UUID owner, Consumer<GsonIgnoreList> saveCallback) {
        this.owner = owner;
        this.saveCallback = saveCallback;
    }
}
