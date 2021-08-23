package net.sushiclient.chatcontrol.data;

import java.util.List;
import java.util.UUID;

public interface IgnoreList {
    UUID getOwner();

    List<IgnoreElement> getIgnoreElements();

    void addIgnoreElement(IgnoreElement ignoreElement);

    boolean removeIgnoreElement(IgnoreElement ignoreElement);

    default boolean isIgnored(UUID player) {
        for (IgnoreElement ignoreElement : getIgnoreElements()) {
            if (ignoreElement.getTarget().equals(player)) return true;
        }
        return false;
    }
}
