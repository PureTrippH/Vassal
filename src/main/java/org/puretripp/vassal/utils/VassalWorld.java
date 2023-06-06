package org.puretripp.vassal.utils;

import org.bukkit.Chunk;
import org.bukkit.entity.Player;
import org.puretripp.vassal.types.Nation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

/**
 * A class representing the whole world of the Vassal's plugin
 * @author PureTrippH
 * @Version 1.1
 */
public class VassalWorld {
    public String worldName;

    public static ArrayList<Nation> nations = new ArrayList<Nation>();
    public static HashMap<UUID, VassalsPlayer> onlinePlayers = new HashMap<UUID, VassalsPlayer>();
    public static HashMap<Chunk, LandChunk> allLand = new HashMap<Chunk, LandChunk>();
    public static LandChunk getLandChunkByChunk(Chunk c) {
        return allLand.get(c);
    }


    public static VassalsPlayer getPlayer(Player p) {
        return VassalWorld.onlinePlayers.get(p.getUniqueId());
    }
}
