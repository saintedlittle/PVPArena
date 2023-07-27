package com.github.saintedlittle.commands;

import com.github.saintedlittle.data.PVPStorage;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class PVPCommand implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!(sender instanceof Player player))
            return false;

        if (PVPStorage.isPlaying(player))
            return false;

        if (args.length == 0)
            return openMenu(player);

        return switch (args[0].toLowerCase()) {
            case "find" -> addToAwait(player);
            case "cancel" -> cancelAwait(player);
            default -> false;
        };

    }

    private static boolean openMenu(Player player) {
        return false;
    }

    private static boolean addToAwait(Player player) {
        if (PVPStorage.isWaiting(player))
            return false;


        player.sendMessage(Objects.requireNonNull(ChatColor.stripColor(PVPStorage.getConfiguration().getString("awaitingMessage"))));

        return PVPStorage.addAwaitingPlayer(player);
    }

    private static boolean cancelAwait(Player player) {
        if (!PVPStorage.isWaiting(player))
            return false;

        player.sendMessage(Objects.requireNonNull(ChatColor.stripColor(PVPStorage.getConfiguration().getString("cancelAwaitingMessage"))));

        return PVPStorage.removeAwaitingPlayer(player);
    }

}
