package org.puretripp.vassal.types.townships;

import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.puretripp.vassal.types.Nation;
import org.puretripp.vassal.types.ranks.TownRanks;
import org.puretripp.vassal.utils.claiming.ChunkType;
import org.puretripp.vassal.utils.claiming.LandChunk;
import org.puretripp.vassal.types.Residence;
import org.puretripp.vassal.utils.claiming.Permissable;
import org.puretripp.vassal.utils.claiming.perms.PermClass;
import org.puretripp.vassal.utils.general.VassalWorld;
import org.puretripp.vassal.utils.general.VassalsPlayer;

import java.net.SocketException;
import java.util.*;

/**
 * A Standard town.
 */
public class Township implements Permissable {
    public static HashMap<String, Township> towns = new HashMap<String, Township>();
    //private static transient final long serialVersionUID = -1681012206529286330L;
    private UUID leader;
    private String name;
    private ArrayList<PermClass> permClasses;
    private ArrayList<LandChunk> chunks;
    private ArrayList<Residence> residences;
    private HashMap<UUID, PermClass> players;
    private ArrayList<Player> insideClaim;
    private int level;

    private Nation nation;
    private double bal;

    /**
     * Constructor of a Township with Land
     * @param name String name of Township
     * @param leader UUID leader of the town
     * @param bal double balance of the town
     * @param chunks ArrayList of Chunks in town
     * @param players players in the new township
     */
    public Township(String name, UUID leader, double bal, ArrayList<LandChunk> chunks, HashMap<UUID, PermClass> players) {
        this.name = name;
        this.leader = leader;
        this.bal = bal;
        this.chunks = (ArrayList<LandChunk>) chunks.clone();
        this.players = (HashMap<UUID, PermClass>) players.clone();
        this.permClasses = new ArrayList<PermClass>();
        this.permClasses.add(new PermClass());
        this.permClasses.add(new PermClass("Citizen"));
    }

    /**
     * Constructor of a landless Township
     * @param name String name of Township
     * @param leader UUID leader of the town
     * @param bal double balance of the town
     * @param c theOrigin Chunk
     * @param players players in the new township
     */
    public Township(String name, UUID leader, double bal, Chunk c, HashMap<UUID, PermClass> players) {
        this.name = name;
        this.leader = leader;
        this.bal = bal;
        this.players = (HashMap<UUID, PermClass>) players.clone();
        this.chunks = new ArrayList<LandChunk>();
        this.residences = new ArrayList<Residence>();
        this.permClasses = new ArrayList<PermClass>();
        this.permClasses.add(new PermClass());
        this.permClasses.add(new PermClass("Citizen"));
        LandChunk lc = new LandChunk(c, leader, this, ChunkType.CAPITAL);
        chunks.add(lc);
        VassalWorld.allLand.put(c, lc);
    }

    /**
     * Copy Constructor for Townships
     * @param toCopy Township to Copy
     */
    public Township(Township toCopy) {
        this.name = toCopy.name;
        this.leader = toCopy.leader;
        this.bal = toCopy.bal;
        this.chunks = (ArrayList<LandChunk>) toCopy.chunks.clone();
        this.players = (HashMap<UUID, PermClass>) toCopy.players.clone();
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
    public Set<UUID> getPlayersUUID() { return players.keySet(); }
    public Nation getNation() { return nation; }
    public void setNation(Nation nation) { this.nation = nation; };
    public String getName() { return name; }
    public void addResidence(Residence res) { residences.add(res); };
    public ArrayList<Residence> getAllResidences() { return residences; };
    public void addInsideClaim(Player p) { insideClaim.add(p); };
    public void removeInsideClaim(Player p) { insideClaim.remove(p); };

    //Permission Ranks
    public PermClass getRank(int i) {
        return permClasses.get(i);
    }

    public PermClass getPlayerRank(UUID uuid) {
        if (!players.containsKey(uuid)) throw new IllegalArgumentException("Player Does Not Exist!");
        return players.get(uuid);
    }

    public LandChunk getChunk(int i) { return chunks.get(i); }
    /**
     * Adds a Player to the Rank map
     * @param vp VassalPlayer to add
     * @param pc PermClass to initialize the player with
     * @exception IllegalArgumentException When arguments are null
     */
    public void addPlayer(VassalsPlayer vp, PermClass pc) {
        if (vp == null || pc == null) throw new IllegalArgumentException("Arguments can't be null!");
        players.put(vp.getUUID(), pc);
        vp.addTown(this, pc);
    }
    /**
     * Sets a player's perm class
     * @param vp VassalPlayer to set the rank
     * @param pc PermClass to set the player as
     * @exception IllegalArgumentException When arguments are null
     */
    public void setPlayerRank(VassalsPlayer vp, PermClass pc) {
        if (vp == null || pc == null) throw new IllegalArgumentException("Arguments can't be null!");
        players.replace(vp.getUUID(), pc);
    }

    /**
     * Gets The Player's Class Rank in the permissable entity
     * @param vp Vassal Player with the rank you want to get
     * @return PermClass Rank of the Class
     * @exception IllegalArgumentException When arguments are null
     */
    public PermClass getPlayerRank(VassalsPlayer vp) {
        if (vp == null) throw new IllegalArgumentException("Arguments can't be null!");
        return players.get(vp.getUUID());
    }

    /**
     *
     * @return Set of all of the players as the Player Object
     */
    public Set<Player> getPlayersObjs() {
        HashSet<Player> playerList = new HashSet<Player>();
        Iterator<UUID> it = players.keySet().iterator();
        UUID curr = it.next();
        while (it.hasNext()) {
            playerList.add(Bukkit.getPlayer(curr));
            curr = it.next();
        }
        return playerList;
    }

    /**
     * Gets The Player's Class Rank in the permissable entity
     * @param vp VassalPlayer to get the rank
     * @return PermClass Rank of the Class
     * @exception IllegalArgumentException When arguments are null
     */
    public PermClass getRank(VassalsPlayer vp) {
        if (vp == null) throw new IllegalArgumentException("Arguments can't be null!");
        return players.get(vp);
    }

    /**
     * Adds A Perm Class To The Permissable Entity
     * @param pc PermClass to Add
     */
    public void addPermClass(PermClass pc) {
        if (pc == null) throw new IllegalArgumentException("Arguments can't be null!");
        permClasses.add(pc);
    }

    /**
     * Removes a Perm Class from the Permissiable Entity
     * @param pc PermClass to Remove
     * @return PermClass that was removed
     */
    public PermClass removePermClass(PermClass pc) {
        if (pc == null) throw new IllegalArgumentException("Arguments can't be null!");
        if (permClasses.remove(pc)) {
            return pc;
        } else {
            throw new NoSuchElementException("Perm Class Was Not Found!");
        }
    }


    /**
     * Checks if towns are equal
     * @param o Object comparing against the Town
     * @return boolean is the object equal to this town?
     */
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
