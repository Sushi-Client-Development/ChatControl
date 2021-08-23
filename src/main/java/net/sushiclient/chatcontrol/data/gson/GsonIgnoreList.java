package net.sushiclient.chatcontrol.data.gson;

import net.sushiclient.chatcontrol.data.IgnoreElement;
import net.sushiclient.chatcontrol.data.IgnoreList;
import net.sushiclient.chatcontrol.data.IgnoreType;

import java.util.List;
import java.util.UUID;
import java.util.function.Consumer;

class GsonIgnoreList implements IgnoreList {

    private transient UUID owner;
    private transient Consumer<GsonIgnoreList> saveCallback;
    private List<GsonIgnoreElement> ignoreElements;

    public GsonIgnoreList() {
    }

    public GsonIgnoreList(UUID owner, Consumer<GsonIgnoreList> saveCallback, List<GsonIgnoreElement> ignoreElements) {
        this.owner = owner;
        this.saveCallback = saveCallback;
        this.ignoreElements = ignoreElements;
    }

    @Override
    public UUID getOwner() {
        return owner;
    }

    @Override
    public List<? extends IgnoreElement> getIgnoreElements() {
        return ignoreElements;
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

    void onLoad(UUID owner, Consumer<GsonIgnoreList> saveCallback) {
        this.owner = owner;
        this.saveCallback = saveCallback;
    }
}
