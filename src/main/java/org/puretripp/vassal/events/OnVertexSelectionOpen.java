package org.puretripp.vassal.events;

import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.puretripp.vassal.types.Nation;
import org.puretripp.vassal.utils.claiming.Residence;
import org.puretripp.vassal.utils.general.VassalsPlayer;

public class OnVertexSelectionOpen extends Event implements Cancellable {
    private Residence subClaim;
    private VassalsPlayer player;

    private boolean isCancelled;
    private static final HandlerList handlers = new HandlerList();

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

    public OnVertexSelectionOpen(Residence subClaim, VassalsPlayer player) {
        this.subClaim = subClaim;
        this.player = player;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }
}
