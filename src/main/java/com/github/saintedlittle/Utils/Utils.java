package com.github.saintedlittle.Utils;

import com.github.saintedlittle.PVPArena;
import com.github.saintedlittle.data.PVPStorage;
import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.WorldEdit;
import com.sk89q.worldedit.bukkit.BukkitWorld;
import com.sk89q.worldedit.extent.clipboard.Clipboard;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardFormat;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardFormats;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardReader;
import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldedit.session.ClipboardHolder;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.Random;

public class Utils {

    public static void teleportRandom(Player player, Player two) {
        // Create a new world for the schematic
        World schematicWorld = Bukkit.createWorld(new WorldCreator("schematic_world").environment(World.Environment.NORMAL));
        if (schematicWorld == null) {
            player.sendMessage("Failed to create the schematic world.");
            return;
        }

        // Load .schematic file using WorldEdit
        File schematicFile = randomSchematic(); // Replace with the actual path to your .schematic file

        ClipboardFormat clipboardFormat = ClipboardFormats.findByFile(schematicFile);
        if (clipboardFormat == null) {
            player.sendMessage("Failed to find the correct clipboard format for the .schematic file.");
            return;
        }

        try (FileInputStream schematicStream = new FileInputStream(schematicFile)) {
            ClipboardReader clipboardReader = clipboardFormat.getReader(schematicStream);
            Clipboard clipboard = clipboardReader.read();

            if (clipboard == null) {
                player.sendMessage("Failed to load the .schematic file.");
                return;
            }

            // Adjust the paste location as needed
            BlockVector3 pasteLocation = BlockVector3.at(player.getLocation().getX(), player.getLocation().getY() - 1, player.getLocation().getZ());
            com.sk89q.worldedit.world.World schematicLocalWorld = new BukkitWorld(schematicWorld);
            try (EditSession editSession = WorldEdit.getInstance().getEditSessionFactory().getEditSession(schematicLocalWorld, -1)) {
                ClipboardHolder clipboardHolder = new ClipboardHolder(clipboard);
                clipboardHolder.createPaste(editSession).to(pasteLocation).ignoreAirBlocks(false).copyEntities(true).build();
            }
        } catch (IOException e) {
            e.printStackTrace();
            player.sendMessage("An error occurred while pasting the .schematic file.");
            return;
        }

        // Teleport players to the new schematic world
        Location schematicSpawn = new Location(schematicWorld, 0, 70, 0); // Adjust the coordinates as needed

        player.teleport(schematicSpawn);
        two.teleport(schematicSpawn);

        PVPStorage.addActiveWorld(schematicWorld);
    }

    private static File randomSchematic() {
        File file = new File(PVPStorage.getDataFolder(), "schematics");
        File[] files = file.listFiles();

        Random random = new Random();

        int randomNumber = random.nextInt(files.length);

        return files[randomNumber];
    }
}
