package org.puretripp.vassal.menus;

import org.bukkit.*;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.lustrouslib.menu.Menu;
import org.lustrouslib.menu.MenuIcon;
import org.lustrouslib.wrapper.StateHandler;
import org.puretripp.vassal.main.Main;
import org.puretripp.vassal.menus.submenus.PermsMenu;
import org.puretripp.vassal.menus.submenus.SubclaimMenu;
import org.puretripp.vassal.types.townships.Township;
import org.puretripp.vassal.types.Residence;
import org.puretripp.vassal.utils.general.VassalsPlayer;

public class TownshipMenu extends Menu implements Listener {
    private Township town;
    private StateHandler<VassalsPlayer> state;
    private Plugin pl = Main.getPlugin(Main.class);
    private VassalsPlayer vp;
    private NamespacedKey clickFunc = new NamespacedKey(Main.getPlugin(Main.class), "iconClickFunction");

    public TownshipMenu(Township town, VassalsPlayer vp, StateHandler state) {
        super("Town Menu", vp, state.getPlugin());
        this.town = town;
        this.state = state;
        this.vp = vp;
        populateItems();
    }

    public void populateItems() {
        if(town.getPlayerRank(vp.getUUID()).isCanManagePolitics()) {
            super.contents.add(new MenuIcon(MenuIcon.generateItem(Material.IRON_BARS,
                    (ChatColor.GREEN + "Permissions"),  "Permissions"), () -> {
                vp.pushMenu(new PermsMenu(
                false, vp.getNation(), vp.getSelected(), vp, state));
            }));
            super.contents.add(new MenuIcon(MenuIcon.generateItem(Material.RED_BED,
                    (ChatColor.GREEN + "Residences"),  "Residences"), () -> {
                vp.pushMenu(new ResidenceList(town, vp, state));
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
        public ResidenceList(Township town, VassalsPlayer vp, StateHandler state) {
            super("Residence List", vp, state.getPlugin());
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
                    vp.pushMenu(new SubclaimMenu(town.getAllResidences().get(index), town, vp, state));
                }));
            }
            super.refreshContents();
        }
    }

    private class GovernnmentTab extends Menu {
        Township town;
        VassalsPlayer vp;
        public GovernnmentTab(Township town, VassalsPlayer vp, StateHandler state) {
            super("Governance", vp, state.getPlugin());
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
                    vp.pushMenu(new SubclaimMenu(town.getAllResidences().get(index), town, vp, state));
                }));
            }
            super.refreshContents();
        }
    }
}
