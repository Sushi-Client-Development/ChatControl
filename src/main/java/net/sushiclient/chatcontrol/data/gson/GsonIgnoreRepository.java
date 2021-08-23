package net.sushiclient.chatcontrol.data.gson;

import com.google.gson.Gson;
import net.sushiclient.chatcontrol.data.IgnoreList;
import net.sushiclient.chatcontrol.data.IgnoreRepository;
import org.apache.commons.io.FileUtils;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.UUID;
import java.util.WeakHashMap;
import java.util.logging.Level;

final public class GsonIgnoreRepository implements IgnoreRepository {

    private final WeakHashMap<UUID, GsonIgnoreList> ignoreLists = new WeakHashMap<>();
    private final Gson gson;
    private final Plugin plugin;
    private final File baseDir;

    public GsonIgnoreRepository(Gson gson, Plugin plugin, File baseDir) {
        this.gson = gson;
        this.plugin = plugin;
        this.baseDir = baseDir;
    }

    private File getFileByOwner(UUID owner) {
        return new File(baseDir, owner + ".json");
    }

    @Override
    public IgnoreList findIgnoreList(UUID owner) {
        // search from cached list
        GsonIgnoreList ignoreList = ignoreLists.get(owner);
        if (ignoreList != null) return ignoreList;

        // load if file exists
        File jsonFile = getFileByOwner(owner);
        if (jsonFile.exists()) {
            try {
                ignoreList = gson.fromJson(FileUtils.readFileToString(jsonFile, StandardCharsets.UTF_8), GsonIgnoreList.class);
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
                FileUtils.writeStringToFile(jsonFile, gson.toJson(ignoreList), StandardCharsets.UTF_8);
            } catch (IOException exception) {
                String msg = "Could not save IgnoreList for " + ignoreList.getOwner();
                plugin.getLogger().log(Level.WARNING, msg, exception);
            }
        });
    }

}
