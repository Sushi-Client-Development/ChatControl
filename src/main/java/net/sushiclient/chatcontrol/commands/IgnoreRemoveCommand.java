package net.sushiclient.chatcontrol.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.Default;
import net.sushiclient.chatcontrol.Utils;
import net.sushiclient.chatcontrol.data.IgnorationListRepository;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

@CommandAlias("ignoreremove")
public class IgnoreRemoveCommand extends BaseCommand {

    private final Plugin plugin;
    private final IgnorationListRepository ignorationListRepository;

    public IgnoreRemoveCommand(Plugin plugin, IgnorationListRepository ignorationListRepository) {
        this.plugin = plugin;
        this.ignorationListRepository = ignorationListRepository;
    }

    @Default
    public void ignoreSoft(Player sender, OfflinePlayer target) {
        Utils.async(plugin, () -> ignorationListRepository.findByUUID(sender.getUniqueId()), list -> {
            list.removeIgnorationRecord(target.getUniqueId());
            sender.sendMessage(ChatColor.GOLD + "No longer ignoring " + ChatColor.DARK_AQUA + target.getName());
        });
    }
}
