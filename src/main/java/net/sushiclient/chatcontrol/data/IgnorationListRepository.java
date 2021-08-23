package net.sushiclient.chatcontrol.data;

import java.util.UUID;

/**
 * Loads and saves {@link IgnorationList}.
 * Implementations of this interface must be thread-safe
 */
public interface IgnorationListRepository {

    /**
     * Returns {@link IgnorationList} with specified {@link UUID}.
     *
     * @param owner The owner of the returned {@link IgnorationList}
     * @return {@link IgnorationList} with specified {@link IgnorationList#getOwner()}
     */
    IgnorationList findByUUID(UUID owner);
}
