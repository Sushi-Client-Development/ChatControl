package net.sushiclient.chatcontrol.data;

import java.util.Collection;
import java.util.UUID;

public interface IgnoreList {
    UUID getOwner();

    Collection<? extends IgnoreElement> getIgnoreElements();

    void addIgnoreElement(UUID target, IgnoreType ignoreType);

    boolean removeIgnoreElement(UUID target);

    void clearSoftIgnoreElements();

    default boolean isIgnored(UUID target) {
        for (IgnoreElement ignoreElement : getIgnoreElements()) {
            if (ignoreElement.getTarget().equals(target)) return true;
        }
        return false;
    }
}
