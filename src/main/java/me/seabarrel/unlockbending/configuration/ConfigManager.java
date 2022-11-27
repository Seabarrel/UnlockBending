package me.seabarrel.unlockbending.configuration;

import org.bukkit.configuration.file.FileConfiguration;

import java.io.File;

public class ConfigManager {

    public static Config defaultConfig;

    public ConfigManager() {
            defaultConfig = new Config(new File("config.yml"));
            configCheck();
            }

    public void configCheck() {
            FileConfiguration config = defaultConfig.get();

            String[] defaults = new String[]{"WaterManipulation", "FireManipulation", "EarthBlast", "Airblast", "FerroControl", "FastSwim", "AirAgility", "Tremorsense", "Illumination", "Bottlebending"};
            config.addDefault("Unlocked.Default", defaults);
            config.addDefault("Permissions.BypassUnlockWhenOP", true);
            config.addDefault("Debug.SendConsoleEvent", false);
            config.addDefault("Scroll.Item", "BOOK");
            defaultConfig.save();

            }


    public static FileConfiguration getConfig() {
            return ConfigManager.defaultConfig.get();
            }

}