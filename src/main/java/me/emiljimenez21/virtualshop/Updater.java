package me.emiljimenez21.virtualshop;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.bukkit.plugin.Plugin;
import org.mineacademy.fo.Common;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.file.Files;
import java.util.logging.Level;


public class Updater
{
    private static final String USER_AGENT = "Updater by Stipess1";
    private String downloadLink;
    private Plugin plugin;
    private File updateFolder;
    private File file;
    private int id;
    private int page = 1;
    private UpdateType updateType;
    private boolean emptyPage;
    private boolean logger;

    private static final String DOWNLOAD = "/download";
    private static final String VERSIONS = "/versions";
    private static final String PAGE = "?page=";
    private static final String API_RESOURCE = "https://api.spiget.org/v2/resources/";

    public Updater(Plugin plugin, int id, File file, UpdateType updateType, boolean logger)
    {
        this.plugin = plugin;
        this.updateFolder = plugin.getServer().getUpdateFolderFile();
        updateFolder.mkdirs();
        this.id = id;
        this.file = file;
        this.updateType = updateType;
        this.logger = logger;
    }

    public void run() {
        if(isValidResource()) {
            checkUpdate();
        } else {
            Common.log("Resource #" + id + " does not exist on Spigot!");
        }
    }

    public enum UpdateType
    {
        VERSION_CHECK,
        DOWNLOAD,
        CHECK_DOWNLOAD
    }

    /**
     * Check if id of resource is valid
     *
     * @return true if id of resource is valid
     */
    private boolean isValidResource()
    {
        emptyPage = false;
        downloadLink = API_RESOURCE + id;
        try
        {
            URL url = new URL(downloadLink);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.addRequestProperty("User-Agent", USER_AGENT);

            int code = connection.getResponseCode();

            if(code != 200)
            {
                connection.disconnect();
                return false;
            }

            downloadLink = downloadLink + DOWNLOAD;
            connection.disconnect();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        return true;
    }

    /**
     * Checks if there is any update available.
     */
    private void checkUpdate()
    {
        try
        {
            String page = Integer.toString(this.page);

            URL url = new URL(API_RESOURCE+id+VERSIONS+PAGE+page);

            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.addRequestProperty("User-Agent", USER_AGENT);

            InputStream inputStream = connection.getInputStream();
            InputStreamReader reader = new InputStreamReader(inputStream);

            JsonElement element = new JsonParser().parse(reader);
            JsonArray jsonArray = element.getAsJsonArray();

            if(jsonArray.size() == 10 && !emptyPage) {
                connection.disconnect();
                this.page++;
                checkUpdate();
            } else if(jsonArray.size() == 0) {
                emptyPage = true;
                this.page--;
                checkUpdate();
            } else {
                element = jsonArray.get(jsonArray.size()-1);

                JsonObject object = element.getAsJsonObject();
                element = object.get("name");
                String version = element.toString().replaceAll("\"", "").replace("v", "");
                if(logger) {
                    Common.log("Checking for an update...");
                }

                if(shouldUpdate(version, plugin.getDescription().getVersion()) && updateType == UpdateType.VERSION_CHECK) {
                    if(logger)
                        Common.log("Update found!");
                } else if(updateType == UpdateType.DOWNLOAD) {
                    if(logger)
                        Common.log("Downloading update... version not checked");
                    download();
                } else if(updateType == UpdateType.CHECK_DOWNLOAD) {
                    if(shouldUpdate(version, plugin.getDescription().getVersion())) {
                        if(logger)
                            Common.log("Update found, downloading now...");
                        download();
                    } else {
                        if(logger)
                            Common.log("Update not found");
                    }
                } else {
                    if(logger)
                        Common.log("Update not found");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Checks if plugin should be updated
     * @param newVersion remote version
     * @param oldVersion current version
     */
    private boolean shouldUpdate(String newVersion, String oldVersion)
    {
        return !newVersion.equalsIgnoreCase(oldVersion);
    }

    /**
     * Downloads the file
     */
    private void download()
    {
        BufferedInputStream in = null;
        FileOutputStream fout = null;

        try
        {
            URL url = new URL(downloadLink);
            in = new BufferedInputStream(url.openStream());
            File pluginName = new File(updateFolder, file.getName());
            if(!pluginName.exists()){
                pluginName.createNewFile();
            }
            fout = new FileOutputStream(pluginName);
            final byte[] data = new byte[4096];
            int count;
            while ((count = in.read(data, 0, 4096)) != -1) {
                fout.write(data, 0, count);
            }
        }
        catch (Exception e)
        {
            if(logger)
                Common.logFramed("An error occurred while trying to download update!");
                e.printStackTrace();
        }
        finally {
            try {
                if (in != null) {
                    in.close();
                }
            } catch (final IOException e) {
                this.plugin.getLogger().log(Level.SEVERE, null, e);
                e.printStackTrace();
            }
            try {
                if (fout != null) {
                    fout.close();
                }
            } catch (final IOException e) {
                e.printStackTrace();
                this.plugin.getLogger().log(Level.SEVERE, null, e);
            }
        }
    }
}
