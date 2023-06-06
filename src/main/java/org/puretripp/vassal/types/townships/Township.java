package org.puretripp.vassal.types.townships;

import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.puretripp.vassal.types.Nation;
import org.puretripp.vassal.types.ranks.TownRanks;
import org.puretripp.vassal.utils.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

/**
 * A Standard town.
 */
public class Township {
    public static HashMap<String, Township> towns = new HashMap<String, Township>();
    //private static transient final long serialVersionUID = -1681012206529286330L;
    private UUID leader;
    private String name;
    private ArrayList<LandChunk> chunks;
    private ArrayList<Residence> residences;
    private ArrayList<UUID> players;
    private ArrayList<Player> insideClaim;

    private Nation nation;
    private double bal;

    /**
     * Constructs a full new town with no defaults. Deep copies everything.
     * @param name TODO
     * @param chunks TODO
     * @param players TODO
     * @param bal TODO
     */
    public Township(String name, UUID leader, double bal, ArrayList<LandChunk> chunks, ArrayList<UUID> players) {
        this.name = name;
        this.leader = leader;
        this.bal = bal;
        this.chunks = (ArrayList<LandChunk>) chunks.clone();
        this.players = (ArrayList<UUID>) players.clone();
    }

    public Township(String name, UUID leader, double bal, Chunk c, ArrayList<UUID> players) {
        this.name = name;
        this.leader = leader;
        this.bal = bal;
        this.players = (ArrayList<UUID>) players.clone();
        this.chunks = new ArrayList<LandChunk>();
        this.residences = new ArrayList<Residence>();
        LandChunk lc = new LandChunk(c, leader, this, ChunkType.CAPITAL);
        chunks.add(lc);
        VassalWorld.allLand.put(c, lc);
    }

    public Township(Township toCopy) {
        this.name = toCopy.name;
        this.leader = toCopy.leader;
        this.bal = toCopy.bal;
        this.chunks = (ArrayList<LandChunk>) toCopy.chunks.clone();
        this.players = (ArrayList<UUID>) toCopy.players.clone();
    }

    //TODO: Check for Adjacent Chunks/Buffer Regions
    public void claimChunk(Chunk c) throws NoSuchFieldException {
        if(!this.chunks.contains(VassalWorld.getLandChunkByChunk(c))) {
            LandChunk newClaim = new LandChunk(c, null, this, ChunkType.DEFAULT);
            VassalWorld.allLand.put(c, newClaim);
            chunks.add(newClaim);
        } else {
            throw new NoSuchFieldException("Chunk is Already Claimed!");
        }
    }

    public void display(Player p) {
        LandChunk origin = chunks.get(0);
        for(int i = 0; i < chunks.size(); i++) {
            Chunk c = chunks.get(i).getChunk();
            chunks.get(i).displayChunk(p,
                //North: Subtract 1 from Chunk z
                !chunks.contains(VassalWorld.getLandChunkByChunk(new Location(c.getWorld(),
                    c.getX() * 16, 64, (c.getZ() - 1) * 16).getChunk())),
                //South: Add 1 to Chunk z
                !chunks.contains(VassalWorld.getLandChunkByChunk(new Location(c.getWorld(),
                    c.getX() * 16 , 64, (c.getZ() + 1) * 16).getChunk())),
                //East: Add 1 to Chunk X
                !chunks.contains(VassalWorld.getLandChunkByChunk(new Location(c.getWorld(),
                    (c.getX() + 1) * 16, 64, c.getZ() * 16).getChunk())),
                //West Subtract 1 from Chunk X
                !chunks.contains(VassalWorld.getLandChunkByChunk(new Location(c.getWorld(),
                    (c.getX() - 1) * 16, 64, c.getZ() * 16).getChunk()))
            );
        }
    }
    public static Township getTownByName(String s) {
        return towns.get(s);
    }
    public ArrayList<UUID> getPlayers() { return players; }
    public Nation getNation() { return nation; }
    public void setNation(Nation nation) { this.nation = nation; };
    public String getName() { return name; }
    public void addResidence(Residence res) { residences.add(res); };
    public ArrayList<Residence> getAllResidences() { return residences; };
    public void addInsideClaim(Player p) { insideClaim.add(p); };
    public void removeInsideClaim(Player p) { insideClaim.remove(p); };

    public LandChunk getChunk(int i) { return chunks.get(i); }

    public void addPlayer(VassalsPlayer vp) {
        vp.addTown(this, TownRanks.CITIZEN);
    }


    @Override
    public boolean equals(Object o) {
        if(o instanceof Township) {
            Township t = (Township) o;
            if(this.name.equals(t.name)
                && this.leader.equals(t.leader)
                && this.bal == t.bal) {
                return true;
            }
        }
        return false;
    }

}
