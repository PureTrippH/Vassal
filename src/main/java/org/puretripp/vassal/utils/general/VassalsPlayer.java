package org.puretripp.vassal.utils.general;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.lustrouslib.menu.interfaces.MenuClient;
import org.lustrouslib.wrapper.PlayerWrapper;
import org.puretripp.vassal.types.Nation;
import org.puretripp.vassal.types.townships.Township;
import org.puretripp.vassal.types.Residence;
import org.puretripp.vassal.utils.claiming.perms.PermClass;
import org.puretripp.vassal.utils.interfaces.Invitable;
import org.puretripp.vassal.utils.interfaces.Inviter;

import java.util.*;

/**
 * Wrapper for a Player
 * This so Violates SRP but IDC
 * Also, Constructor being made in a bit. Dw!
 */
public class VassalsPlayer extends PlayerWrapper implements Invitable, MenuClient {
    private HashMap<Township, PermClass> memberInfo = new HashMap<Township, PermClass>();
    private ArrayList<Residence> ownedSubClaims = new ArrayList<Residence>();
    private ArrayList<BukkitRunnable> clientTasks = new ArrayList<>();
    public HashMap<String, Boolean> cooldowns = new HashMap<>();
    private Township selectedTown;
    private Residence selectedResidence;
    private Nation n;
    private UUID uuid;
    //Holds The Current Menu Stack
    private ArrayList<Inviter> invites;

    public VassalsPlayer(UUID uuid) {
        super(Bukkit.getPlayer(uuid));
        this.uuid = uuid;
        this.invites = new ArrayList<Inviter>();
    }
    public UUID getUUID() { return uuid; }
    public void setSelected(Township t) { selectedTown = t; }
    public Township getSelected() throws NullPointerException {
        if (selectedTown == null) {
            throw new NullPointerException("You Must Have A Town Selected!");
        }
        return selectedTown;
    }

    public void addTown(Township t, PermClass rank) throws IllegalArgumentException {
        if(memberInfo.containsKey(t)) {
            throw new IllegalArgumentException("Already A Member Of The Given Town!");
        }
        if(rank == null) {
            throw new IllegalArgumentException("Must be a valid rank!");
        }
        memberInfo.put(t, rank);
    }

    public boolean isInTown(Township t) {
        return memberInfo.containsKey(t);
    }



    public Nation getNation() { return n; }


    public void setNation(Nation n) { this.n = n; }

    public void addResidence(Residence res) { ownedSubClaims.add(res); }
    public void removeSubClaim(Residence res) { ownedSubClaims.remove(res); }

    public void addTask(BukkitRunnable run) { clientTasks.add(run); }
    public void clearTasks() {
        for (BukkitRunnable task : clientTasks) {
            task.cancel();
        }
    }
    @Override
    public boolean equals(Object o) {
        if(o instanceof VassalsPlayer) {
            VassalsPlayer pl = (VassalsPlayer) o;
            if (this.uuid.equals(pl.uuid)) {
                return true;
            }
        }
        return false;
    }

    public Residence getSelectedResidence() {
        return selectedResidence;
    }

    public Player getPlayer() {
        return Bukkit.getPlayer(uuid);
    }

    public void setSelectedResidence(Residence selectedResidence) {
        this.selectedResidence = selectedResidence;
    }

    @Override
    public void addInvite(Inviter inviter) {
        if (inviter == null) throw new IllegalArgumentException("param can not be null!");
        invites.add(inviter);
    }

    @Override
    public void acceptInvite(Inviter inviter) {
        inviter.addToInviter(this);
        this.invites.remove(inviter);
    }


    @Override
    public void rejectInvite(Inviter inviter) {
        inviter.removeInvite(this);
        this.invites.remove(inviter);
    }

    @Override
    public Inviter getInvite(int index) {
        return this.invites.get(index);
    }
    @Override
    public Inviter[] getAllInvites() {
        return invites.toArray(new Inviter[invites.size()]);
    }
}
