package org.puretripp.vassal.events;

import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.puretripp.vassal.types.Nation;
import org.puretripp.vassal.utils.VassalsPlayer;

public class OnNationCreation extends Event implements Cancellable {
    private boolean isCancelled;
    private static final HandlerList handlers = new HandlerList();

    private Nation nation;
    private VassalsPlayer player;

    @Override
    public boolean isCancelled() {
        return isCancelled;
    }

    @Override
    public void setCancelled(boolean cancelled) {
        this.isCancelled = cancelled;
    }

    public HandlerList getHandlers() {
        return handlers;
    }

    public OnNationCreation(Nation nation, VassalsPlayer player) {
        this.nation = nation;
        this.player = player;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

}
