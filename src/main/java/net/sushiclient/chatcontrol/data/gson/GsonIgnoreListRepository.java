package net.sushiclient.chatcontrol.data.gson;

import com.google.gson.Gson;
import net.sushiclient.chatcontrol.Utils;
import net.sushiclient.chatcontrol.data.IgnoreList;
import net.sushiclient.chatcontrol.data.IgnoreListRepository;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.logging.Level;

final public class GsonIgnoreListRepository implements IgnoreListRepository, Listener {

    private final Map<UUID, GsonIgnoreList> ignoreLists = Collections.synchronizedMap(new WeakHashMap<>());
    private final Gson gson;
    private final Plugin plugin;
    private final File baseDir;

    public GsonIgnoreListRepository(Gson gson, Plugin plugin, File baseDir) {
        this.gson = gson;
        this.plugin = plugin;
        this.baseDir = baseDir;
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    private File getFileByOwner(UUID owner) {
        return new File(baseDir, owner + ".json");
    }

    @Override
    public IgnoreList findByUUID(UUID owner) {
        // search from cached list
        GsonIgnoreList ignoreList = ignoreLists.get(owner);
        if (ignoreList != null) return ignoreList;

        // load if file exists
        File jsonFile = getFileByOwner(owner);
        if (jsonFile.exists()) {
            try {
                ignoreList = gson.fromJson(Utils.readFile(jsonFile), GsonIgnoreList.class);
                ignoreList.onLoad(owner, this::saveIgnoreList);
            } catch (IOException exception) {
                // re-new json
            }
        } else {
            ignoreList = new GsonIgnoreList(owner, this::saveIgnoreList, new ArrayList<>());
        }
        ignoreLists.put(owner, ignoreList);
        return ignoreList;
    }

    void saveIgnoreList(GsonIgnoreList ignoreList) {
        File jsonFile = getFileByOwner(ignoreList.getOwner());
        Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
            try {
                Utils.writeString(jsonFile, gson.toJson(ignoreList));
            } catch (IOException exception) {
                String msg = "Could not save IgnoreList for " + ignoreList.getOwner();
                plugin.getLogger().log(Level.WARNING, msg, exception);
            }
        });
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e) {
        Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> findByUUID(e.getPlayer().getUniqueId()));
    }

}
