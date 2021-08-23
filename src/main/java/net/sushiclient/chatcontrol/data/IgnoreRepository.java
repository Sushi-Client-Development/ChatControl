package net.sushiclient.chatcontrol.data;

import java.util.UUID;

public interface IgnoreRepository {
    IgnoreList findIgnoreList(UUID owner);
}
