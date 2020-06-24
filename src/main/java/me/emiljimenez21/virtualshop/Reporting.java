package me.emiljimenez21.virtualshop;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import me.emiljimenez21.virtualshop.settings.Settings;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.mineacademy.fo.remain.Remain;

import javax.net.ssl.HttpsURLConnection;
import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.zip.GZIPOutputStream;

public class Reporting {
    public Plugin plugin;
    public String base_url = "https://magnidev.com/api/v1/";

    public Reporting(Plugin plugin) {
        this.plugin = plugin;
    }

    public void sendError(String message) {
        JsonObject data = new JsonObject();

        data.addProperty("serverUUID", Settings.serverUUID);
        data.addProperty("message", message);

        try {
            new Thread(() -> {
                try {
                    // Send the data
                    sendData("plugin_errors", data);
                } catch (final Exception e) {
                    // Do Nothing
                }
            }).start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void sendCommangUsage(JsonObject data) {
        data.addProperty("serverUUID", Settings.serverUUID);

        try {
            new Thread(() -> {
                try {
                    // Send the data
                    sendData("plugin_command_usage", data);
                } catch (final Exception e) {
                    // Do Nothing
                }
            }).start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void sendServerData() {
        final JsonArray pluginData = new JsonArray();
        int playerAmount;

        try {
            final Method onlinePlayersMethod = Class.forName("org.bukkit.Server").getMethod("getOnlinePlayers");
            playerAmount = onlinePlayersMethod.getReturnType().equals(Collection.class)
                    ? ((Collection<?>) onlinePlayersMethod.invoke(Bukkit.getServer())).size()
                    : ((Player[]) onlinePlayersMethod.invoke(Bukkit.getServer())).length;
        } catch (final Exception e) {
            playerAmount = Remain.getOnlinePlayers().size();
        }

        final int onlineMode = Bukkit.getOnlineMode() ? 1 : 0;
        final String bukkitVersion = Bukkit.getVersion();
        final String javaVersion = System.getProperty("java.version");
        final String osName = System.getProperty("os.name");
        final String osArch = System.getProperty("os.arch");
        final String osVersion = System.getProperty("os.version");
        final int coreCount = Runtime.getRuntime().availableProcessors();

        final JsonObject data = new JsonObject();

        data.addProperty("serverUUID", Settings.serverUUID);
        data.addProperty("serverName", Bukkit.getName());
        data.addProperty("port", Bukkit.getPort());
        data.addProperty("playerAmount", playerAmount);
        data.addProperty("onlineMode", onlineMode);
        data.addProperty("bukkitVersion", bukkitVersion);
        data.addProperty("javaVersion", javaVersion);
        data.addProperty("osName", osName);
        data.addProperty("osArch", osArch);
        data.addProperty("osVersion", osVersion);
        data.addProperty("coreCount", coreCount);

        for (final Class<?> service : Bukkit.getServicesManager().getKnownServices()) {
            for (final RegisteredServiceProvider<?> provider : Bukkit.getServicesManager().getRegistrations(service)) {
                try {
                    final Object plugin = provider.getService().getMethod("getPluginData").invoke(provider.getProvider());
                    if (plugin instanceof JsonObject)
                        pluginData.add((JsonObject) plugin);
                    else
                        try {
                            final Class<?> jsonObjectJsonSimple = Class.forName("org.json.simple.JSONObject");
                            if (plugin.getClass().isAssignableFrom(jsonObjectJsonSimple)) {
                                final Method jsonStringGetter = jsonObjectJsonSimple.getDeclaredMethod("toJSONString");
                                jsonStringGetter.setAccessible(true);
                                final String jsonString = (String) jsonStringGetter.invoke(plugin);
                                final JsonObject object = new JsonParser().parse(jsonString).getAsJsonObject();
                                pluginData.add(object);
                            }
                        } catch (final ClassNotFoundException e) {
                            continue;
                        }
                } catch (NullPointerException | NoSuchMethodException | IllegalAccessException | InvocationTargetException ignored) {
                }
            }
        }

        data.add("plugins", pluginData);

        // Create a new thread for the connection to the server
        new Thread(() -> {
            try {
                // Send the data
                sendData("plugin_metrics", data);
            } catch (final Exception e) {
                // Do Nothing
            }
        }).start();
    }

    /**
     * Sends the data to the server.
     *
     * @param data The data to send.
     * @throws Exception If the request failed.
     */
    private void sendData(final String endpoint, final JsonObject data) throws Exception {
        if (!Settings.reporting) {
            return;
        }

        if (data == null) {
            throw new IllegalArgumentException("Data cannot be null!");
        }

        if (Bukkit.isPrimaryThread()) {
            throw new IllegalAccessException("This method must not be called from the main thread!");
        }

        final HttpsURLConnection connection = (HttpsURLConnection) new URL(base_url + endpoint).openConnection();

        // Compress the data to save bandwidth
        final byte[] compressedData = compress(data.toString());

        // Add headers
        connection.setRequestMethod("POST");
        connection.addRequestProperty("Accept", "application/json");
        connection.addRequestProperty("Connection", "close");
        connection.addRequestProperty("Content-Encoding", "gzip"); // We gzip our request
        connection.addRequestProperty("Content-Length", String.valueOf(compressedData.length));
        connection.setRequestProperty("Content-Type", "application/json"); // We send our data in JSON format

        // Send data
        connection.setDoOutput(true);
        final DataOutputStream outputStream = new DataOutputStream(connection.getOutputStream());
        outputStream.write(compressedData);
        outputStream.flush();
        outputStream.close();

        final InputStream inputStream = connection.getInputStream();
        final BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));

        final StringBuilder builder = new StringBuilder();
        String line;
        while ((line = bufferedReader.readLine()) != null)
            builder.append(line);
        bufferedReader.close();
    }

    /**
     * Gzips the given String.
     *
     * @param str The string to gzip.
     * @return The gzipped String.
     * @throws IOException If the compression failed.
     */
    private static byte[] compress(final String str) throws IOException {
        if (str == null)
            return null;
        final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        final GZIPOutputStream gzip = new GZIPOutputStream(outputStream);
        gzip.write(str.getBytes(StandardCharsets.UTF_8));
        gzip.close();
        return outputStream.toByteArray();
    }

}
