package net.sushiclient.chatcontrol.data;

import java.util.Collection;
import java.util.UUID;

/**
 * Represents a collection of {@link IgnorationRecord}.
 * Implementations of this interface must be thread-safe.
 */
public interface IgnorationList {

    /**
     * Returns the owner of this instance.
     *
     * @return the owner of this instance
     */
    UUID getOwner();

    /**
     * Returns all the ignoration records.
     *
     * @return all the ignoration records
     */
    Collection<? extends IgnorationRecord> getIgnoreRecords();

    /**
     * Add {@link IgnorationRecord} to this list by specifying both target and {@link IgnorationType}.
     * If {@link IgnorationRecord} with specified {@link IgnorationRecord#getTarget()} already exists,
     * the old record will be removed.
     *
     * @param target         UUID of who get ignored
     * @param ignorationType {@link IgnorationType}
     */
    void addIgnorationRecord(UUID target, IgnorationType ignorationType);

    /**
     * Remove {@link IgnorationRecord} with specified {@link IgnorationRecord#getTarget()}
     *
     * @param target {@link UUID} of who got removed from this list
     * @return {@code true} if successfully removed, otherwise {@code false}
     */
    boolean removeIgnorationRecord(UUID target);

    /**
     * Remove {@link IgnorationRecord} with specified {@link IgnorationRecord#getIgnorationType()}
     *
     * @param type the type of {@link IgnorationType} to be removed
     * @return {@code true} if successfully removed, otherwise {@code false}
     */
    boolean removeIgnorationRecords(IgnorationType type);

    /**
     * Returns {@code true} if the player is ignored by {@link #getOwner()}}, otherwise {@code false}.
     *
     * @param target {@link UUID} of who sent a message, and who should be checked by this method
     * @return {@code true} if ignored, otherwise {@code false}
     */
    default boolean isIgnored(UUID target) {
        for (IgnorationRecord ignorationRecord : getIgnoreRecords()) {
            if (ignorationRecord.getTarget().equals(target)) return true;
        }
        return false;
    }
}
