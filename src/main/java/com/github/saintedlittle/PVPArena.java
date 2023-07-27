package com.github.saintedlittle;

import com.github.saintedlittle.commands.PVPCommand;
import com.github.saintedlittle.data.PVPStorage;
import com.github.saintedlittle.listener.OnDeath;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Objects;

public final class PVPArena extends JavaPlugin {

    private FileConfiguration config;

    @Override
    public void onEnable() {
        config = getConfig();
        config.options().copyDefaults();

        PVPStorage.setConfiguration(config);

        saveConfig();

        getServer().getPluginManager().registerEvents(new OnDeath(), this);
        Objects.requireNonNull(this.getCommand("pvp")).setExecutor(new PVPCommand());
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
