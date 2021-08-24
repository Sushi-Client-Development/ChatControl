package net.sushiclient.chatcontrol.data.gson;

import com.google.gson.Gson;
import net.sushiclient.chatcontrol.Utils;
import net.sushiclient.chatcontrol.data.IgnorationList;
import net.sushiclient.chatcontrol.data.IgnorationListRepository;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.logging.Level;

final public class GsonIgnorationListRepository implements IgnorationListRepository, Listener {

    private final Map<UUID, GsonIgnorationList> ignorationLists = Collections.synchronizedMap(new WeakHashMap<>());
    private final Gson gson;
    private final Plugin plugin;
    private final File baseDir;

    public GsonIgnorationListRepository(Gson gson, Plugin plugin, File baseDir) {
        this.gson = gson;
        this.plugin = plugin;
        this.baseDir = baseDir;
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    private File getFileByOwner(UUID owner) {
        return new File(baseDir, owner + ".json");
    }

    /* This method should be refactored with locks in the future for better performance */
    @Override
    public synchronized IgnorationList findByUUID(UUID owner) {
        // search from cached list
        GsonIgnorationList ignorationList = ignorationLists.get(owner);
        if (ignorationList != null) return ignorationList;

        // load if file exists
        File jsonFile = getFileByOwner(owner);
        if (jsonFile.exists()) {
            try {
                ignorationList = gson.fromJson(Utils.readFile(jsonFile), GsonIgnorationList.class);
                ignorationList.onLoad(owner, this::saveIgnorationList);
            } catch (IOException exception) {
                // re-new json
            }
        }
        if (ignorationList == null) {
            ignorationList = new GsonIgnorationList(owner, this::saveIgnorationList, new ArrayList<>());
        }
        ignorationLists.put(owner, ignorationList);
        return ignorationList;
    }

    void saveIgnorationList(GsonIgnorationList ignorationList) {
        File jsonFile = getFileByOwner(ignorationList.getOwner());
        Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
            try {
                Utils.writeString(jsonFile, gson.toJson(ignorationList));
            } catch (IOException exception) {
                String msg = "Could not save " + ignorationList.getClass() + " for " + ignorationList.getOwner();
                plugin.getLogger().log(Level.WARNING, msg, exception);
            }
        });
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e) {
        Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> findByUUID(e.getPlayer().getUniqueId()));
    }

}
