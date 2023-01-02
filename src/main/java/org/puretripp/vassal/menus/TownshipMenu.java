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
        populateItems();
    }

    public void populateItems() {
        ItemStack banner = new ItemStack(Material.WHITE_BANNER);
        Nation n = town.getNation();
        if (n != null) {
            if(n.getBanner() != null) {
                banner.setItemMeta(n.getBanner());
            }
        }
        super.inv.setItem(11, banner);
    }

    @EventHandler
    public void onInventoryClick(final InventoryClickEvent e) {
        if (e.getInventory().equals(inv)) {
            if (e.getCurrentItem().getItemMeta() instanceof BannerMeta) {
                final Player p = (Player) e.getWhoClicked();
                Nation n = town.getNation();
                if(n != null) {
                    n.setBannerType(e.getCurrentItem().getType());
                    n.setBanner((BannerMeta) e.getCurrentItem().getItemMeta());
                    p.sendMessage("Nation Banner Set");
                } else {
                    p.sendMessage("MUST BE IN A NATION TO DO THAT!");
                }
            }
            e.setCancelled(true);
        }
    }
}