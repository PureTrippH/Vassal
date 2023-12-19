package org.puretripp.vassal.events;

import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.puretripp.vassal.utils.general.VassalWorld;
import org.puretripp.vassal.utils.general.VassalsPlayer;

public class MenuHelperEvents {
    public void menuClose(InventoryCloseEvent e) {
        VassalsPlayer vp = VassalWorld.getWorldInstance().onlinePlayers.get(e.getPlayer().getUniqueId());
        if (vp == null) return;
        vp.popMenu();
    }
}
