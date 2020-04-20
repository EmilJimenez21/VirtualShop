package me.emiljimenez21.virtualshop.settings;

import org.mineacademy.fo.settings.SimpleSettings;

public class Settings extends SimpleSettings {
    public static Boolean databaseEnabled;
    public static String databaseHost;
    public static String databaseUser;
    public static String databasePass;
    public static String databaseName;
    public static String prefix;
    public static Boolean sound;
    public static Integer databasePort;
    public static String databasePrefix;

    @Override
    protected int getConfigVersion() {
        return 3;
    }

    private static void init() {
        pathPrefix(null);
        prefix = getString("prefix");
        sound = getBoolean("sound");

        pathPrefix("mysql");
        databaseEnabled = getBoolean("enabled");
        databaseHost = getString("host");
        databasePort = getInteger("port");
        databaseUser = getString("user");
        databasePass = getString("pass");
        databaseName = getString("name");
        databasePrefix = getString("prefix");
    }
}
