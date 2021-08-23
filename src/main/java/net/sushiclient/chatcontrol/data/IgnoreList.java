package net.sushiclient.chatcontrol.data;

import java.util.List;
import java.util.UUID;

public interface IgnoreList {
    UUID getOwner();

    List<? extends IgnoreElement> getIgnoreElements();

    void addIgnoreElement(UUID target, IgnoreType ignoreType);

    boolean removeIgnoreElement(UUID target);

    default boolean isIgnored(UUID target) {
        for (IgnoreElement ignoreElement : getIgnoreElements()) {
            if (ignoreElement.getTarget().equals(target)) return true;
        }
        return false;
    }
}
