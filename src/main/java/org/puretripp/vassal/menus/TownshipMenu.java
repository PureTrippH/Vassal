package org.puretripp.vassal.menus;

import org.bukkit.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.Plugin;
import org.puretripp.vassal.main.Main;
import org.puretripp.vassal.menus.submenus.PermsMenu;
import org.puretripp.vassal.menus.submenus.SubclaimMenu;
import org.puretripp.vassal.types.townships.Township;
import org.puretripp.vassal.types.Residence;
import org.puretripp.vassal.utils.MenuIcon;
import org.puretripp.vassal.utils.general.VassalsPlayer;

public class TownshipMenu extends Menu implements Listener {
    private Township town;
    private Plugin pl = Main.getPlugin(Main.class);
    private VassalsPlayer vp;
    private NamespacedKey clickFunc = new NamespacedKey(Main.getPlugin(Main.class), "iconClickFunction");

    public TownshipMenu(Township town, VassalsPlayer vp) {
        super("Town Menu");
        this.town = town;
        this.vp = vp;
        populateItems();
    }

    public void populateItems() {
        if(town.getPlayerRank(vp.getUUID()).isCanManagePolitics()) {
            super.contents.add(new MenuIcon(MenuIcon.generateItem(Material.IRON_BARS,
                    (ChatColor.GREEN + "Permissions"),  "Permissions"), () -> {
                vp.pushMenu(new PermsMenu(
                false, vp.getNation(), vp.getSelected(), vp));
            }));
            super.contents.add(new MenuIcon(MenuIcon.generateItem(Material.RED_BED,
                    (ChatColor.GREEN + "Residences"),  "Residences"), () -> {
                vp.pushMenu(new ResidenceList(town, vp));
            }));
            super.contents.add(new MenuIcon(MenuIcon.generateItem(Material.QUARTZ_PILLAR,
                    (ChatColor.GREEN + "Government"),  "Government")));
            super.contents.add(new MenuIcon(MenuIcon.generateItem(Material.GOLD_NUGGET,
                    (ChatColor.GREEN + "Treasury"),  "Treasury")));
        }
        super.refreshContents();
    }

    private class ResidenceList extends Menu {
        Township town;
        VassalsPlayer vp;
        public ResidenceList(Township town, VassalsPlayer vp) {
            super("Residence List");
            this.town = town;
            this.vp = vp;
            populateItems();
        }
        public void populateItems() {
            for (int i = 0; i < town.getAllResidences().size(); i++) {
                Residence res = town.getAllResidences().get(i);
                int finalI = i;
                super.contents.add(new MenuIcon(MenuIcon.generateItem(Material.GREEN_BED, (ChatColor.GREEN + res.getName()),
                    "residence_" + i), () -> {
                    int index = finalI;
                    Bukkit.getPlayer(vp.getUUID()).openInventory(new SubclaimMenu(town.getAllResidences().get(index), town, vp).getInv());
                }));
            }
            super.refreshContents();
        }
    }
}
