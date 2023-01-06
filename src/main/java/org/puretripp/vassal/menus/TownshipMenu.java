package org.puretripp.vassal.menus;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BannerMeta;
import org.bukkit.plugin.Plugin;
import org.puretripp.vassal.main.Main;
import org.puretripp.vassal.types.Nation;
import org.puretripp.vassal.types.townships.Township;

public class TownshipMenu extends Menu implements Listener {
    Township town;
    private Plugin pl = Main.getPlugin(Main.class);
    public TownshipMenu(Township town) {
        super("Town Menu");
        this.town = town;
    }


    @EventHandler
    public void onInventoryClick(final InventoryClickEvent e) {
        if (e.getInventory().equals(inv)) {

        }
    }
}