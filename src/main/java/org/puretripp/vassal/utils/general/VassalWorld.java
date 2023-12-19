package org.puretripp.vassal.utils.general;

import org.bukkit.Chunk;
import org.bukkit.entity.Player;
import org.puretripp.vassal.types.Nation;
import org.puretripp.vassal.utils.claiming.LandChunk;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

/**
 * A class representing the whole world of the Vassal's plugin
 * TODO: Make Singleton
 * @author PureTrippH
 * @Version 1.1
 */
public class VassalWorld {
    public String worldName;

    private static volatile VassalWorld currInstance;

    public ArrayList<Nation> nations;
    public HashMap<UUID, VassalsPlayer> onlinePlayers;
    public HashMap<Chunk, LandChunk> allLand;

    private VassalWorld() {
        this.nations = new ArrayList<Nation>();
        this.onlinePlayers = new HashMap<UUID, VassalsPlayer>();
        this.allLand = new HashMap<Chunk, LandChunk>();
    }

    public static VassalWorld getWorldInstance() {
        if (currInstance == null) {
            synchronized (VassalWorld.class) {
                if (currInstance == null) {
                    currInstance = new VassalWorld();
                }
            }
        }
        return currInstance;
    }

    public LandChunk getLandChunkByChunk(Chunk c) {
        return allLand.get(c);
    }

    public VassalsPlayer getPlayer(Player p) {
        return VassalWorld.getWorldInstance().onlinePlayers.get(p.getUniqueId());
    }

}
