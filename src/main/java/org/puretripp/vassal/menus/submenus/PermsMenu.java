package org.puretripp.vassal.menus.submenus;

import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.puretripp.vassal.main.Main;
import org.puretripp.vassal.menus.Menu;
import org.puretripp.vassal.types.Nation;
import org.puretripp.vassal.types.ranks.TownRanks;
import org.puretripp.vassal.types.townships.Township;
import org.puretripp.vassal.utils.claiming.perms.PermClass;
import org.puretripp.vassal.utils.general.VassalWorld;
import org.puretripp.vassal.utils.general.VassalsPlayer;

import java.util.UUID;

public class PermsMenu extends Menu {
    private Township town;
    private Nation nation;
    private VassalsPlayer target;

    public PermsMenu(boolean isNation, Nation nation, Township town, VassalsPlayer target) {
        super("Permissions Menu");
        this.nation = nation;
        this.town = town;
        this.target = target;
        populateItems();
    }

    public void populateItems() {
        ItemStack add = Menu.generateCustomSkull(ChatColor.GREEN + "Create Rank", "createMenu",
                null, "790f62ec5fa2e93e67cf1e00db8af4b47ac7ac769aa09a203a1f575a12710b10");
        ItemStack list = Menu.generateCustomSkull(ChatColor.GREEN + "Create Rank", "createMenu",
                null, "8a16038bc8e6518afa91498dab7675c01cb31a125d21c49b861294d39e1c560c");
        ItemStack promote = Menu.generateCustomSkull(ChatColor.GREEN + "Promote Players", "createMenu",
                null, "1c8e0cfebc7f9c7e16fbaaae025d1b1d19d5ee633666bcf25fa0b40d5bd21bcd");
        super.contents.add(add);
        super.contents.add(list);
        super.contents.add(promote);
        super.refreshContents();
    }


    private class PlayerList extends Menu {
        private Township town;
        private Nation nation;
        private VassalsPlayer target;
        private NamespacedKey key = new NamespacedKey(Main.getPlugin(Main.class), "iconClickFunction");

        public PlayerList(boolean isNation, Nation n, Township t) {
            super("Permission Menu");
            this.nation = n;
            this.town = t;
            populateItems();
        }

        public void populateItems() {
            for(UUID uuid : town.getPlayers()) {
                OfflinePlayer p = Bukkit.getOfflinePlayer(uuid);
                super.contents.add(Menu.generateSkull(ChatColor.RED + p.getName(), ("Perms_" + p.getUniqueId()),
                        null, uuid));
            }
            super.refreshContents();
        }

        @EventHandler
        public void invClick(final InventoryClickEvent e) {
            if (e.getInventory().equals(inv)) {
                if (e.getCurrentItem() != null) {
                    if (e.getCurrentItem().getType() == Material.PLAYER_HEAD) {
                        SkullMeta meta = (SkullMeta) e.getCurrentItem().getItemMeta();
                        PersistentDataContainer data = meta.getPersistentDataContainer();
                        if((data.get(key, PersistentDataType.STRING).substring(0, 6)).equals("Perms_")) {
                            e.getWhoClicked().sendMessage(data.get(key, PersistentDataType.STRING));
                            OfflinePlayer p = Bukkit.getOfflinePlayer(
                                    UUID.fromString(data.get(key, PersistentDataType.STRING).substring(6)));
                            target = VassalWorld.onlinePlayers.get(e.getWhoClicked().getUniqueId());
                        }
                    }
                }
            }
        }
    }
}