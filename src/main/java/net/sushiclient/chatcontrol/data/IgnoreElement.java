package net.sushiclient.chatcontrol.data;

import java.util.UUID;

public interface IgnoreElement {
    UUID getTarget();

    IgnoreType getIgnoreType();
}
