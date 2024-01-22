package org.puretripp.vassal.types;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BannerMeta;
import org.puretripp.vassal.types.governments.GovStyles;
import org.puretripp.vassal.types.townships.Township;
import org.puretripp.vassal.utils.interfaces.PermissionHolder;
import org.puretripp.vassal.utils.claiming.perms.PermClass;
import org.puretripp.vassal.utils.general.VassalsPlayer;

import java.util.*;

/**
 * A Nation That Represents the Township Graph & Total Nation Properties
 * @Version 1.0
 */
public class Nation implements PermissionHolder {

    private String name;
    private Material bannerType = Material.WHITE_BANNER;
    private BannerMeta banner;
    private Set<Township> adjacencyList;
    private GovStyles style;

    private int nationBal;
    private ArrayList<VassalsPlayer> leaders;
    private ArrayList<VassalsPlayer> council;
    private ArrayList<PermClass> permLevels;
    private int kill;

    private ArrayList<PermClass> permClasses;
    private HashMap<UUID, PermClass> players;


    public Nation(String name, BannerMeta banner, GovStyles style,
        ArrayList<VassalsPlayer> leaders, ArrayList<VassalsPlayer> council,
        ArrayList<Township> towns) {
        this.name = name;
        if(banner == null ) {
            this.banner = (BannerMeta) (new ItemStack(Material.WHITE_BANNER)).getItemMeta();
        } else {
            this.banner = banner;
        }
        this.style = style;
        this.leaders = leaders;
        this.council = council;
        this.adjacencyList = new HashSet<Township>();
    }
    /**
     * Adds a Player to the Rank map
     * @param vp VassalPlayer to add
     * @param pc PermClass to initialize the player with
     * @exception IllegalArgumentException When arguments are null
     */
    public void addPlayer(VassalsPlayer vp, PermClass pc) {
        if (vp == null || pc == null) throw new IllegalArgumentException("Arguments can't be null!");
        players.put(vp.getUUID(), pc);
    }
    /**
     * Gets Name of Nation
     * @return String name of nation
     */
    public String getName() { return name; }

    /**
     * Sets Nation Banner Meta
     * @param banner BannerMeta of banner of the nation
     */
    public void setBanner(BannerMeta banner) { this.banner = banner; }

    /**
     * Sets the color of the banner
     * @param m Material of the new banner color
     */
    public void setBannerType(Material m) { this.bannerType = m; }

    /**
     * Gets Banner Color
     * @return Material of Banner
     */
    public Material getBannerType() { return bannerType; }
    /**
     * Gets Nation Banner Meta
     * @return banner BannerMeta of banner of the nation
     */
    public BannerMeta getBanner() { return banner; }

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
     * Gets All of the Players in the Permissable object as Player Object
     * @return Set of all the players as the Player Object
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
     * Gets All of the Players in the Permissable object as UUIDs
     * @return Set of all the players' UUIDs
     */
    public Set<UUID> getPlayersUUID() { return players.keySet(); }
}
