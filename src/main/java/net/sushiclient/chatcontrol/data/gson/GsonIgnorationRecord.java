package net.sushiclient.chatcontrol.data.gson;

import net.sushiclient.chatcontrol.data.IgnorationRecord;
import net.sushiclient.chatcontrol.data.IgnorationType;

import java.util.UUID;

class GsonIgnorationRecord implements IgnorationRecord {
    private UUID target;
    private IgnorationType ignorationType;

    public GsonIgnorationRecord(UUID target, IgnorationType ignorationType) {
        this.target = target;
        this.ignorationType = ignorationType;
    }

    @Override
    public UUID getTarget() {
        return target;
    }

    @Override
    public IgnorationType getIgnorationType() {
        return ignorationType;
    }
}
