package org.puretripp.vassal.utils;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.scheduler.BukkitRunnable;
import org.puretripp.vassal.events.Events;
import org.puretripp.vassal.types.Nation;
import org.puretripp.vassal.types.ranks.TownRanks;
import org.puretripp.vassal.types.townships.Township;
import org.puretripp.vassal.utils.runnables.LineRunnable;

import java.util.*;

public class VassalsPlayer {
    private HashMap<Township, TownRanks> memberInfo = new HashMap<Township, TownRanks>();
    private ArrayList<Township> invites = new ArrayList<Township>();

    public LinkedList<Location> vertexSelections = new LinkedList<>();
    //It is doubly so O(1) access
    public LinkedList<ArrayList<LineRunnable>> partViewStack = new LinkedList<>();
    private ArrayList<BukkitRunnable> clientTasks = new ArrayList<>();
    public boolean canClickAgain = true;
    //See idk why size isn't working here, so lets do this
    int vertexSize = 0;
    private Township selectedTown;
    private boolean isInSelectionMode;
    private Nation n;
    private UUID uuid;

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

    public void addTown(Township t, TownRanks rank) throws IllegalArgumentException {
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

    public TownRanks getRank(Township t) {
        return memberInfo.get(t);
    }

    public void changeRank(Township t, TownRanks rank) {
        memberInfo.replace(t, rank);
    }

    public Nation getNation() { return n; }

    public boolean getSelectionMode() { return isInSelectionMode; }
    public void setSelectionMode(boolean newMode) { isInSelectionMode = newMode; }

    public LinkedList<Location> getAllVertices() { return vertexSelections; }

    public void setNation(Nation n) { this.n = n; }

    public void addInvite(Township t) { invites.add(t); }

    public void removeInvite(Township t) { invites.remove(t); }



    public void addTask(BukkitRunnable run) { clientTasks.add(run); }
    public int getVertexAmount() {
        return vertexSelections.size(); }
    public Location getVertex(int i) { return vertexSelections.get(i); }
    public void clearTasks() { clientTasks.clear(); }

    public void addVertex(Location v) { vertexSelections.add(v); }
    public void removeVertex(Location v) { vertexSelections.remove(v); }
    public boolean containsVertex(Location v) { return vertexSelections.contains(v); }
    public void clearVertices() { vertexSelections.clear(); }
    public Township[] getInvites() { return invites.toArray(new Township[invites.size()]); }
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
}
