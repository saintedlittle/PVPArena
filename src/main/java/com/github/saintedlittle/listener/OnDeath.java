package com.github.saintedlittle.listener;

import com.github.saintedlittle.data.PVPStorage;
import net.kyori.adventure.text.Component;
import org.bukkit.ChatColor;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

public class OnDeath implements Listener {

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onDeath(PlayerDeathEvent event) {
        Player entity = event.getEntity();

        Player killer = entity.getKiller();

        if (killer == null)
            return;


        // Это сработает для обоих, там идёт проверка по ключу и значению одновременно.
        PVPStorage.removePlayingPlayer(killer);

        String message = PVPStorage.getConfiguration().getString("deathMessage").replace("%s", killer.getName());
        event.deathMessage(Component.text(ChatColor.stripColor(message)));
        event.setKeepInventory(PVPStorage.getConfiguration().getBoolean("clearInventory"));

        killer.sendMessage(PVPStorage.getConfiguration().getString("winnerMessage").replace("%s", entity.getName()));
    }
}
