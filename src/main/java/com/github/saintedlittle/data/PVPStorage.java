package com.github.saintedlittle.data;

import com.github.saintedlittle.Utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

public class PVPStorage {

    private static FileConfiguration configuration;

    private static File dataFolder;

    private static final List<Player> awaitingPlayers = new ArrayList<>();
    private static final HashMap<Player, Player> playingPlayers = new HashMap<>();

    private static final List<World> activeWorlds = new ArrayList<>();

    public static FileConfiguration getConfiguration() {
        return configuration;
    }

    public static void setConfiguration(FileConfiguration configuration) {
        PVPStorage.configuration = configuration;
    }

    public static void setDataFolder(File dataFolder) {
        PVPStorage.dataFolder = dataFolder;
    }

    public static File getDataFolder() {
        return dataFolder;
    }

    public static boolean addAwaitingPlayer(Player player) {
        if (!awaitingPlayers.isEmpty()) {
            return addPlayingPlayers(awaitingPlayers.stream().findAny().get(), player);
        }

        return awaitingPlayers.add(player);
    }

    public static boolean addPlayingPlayers(Player player, Player two) {
        if (playingPlayers.containsKey(player) || playingPlayers.containsKey(two))
            return false;

        if (playingPlayers.containsValue(player) || playingPlayers.containsValue(two))
            return false;

        playingPlayers.put(player, two);

        player.sendMessage(ChatColor.stripColor(configuration.getString("findPVP").replace("%s", two.getName())));
        two.sendMessage(ChatColor.stripColor(configuration.getString("findPVP").replace("%s", player.getName())));

        Utils.teleportRandom(player, two);

        return true;
    }

    public static boolean isPlaying(Player player) {
        UUID uuid = player.getUniqueId();

        AtomicBoolean status_code = new AtomicBoolean(false);
        playingPlayers.forEach((k,v) -> {
            if (k.getUniqueId().equals(uuid) || v.getUniqueId().equals(uuid)) {
                status_code.set(true);
                return;
            }
        });

        return status_code.get();
    }

    public static boolean isWaiting(Player player) {
        UUID uuid = player.getUniqueId();

        AtomicBoolean status_code = new AtomicBoolean(false);
        awaitingPlayers.forEach(e -> {
            if (e.getUniqueId().equals(player.getUniqueId())) {
                status_code.set(true);
                return;
            }
        });

        return status_code.get();
    }

    public static boolean removeAwaitingPlayer(Player player) {
        return awaitingPlayers.remove(player);
    }

    public static boolean removePlayingPlayer(Player player) {

        AtomicBoolean status_code = new AtomicBoolean(false);

        AtomicReference<Player> key = new AtomicReference<>();

        playingPlayers.forEach((k,v) -> {
            if (k.equals(player) || v.equals(player)) {
                status_code.set(true);
                key.set(k);
                return;
            }
        });

        playingPlayers.remove(key.get());

        return status_code.get();
    }

    public static void addActiveWorld(World world) {
        activeWorlds.add(world);
    }

    public static void removeActiveWorld(World world) {
        activeWorlds.remove(world);
        Bukkit.getWorlds().remove(world);
    }

}
