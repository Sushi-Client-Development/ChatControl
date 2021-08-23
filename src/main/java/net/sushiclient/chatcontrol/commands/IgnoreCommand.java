package net.sushiclient.chatcontrol.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandPermission;
import co.aikar.commands.annotation.Default;
import net.sushiclient.chatcontrol.Utils;
import net.sushiclient.chatcontrol.data.IgnoreListRepository;
import net.sushiclient.chatcontrol.data.IgnoreType;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

@CommandAlias("ignore")
@CommandPermission("")
public class IgnoreCommand extends BaseCommand {

    private final Plugin plugin;
    private final IgnoreListRepository ignoreListRepository;

    public IgnoreCommand(Plugin plugin, IgnoreListRepository ignoreListRepository) {
        this.plugin = plugin;
        this.ignoreListRepository = ignoreListRepository;
    }

    @Default
    public void ignoreSoft(Player sender, OfflinePlayer target) {
        Utils.async(plugin, () -> ignoreListRepository.findByUUID(sender.getUniqueId()), list -> {
            list.addIgnoreElement(target.getUniqueId(), IgnoreType.SOFT);
            sender.sendMessage(ChatColor.GOLD + "Now ignoring " + ChatColor.DARK_AQUA + target.getName());
        });
    }
}
