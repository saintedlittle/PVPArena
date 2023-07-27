package com.github.saintedlittle;

import com.github.saintedlittle.data.PVPStorage;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

public final class PVPArena extends JavaPlugin {

    private FileConfiguration config;

    @Override
    public void onEnable() {
        config = getConfig();
        config.options().copyDefaults();

        PVPStorage.setConfiguration(config);

        saveConfig();
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
