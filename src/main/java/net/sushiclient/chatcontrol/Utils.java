package net.sushiclient.chatcontrol;

import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class Utils {

    private static final int BUFFER_SIZE = 8192;

    public static <T> void async(Plugin plugin, Supplier<T> supplier, Consumer<? super T> callback) {
        Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
            T result = supplier.get();
            Bukkit.getScheduler().runTask(plugin, () -> callback.accept(result));
        });
    }

    private static void copy(Reader reader, Writer writer) throws IOException {
        char[] buffer = new char[BUFFER_SIZE];
        int len;
        while ((len = reader.read(buffer)) != -1) {
            writer.write(buffer, 0, len);
        }
    }

    public static String readFile(File file) throws IOException {
        try (StringWriter out = new StringWriter();
             InputStreamReader in = new InputStreamReader(new FileInputStream(file), StandardCharsets.UTF_8)) {
            copy(in, out);
            return out.toString();
        }
    }

    public static void writeString(File file, String str) throws IOException {
        file.getParentFile().mkdirs();
        try (StringReader in = new StringReader(str);
             OutputStreamWriter out = new OutputStreamWriter(new FileOutputStream(file), StandardCharsets.UTF_8)) {
            copy(in, out);
        }
    }
}
