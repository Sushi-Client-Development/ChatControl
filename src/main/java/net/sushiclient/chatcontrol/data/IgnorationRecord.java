package net.sushiclient.chatcontrol.data;

import java.util.UUID;

/**
 * Represents a single ignore record.
 * Implementations of this interface must be thread-safe.
 */
public interface IgnorationRecord {

    /**
     * Returns {@link UUID} of who got ignored.
     *
     * @return {@link UUID} of who got ignored
     */
    UUID getTarget();

    /**
     * Returns the type of this record.
     *
     * @return the type of this record
     */
    IgnorationType getIgnorationType();
}
