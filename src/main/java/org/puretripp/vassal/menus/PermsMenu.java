package org.puretripp.vassal.menus;

import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;
import org.puretripp.vassal.main.Main;
import org.puretripp.vassal.types.Nation;
import org.puretripp.vassal.types.townships.Township;

import java.util.UUID;

public class PermsMenu extends Menu {
    Township town;
    Nation nation;
    public PermsMenu(boolean isNation, Nation n, Township t) {
        super("Permission Menu");
        this.nation = n;
        this.town = t;
    }

    public void populateItems() {
        NamespacedKey key = new NamespacedKey(Main.getPlugin(Main.class), "permsMenuSubject");
        for(UUID uuid : town.getPlayers()) {
            OfflinePlayer p = Bukkit.getOfflinePlayer(uuid);
            ItemStack subject = new ItemStack(Material.PLAYER_HEAD);
            SkullMeta leftMeta = (SkullMeta) subject.getItemMeta();
            leftMeta.setDisplayName(ChatColor.RED + p.getName());
            leftMeta.setOwner(p.getName());
            super.contents.add(subject);
        }
        super.refreshContents();
    }
}
