package org.puretripp.vassal.menus;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.puretripp.vassal.utils.SubCommand;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class HelpMenu extends Menu {

    public HelpMenu(HashMap<String, SubCommand > commands) {
        super( "Help Menu");
        populateItems(commands);
    }

    public void populateItems(HashMap<String, SubCommand> commands) {
        for (SubCommand value : commands.values()) {
            if (value.getDesc() != null) {
                List<String> desc = Arrays.asList(value.getDesc().split("\n"));
                ItemStack it = Menu.generateItem(Material.LIME_CANDLE, ChatColor.GREEN + value.getName(), "HelpIcon", desc);
                super.contents.add(it);
            } else {
                ItemStack it = Menu.generateItem(Material.LIME_CANDLE, ChatColor.GREEN + value.getName(), "HelpIcon");
                super.contents.add(it);
            }
        }
        refreshContents();
    }

    @EventHandler
    public void invClick(final InventoryClickEvent e) {
        if (e.getInventory().equals(inv)) {
            if (e.getCurrentItem() != null) {
                e.setCancelled(true);
            }
        }
    }
}
