package net.sushiclient.chatcontrol.data;

import org.bukkit.entity.Player;

public interface IgnoreRepository {
    IgnoreList findIgnoreList(Player player);
}
