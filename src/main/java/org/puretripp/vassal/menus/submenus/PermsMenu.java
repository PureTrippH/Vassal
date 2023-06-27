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
import org.puretripp.vassal.utils.general.VassalWorld;
import org.puretripp.vassal.utils.general.VassalsPlayer;

import java.util.UUID;

public class PermsMenu extends Menu {
    private Township town;
    private Nation nation;
    private VassalsPlayer target;
    private NamespacedKey key = new NamespacedKey(Main.getPlugin(Main.class), "iconClickFunction");

    public PermsMenu(boolean isNation, Nation n, Township t) {
        super("Permission Menu");
        this.nation = n;
        this.town = t;
        populateItems();
    }

    public void openPerms(VassalsPlayer vp, Player player) {
        player.closeInventory();
        inv.clear();
        ItemStack promote = new ItemStack(Material.PLAYER_HEAD);
        SkullMeta metaPromote = (SkullMeta) promote.getItemMeta();
        metaPromote.setDisplayName(ChatColor.GREEN + "Promote");
        metaPromote.setOwner("MHF_ArrowUp");
        promote.setItemMeta(metaPromote);

        ItemStack demote = new ItemStack(Material.PLAYER_HEAD);
        SkullMeta metaDemote = (SkullMeta) promote.getItemMeta();
        metaDemote.setOwner("MHF_ArrowDown");
        metaDemote.setDisplayName(ChatColor.GREEN + "Demote");
        demote.setItemMeta(metaDemote);

        OfflinePlayer p = Bukkit.getOfflinePlayer(vp.getUUID());
        ItemStack subject = new ItemStack(Material.PLAYER_HEAD);
        SkullMeta meta = (SkullMeta) subject.getItemMeta();
        meta.setDisplayName(ChatColor.RED + vp.getRank(town).toString());
        meta.setOwner(p.getName());
        subject.setItemMeta(meta);
        super.contents.clear();
        super.initializeItems(-1, true);
        super.inv.setItem(11, promote);
        super.inv.setItem(15, demote);
        super.inv.setItem(13, subject);
        player.updateInventory();
        player.openInventory(super.inv);
    }

    public void populateItems() {
        for(UUID uuid : town.getPlayers()) {
            OfflinePlayer p = Bukkit.getOfflinePlayer(uuid);
            ItemStack subject = new ItemStack(Material.PLAYER_HEAD);
            SkullMeta meta = (SkullMeta) subject.getItemMeta();
            meta.setDisplayName(ChatColor.RED + p.getName());
            PersistentDataContainer pd = meta.getPersistentDataContainer();
            pd.set(key, PersistentDataType.STRING, "Perms_" +p.getUniqueId());
            meta.setOwner(p.getName());
            subject.setItemMeta(meta);
            super.contents.add(subject);
        }
        super.refreshContents();
    }

    @EventHandler
    public void invClick(final InventoryClickEvent e) {
        if (e.getInventory().equals(inv)) {
            if (e.getCurrentItem() != null) {
                if (e.getCurrentItem().getType() == Material.PLAYER_HEAD) {
                    SkullMeta meta = (SkullMeta) e.getCurrentItem().getItemMeta();
                    switch(meta.getOwner()) {
                        case "MHF_ArrowUp":
                            if(target.getRank(town) == TownRanks.LEADER) {
                                target.changeRank(town, TownRanks.LEADER);
                            } else {
                                target.changeRank(town, TownRanks.intToEnum(target.getRank(town).getValue() + 1));
                            }
                                openPerms(target, (Player) e.getWhoClicked());
                            break;
                        case "MHF_ArrowDown":
                            if(target.getRank(town) == TownRanks.CITIZEN) {
                                target.changeRank(town, TownRanks.CITIZEN);
                            } else {
                                target.changeRank(town, TownRanks.intToEnum(target.getRank(town).getValue() - 1));
                            }
                            openPerms(target, (Player) e.getWhoClicked());
                            break;
                        default:
                            PersistentDataContainer data = meta.getPersistentDataContainer();
                            if((data.get(key, PersistentDataType.STRING).substring(0, 6)).equals("Perms_")) {
                                e.getWhoClicked().sendMessage(data.get(key, PersistentDataType.STRING));
                                OfflinePlayer p = Bukkit.getOfflinePlayer(
                                        UUID.fromString(data.get(key, PersistentDataType.STRING).substring(6)));
                                target = VassalWorld.onlinePlayers.get(e.getWhoClicked().getUniqueId());
                                openPerms(target, (Player) e.getWhoClicked());
                            }
                            break;
                    }
                }
            }
        }
    }
}
