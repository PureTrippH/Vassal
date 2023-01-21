package org.puretripp.vassal.utils;

import org.bukkit.Location;
import org.bukkit.scheduler.BukkitRunnable;
import org.puretripp.vassal.types.Nation;
import org.puretripp.vassal.types.ranks.TownRanks;
import org.puretripp.vassal.types.townships.Township;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

public class VassalsPlayer {
    private HashMap<Township, TownRanks> memberInfo = new HashMap<Township, TownRanks>();
    private ArrayList<Township> invites = new ArrayList<Township>();

    private ArrayList<Location> vertexSelections = new ArrayList<>(10);
    private ArrayList<BukkitRunnable> clientTasks = new ArrayList<>();
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

    public ArrayList<Location> getAllVertices() { return vertexSelections; }

    public void setNation(Nation n) { this.n = n; }

    public void addInvite(Township t) { invites.add(t); }

    public void addTask(BukkitRunnable run) { clientTasks.add(run); }

    public void addVertex(Location v) { vertexSelections.add(v); }
    public void removeVertex(Location v) { vertexSelections.remove(v); }
    public boolean containsVertex(Location v) { return vertexSelections.contains(v); }
    public void clearVertices() { vertexSelections.clear(); }
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
