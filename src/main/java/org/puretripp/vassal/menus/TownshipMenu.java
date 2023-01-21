package org.puretripp.vassal.menus;

import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BannerMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.Plugin;
import org.puretripp.vassal.main.Main;
import org.puretripp.vassal.types.Nation;
import org.puretripp.vassal.types.ranks.TownRanks;
import org.puretripp.vassal.types.townships.Township;
import org.puretripp.vassal.utils.VassalWorld;
import org.puretripp.vassal.utils.VassalsPlayer;

import java.util.UUID;

public class TownshipMenu extends Menu implements Listener {
    private Township town;
    private Plugin pl = Main.getPlugin(Main.class);
    private VassalsPlayer vp;

    public TownshipMenu(Township town, VassalsPlayer vp) {
        super("Town Menu");
        this.town = town;
        this.vp = vp;
        populateItems();
    }

    public void populateItems() {
        if(vp.getRank(town) == TownRanks.LEADER) {
            super.contents.add(Menu.generateItem(Material.IRON_BARS, (ChatColor.GREEN + "Permissions"),  "Permissions"));
            super.contents.add(Menu.generateItem(Material.QUARTZ_PILLAR, (ChatColor.GREEN + "Government"),  "Government"));
            super.contents.add(Menu.generateItem(Material.GOLD_NUGGET, (ChatColor.GREEN + "Treasury"),  "Treasury"));
        }
        super.refreshContents();
    }


    @EventHandler
    public void onInventoryClick(final InventoryClickEvent e) {
        if (e.getInventory().equals(inv)) {
            ItemStack selected = e.getCurrentItem();
            ItemMeta meta = selected.getItemMeta();
            PersistentDataContainer data = meta.getPersistentDataContainer();
            NamespacedKey clickFunc = new NamespacedKey(Main.getPlugin(Main.class), "iconClickFunction");
            if (data.has(clickFunc, PersistentDataType.STRING)) {
                String val = data.get(clickFunc, PersistentDataType.STRING);
                switch (val) {
                    case ("Permissions"):
                            e.getWhoClicked().openInventory(new PermsMenu(
                        false, vp.getNation(), vp.getSelected()).getInv());
                        break;
                    case ("Government"):
                        break;
                    case ("Treasury"):
                        break;

                }
            }
        }
    }
}
