package org.puretripp.vassal.utils;

import org.bukkit.Chunk;
import org.bukkit.entity.Player;
import org.puretripp.vassal.types.Nation;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * A class representing the whole world of the Vassal's plugin
 * @author PureTrippH
 * @Version 1.1
 */
public class VassalWorld {
    public String worldName;

    public static ArrayList<Nation> nations = new ArrayList<Nation>();
    public static ArrayList<VassalsPlayer> onlinePlayers = new ArrayList<VassalsPlayer>();
    public static HashMap<Chunk, LandChunk> allLand = new HashMap<Chunk, LandChunk>();
    public static LandChunk getLandChunkByChunk(Chunk c) {
        return allLand.get(c);
    }


    public static VassalsPlayer getPlayer(Player p) {
        return VassalWorld.onlinePlayers.get(VassalWorld.onlinePlayers.indexOf(new VassalsPlayer(p.getUniqueId())));
    }
}
