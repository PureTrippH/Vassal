package org.puretripp.vassal.utils.claiming;

import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.puretripp.vassal.main.Main;
import org.puretripp.vassal.types.townships.Township;

import java.util.UUID;

public class LandChunk {
    private Chunk chunk;
    private UUID owner;
    private Township town;
    private World world;
    private ChunkType type;

    /**
     * Primary Constructor
     * @param c
     * @param uuid
     * @param town
     * @param type
     */
    public LandChunk(Chunk c, UUID uuid, Township town, ChunkType type) {

        this.world = c.getWorld();
        this.chunk = c;
        this.owner = uuid;
        //TODO: Implement Deep Copy
        this.town = town;
        this.type = ChunkType.DEFAULT;
    }

    /**
     * A Constructor used for getting a land chunk ONLY using a Chunk since
     * I can't extend to a Chunk and I do not want to fully implement.
     * @param c Chunk you want to make into a land chunk.
     */
    public LandChunk(Chunk c) {
        this(c, null, null, null);
    }

    /**
     * A Constuctor only using primative data (outside of UUID).
     * @param world
     * @param cordx
     * @param cordz
     * @param uuid
     * @param townName
     * @param type
     */
    public LandChunk(String world, int cordx, int cordz, UUID uuid, String townName, String type) {
        this.world = Bukkit.getWorld(world);
        this.owner = uuid;
        this.world.getChunkAt(cordx, cordz);
        this.town = Township.getTownByName(townName);
        switch(type) {
            case "Capital":
                this.type = ChunkType.CAPITAL;
                break;
            case "Treasury":
                this.type = ChunkType.TREASURY;
                break;
            default:
                this.type = ChunkType.DEFAULT;
                break;
        }
    }

    public void setOwner(UUID uuid) {
        this.owner = uuid;
    }

    public void setTown(Township t) {
        this.town = t;
    }

    public Township getTown() {
        return town;
    }

    public Chunk getChunk() {
        return chunk;
    }
    public BukkitRunnable displayChunk(Player p, boolean showNorth, boolean showSouth, boolean showEast, boolean showWest) {
        BukkitRunnable showParticles = new BukkitRunnable() {
            int i = 0;
            @Override
            public void run() {
                for (int i = 0; i < 17; i++) {
                    if (showWest) {
                        p.spawnParticle(Particle.CAMPFIRE_COSY_SMOKE, new Location(p.getWorld(),
                            chunk.getX() * 16,
                            p.getLocation().getY(),
                            chunk.getZ() * 16 + i), 5,
                    0, 1, 0, 0);
                    }
                    if (showNorth) {
                        p.spawnParticle(Particle.CAMPFIRE_COSY_SMOKE, new Location(p.getWorld(),
                            chunk.getX() * 16 + i,
                            p.getLocation().getY(),
                            chunk.getZ() * 16), 5,
                    0, 1, 0, 0);
                    }
                    if (showSouth) {
                        p.spawnParticle(Particle.CAMPFIRE_COSY_SMOKE, new Location(p.getWorld(),
                            chunk.getX() * 16 + i,
                            p.getLocation().getY(),
                            chunk.getZ() * 16 + 16), 5,
                    0, 1, 0, 0);
                    }
                    if (showEast) {
                        p.spawnParticle(Particle.CAMPFIRE_COSY_SMOKE, new Location(p.getWorld(),
                            chunk.getX() * 16 + 16,
                            p.getLocation().getY(),
                            chunk.getZ() * 16 + i), 5,
                    0, 1, 0, 0);
                    }
                }
                if(i == 5) {
                    this.cancel();
                }
                i++;
            }
        };
        showParticles.runTaskTimerAsynchronously(Main.getPlugin(Main.class), 1L, 20L);
        return showParticles;
    }

    @Override
    public boolean equals(Object o) {
        if(o instanceof LandChunk) {
            LandChunk c = (LandChunk) o;
            if(this.owner == null) {
                if(this.chunk.equals(c.chunk)
                        && this.town.equals(c.town)
                        && this.type == c.type
                        && this.world.equals(c.world)) {
                    return true;
                }
            }
            if(this.chunk.equals(c.chunk)
                && this.owner.equals(c.owner)
                && this.town.equals(c.town)
                && this.type == c.type
                && this.world.equals(c.world)) {
                return true;
            }
        }
        return false;
    }
}
