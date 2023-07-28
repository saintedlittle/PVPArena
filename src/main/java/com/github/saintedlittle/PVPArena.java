package com.github.saintedlittle;

import com.github.saintedlittle.commands.PVPCommand;
import com.github.saintedlittle.data.PVPStorage;
import com.github.saintedlittle.listener.OnDeath;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.Objects;
import java.util.logging.Level;

public final class PVPArena extends JavaPlugin {

    private FileConfiguration config;

    @Override
    public void onEnable() {
        config = getConfig();
        config.options().copyDefaults();
        saveConfig();

        PVPStorage.setConfiguration(config);

        File schematicsFolder = new File(this.getDataFolder(), "schematics");
        if (!schematicsFolder.exists()) {
            schematicsFolder.mkdir();
            Bukkit.getLogger().log(Level.WARNING, "SCHEMATICS NOT FOUND! CREATE AND DISABLE PLUGIN..");
            this.onDisable();
        }

        getServer().getPluginManager().registerEvents(new OnDeath(), this);
        Objects.requireNonNull(this.getCommand("pvp")).setExecutor(new PVPCommand());

        PVPStorage.setDataFolder(this.getDataFolder());
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
