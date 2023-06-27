package org.puretripp.vassal.menus;

import org.bukkit.*;
import org.bukkit.entity.Player;
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
import org.puretripp.vassal.types.Vassal;
import org.puretripp.vassal.types.ranks.TownRanks;
import org.puretripp.vassal.types.townships.Township;
import org.puretripp.vassal.utils.claiming.Residence;
import org.puretripp.vassal.utils.claiming.ResidenceType;
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
        if(vp.getRank(town) == TownRanks.LEADER) {
            super.contents.add(Menu.generateItem(Material.IRON_BARS, (ChatColor.GREEN + "Permissions"),  "Permissions"));
            super.contents.add(Menu.generateItem(Material.RED_BED, (ChatColor.GREEN + "Residences"),  "Residences"));
            super.contents.add(Menu.generateItem(Material.QUARTZ_PILLAR, (ChatColor.GREEN + "Government"),  "Government"));
            super.contents.add(Menu.generateItem(Material.GOLD_NUGGET, (ChatColor.GREEN + "Treasury"),  "Treasury"));
        }
        super.refreshContents();
    }

    @EventHandler
    public void onInventoryClick(final InventoryClickEvent e) {
        if (e.getInventory().equals(inv) && e.getCurrentItem() != null) {
            ItemStack selected = e.getCurrentItem();
            ItemMeta meta = selected.getItemMeta();
            PersistentDataContainer data = meta.getPersistentDataContainer();
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
                    case ("Residences"):
                        e.getWhoClicked().openInventory(new ResidenceList(
                        town, vp).getInv());
                        break;
                }
            }
        }
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
                super.contents.add(Menu.generateItem(Material.GREEN_BED, (ChatColor.GREEN + res.getName()),
                    "residence_" + i));
            }
            super.refreshContents();
        }

        @EventHandler
        public void onInventoryClick(final InventoryClickEvent e) {
            if (e.getInventory().equals(inv)) {
                if ((e.getCurrentItem()) == null) return;
                ItemMeta meta = e.getCurrentItem().getItemMeta();
                PersistentDataContainer data = meta.getPersistentDataContainer();
                String inviteData = data.get(clickFunc, PersistentDataType.STRING);
                if((inviteData.substring(0, 10)).equals("residence_")) {
                    int index = Integer.parseInt(inviteData.substring(10));
                    Bukkit.getPlayer(vp.getUUID())
                        .openInventory(new SubclaimMenu(town.getAllResidences().get(index), town, vp).getInv());
                }
            }
        }
    }
}
