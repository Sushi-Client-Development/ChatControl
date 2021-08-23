package net.sushiclient.chatcontrol.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.Default;
import net.sushiclient.chatcontrol.Utils;
import net.sushiclient.chatcontrol.data.IgnoreListRepository;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

@CommandAlias("ignoreremove")
public class IgnoreRemoveCommand extends BaseCommand {

    private final Plugin plugin;
    private final IgnoreListRepository ignoreListRepository;

    public IgnoreRemoveCommand(Plugin plugin, IgnoreListRepository ignoreListRepository) {
        this.plugin = plugin;
        this.ignoreListRepository = ignoreListRepository;
    }

    @Default
    public void ignoreSoft(Player sender, OfflinePlayer target) {
        Utils.async(plugin, () -> ignoreListRepository.findByUUID(sender.getUniqueId()), list -> {
            list.removeIgnoreElement(target.getUniqueId());
            sender.sendMessage(ChatColor.GOLD + "No longer ignoring " + ChatColor.DARK_AQUA + target.getName());
        });
    }
}
