package net.sushiclient.chatcontrol.data;

import java.util.UUID;

public interface IgnoreListRepository {
    IgnoreList findByUUID(UUID owner);
}
