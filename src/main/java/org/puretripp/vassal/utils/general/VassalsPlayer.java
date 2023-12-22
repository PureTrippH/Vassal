package org.puretripp.vassal.utils.general;

import net.bytebuddy.dynamic.scaffold.MethodGraph;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.puretripp.vassal.types.Nation;
import org.puretripp.vassal.types.townships.Township;
import org.puretripp.vassal.types.Residence;
import org.puretripp.vassal.utils.claiming.perms.PermClass;
import org.puretripp.vassal.utils.interfaces.GUIMenu;
import org.puretripp.vassal.utils.interfaces.Invitable;
import org.puretripp.vassal.utils.interfaces.InviteDeliverer;

import java.util.*;

/**
 * Wrapper for a Player
 * This so Violates SRP but IDC
 * Also, Constructor being made in a bit. Dw!
 */
public class VassalsPlayer implements Invitable {
    private HashMap<Township, PermClass> memberInfo = new HashMap<Township, PermClass>();
    private ArrayList<Residence> ownedSubClaims = new ArrayList<Residence>();
    private ArrayList<BukkitRunnable> clientTasks = new ArrayList<>();
    public HashMap<String, Boolean> cooldowns = new HashMap<>();
    private Township selectedTown;
    private Residence selectedResidence;
    private Nation n;
    private UUID uuid;
    //Holds The Current Menu Stack
    private Stack<GUIMenu> menuStack = new Stack<GUIMenu>();
    private ArrayList<InviteDeliverer> invites;

    public VassalsPlayer(UUID uuid) {
        this.uuid = uuid;
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

    public boolean getIsSelectionMode() { return !(selectedResidence == null); }
    public void setSelectionMode(Residence selected) { selectedResidence = selected; }


    public void setNation(Nation n) { this.n = n; }

    public void addResidence(Residence res) { ownedSubClaims.add(res); }
    public void removeSubClaim(Residence res) { ownedSubClaims.remove(res); }

    public void addTask(BukkitRunnable run) { clientTasks.add(run); }
    public void clearTasks() {
        for (BukkitRunnable task : clientTasks) {
            task.cancel();
        }
    }
    public void pushMenu(GUIMenu menu) {
        menuStack.push(menu);
        menu.open(this);
    }
    public void popMenu() {
        if (menuStack.size() <= 1) {
            menuStack.clear();
            return;
        }
        menuStack.pop();
        menuStack.peek().open(this);
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
    public void addInvite(InviteDeliverer inviter) {
        if (inviter == null) throw new IllegalArgumentException("param can not be null!");
        invites.add(inviter);
    }

    @Override
    public void acceptInvite(InviteDeliverer inviter) {
        inviter.addToInviter(this);
        this.invites.remove(inviter);
    }


    @Override
    public void rejectInvite(InviteDeliverer inviter) {
        inviter.removeInvite(this);
        this.invites.remove(inviter);
    }

    @Override
    public InviteDeliverer getInvite(int index) {
        return this.invites.get(index);
    }
    @Override
    public InviteDeliverer[] getAllInvites() {
        return (InviteDeliverer[]) invites.toArray();
    }
}
