package net.sushiclient.chatcontrol.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.Default;
import net.sushiclient.chatcontrol.Utils;
import net.sushiclient.chatcontrol.data.IgnorationListRepository;
import net.sushiclient.chatcontrol.data.IgnorationRecord;
import net.sushiclient.chatcontrol.data.IgnorationType;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.util.Collection;
import java.util.Map;
import java.util.TreeMap;

@CommandAlias("ignorelist")
public class IgnoreListCommand extends BaseCommand {

    private final Plugin plugin;
    private final IgnorationListRepository ignorationListRepository;

    public IgnoreListCommand(Plugin plugin, IgnorationListRepository ignorationListRepository) {
        this.plugin = plugin;
        this.ignorationListRepository = ignorationListRepository;
    }

    @Default
    public void showList(Player sender) {
        Utils.async(plugin, () -> ignorationListRepository.findByUUID(sender.getUniqueId()), list -> {
            // convert UUID-IgnoreType to String-IgnoreType
            // Note: TreeMap automatically sorts entries by key
            Collection<? extends IgnorationRecord> ignoreElements = list.getIgnoreRecords();
            TreeMap<String, IgnorationType> ignoreMap = new TreeMap<>();
            for (IgnorationRecord ignorationRecord : ignoreElements) {
                OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(ignorationRecord.getTarget());
                if (offlinePlayer != null) {
                    ignoreMap.put(offlinePlayer.getName(), ignorationRecord.getIgnorationType());
                }
            }
            for (Map.Entry<String, IgnorationType> entry : ignoreMap.entrySet()) {
                sender.sendMessage(
                        ChatColor.DARK_AQUA + entry.getKey() + " " +
                                ChatColor.GRAY + "[" +
                                ChatColor.GOLD + entry.getValue() +
                                ChatColor.DARK_AQUA + "]"
                );
            }
        });
    }
}
