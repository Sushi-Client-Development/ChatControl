package net.sushiclient.chatcontrol.data.gson;

import net.sushiclient.chatcontrol.data.IgnoreElement;
import net.sushiclient.chatcontrol.data.IgnoreType;

import java.util.UUID;

class GsonIgnoreElement implements IgnoreElement {
    private UUID target;
    private IgnoreType ignoreType;

    public GsonIgnoreElement(UUID target, IgnoreType ignoreType) {
        this.target = target;
        this.ignoreType = ignoreType;
    }

    @Override
    public UUID getTarget() {
        return target;
    }

    @Override
    public IgnoreType getIgnoreType() {
        return ignoreType;
    }
}
