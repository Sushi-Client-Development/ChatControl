package net.sushiclient.chatcontrol;

import co.aikar.commands.BukkitCommandManager;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.sushiclient.chatcontrol.commands.IgnoreCommand;
import net.sushiclient.chatcontrol.commands.IgnoreHardCommand;
import net.sushiclient.chatcontrol.commands.IgnoreListCommand;
import net.sushiclient.chatcontrol.data.IgnorationList;
import net.sushiclient.chatcontrol.data.IgnorationListRepository;
import net.sushiclient.chatcontrol.data.IgnorationType;
import net.sushiclient.chatcontrol.data.gson.GsonIgnorationListRepository;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

public class Main extends JavaPlugin implements Listener {

    private IgnorationListRepository ignorationListRepository;

    @Override
    public void onEnable() {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        ignorationListRepository = new GsonIgnorationListRepository(gson, this, new File(getDataFolder(), "data"));
        BukkitCommandManager bukkitCommandManager = new BukkitCommandManager(this);
        bukkitCommandManager.registerCommand(new IgnoreCommand(this, ignorationListRepository));
        bukkitCommandManager.registerCommand(new IgnoreHardCommand(this, ignorationListRepository));
        bukkitCommandManager.registerCommand(new IgnoreListCommand(this, ignorationListRepository));

        Bukkit.getPluginManager().registerEvents(this, this);
    }

    @EventHandler
    public void onAsyncPlayerChat(AsyncPlayerChatEvent e) {
        e.getRecipients().removeIf(it ->
                ignorationListRepository.findByUUID(it.getUniqueId()).isIgnored(e.getPlayer().getUniqueId()));
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent e) {
        Bukkit.getScheduler().runTaskAsynchronously(this, () -> {
            IgnorationList ignorationList = ignorationListRepository.findByUUID(e.getPlayer().getUniqueId());
            ignorationList.removeIgnorationRecords(IgnorationType.SOFT);
        });
    }
}
