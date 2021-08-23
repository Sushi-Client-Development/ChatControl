package net.sushiclient.chatcontrol.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.Default;
import net.sushiclient.chatcontrol.Utils;
import net.sushiclient.chatcontrol.data.IgnorationListRepository;
import net.sushiclient.chatcontrol.data.IgnorationType;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

@CommandAlias("ignorehard")
public class IgnoreHardCommand extends BaseCommand {

    private final Plugin plugin;
    private final IgnorationListRepository ignorationListRepository;

    public IgnoreHardCommand(Plugin plugin, IgnorationListRepository ignorationListRepository) {
        this.plugin = plugin;
        this.ignorationListRepository = ignorationListRepository;
    }

    @Default
    public void ignoreHard(Player sender, OfflinePlayer target) {
        Utils.async(plugin, () -> ignorationListRepository.findByUUID(sender.getUniqueId()), list -> {
            list.addIgnorationRecord(target.getUniqueId(), IgnorationType.HARD);
            sender.sendMessage(ChatColor.GOLD + "Now permanently ignoring " + ChatColor.DARK_AQUA + target.getName());

        });
    }
}
