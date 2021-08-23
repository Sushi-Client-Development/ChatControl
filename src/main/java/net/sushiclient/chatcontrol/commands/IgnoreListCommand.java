package net.sushiclient.chatcontrol.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.Default;
import net.sushiclient.chatcontrol.Utils;
import net.sushiclient.chatcontrol.data.IgnoreElement;
import net.sushiclient.chatcontrol.data.IgnoreListRepository;
import net.sushiclient.chatcontrol.data.IgnoreType;
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
    private final IgnoreListRepository ignoreListRepository;

    public IgnoreListCommand(Plugin plugin, IgnoreListRepository ignoreListRepository) {
        this.plugin = plugin;
        this.ignoreListRepository = ignoreListRepository;
    }

    @Default
    public void showList(Player sender) {
        Utils.async(plugin, () -> ignoreListRepository.findByUUID(sender.getUniqueId()), list -> {
            // convert UUID-IgnoreType to String-IgnoreType
            // Note: TreeMap automatically sorts entries by key
            Collection<? extends IgnoreElement> ignoreElements = list.getIgnoreElements();
            TreeMap<String, IgnoreType> ignoreMap = new TreeMap<>();
            for (IgnoreElement ignoreElement : ignoreElements) {
                OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(ignoreElement.getTarget());
                if (offlinePlayer != null) {
                    ignoreMap.put(offlinePlayer.getName(), ignoreElement.getIgnoreType());
                }
            }
            for (Map.Entry<String, IgnoreType> entry : ignoreMap.entrySet()) {
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
