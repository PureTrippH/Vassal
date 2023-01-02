package org.puretripp.vassal.utils;

import org.puretripp.vassal.types.ranks.TownRanks;
import org.puretripp.vassal.types.townships.Township;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

public class VassalsPlayer {
    private HashMap<Township, TownRanks> memberInfo = new HashMap<Township, TownRanks>();
    private Township selectedTown;
    private UUID uuid;

    public VassalsPlayer(UUID uuid) {
        this.uuid = uuid;
    }

    public UUID getUUID() { return uuid; };
    public void setSelected(Township t) { selectedTown = t; }
    public Township getSelected() throws NullPointerException {
        if (selectedTown == null) {
            throw new NullPointerException("You Must Have A Town Selected!");
        }
        return selectedTown;
    }

    public void addTown(Township t, TownRanks rank) throws IllegalArgumentException {
        if(rank == null) {
            throw new IllegalArgumentException("Must be a valid rank!");
        }
        memberInfo.put(t, rank);
    }

    public boolean isInTown(Township t) {
        return memberInfo.containsKey(t);
    }

    @Override
    public boolean equals(Object o) {
        if(o instanceof VassalsPlayer) {
            VassalsPlayer pl = (VassalsPlayer) o;
            if(this.uuid.equals(pl.uuid)) {
                return true;
            }
        }
        return false;
    }
}
