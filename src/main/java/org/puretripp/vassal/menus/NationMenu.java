package org.puretripp.vassal.menus;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BannerMeta;
import org.bukkit.plugin.Plugin;
import org.puretripp.vassal.main.Main;
import org.puretripp.vassal.types.Nation;
import org.puretripp.vassal.utils.general.VassalsPlayer;

public class NationMenu extends Menu {
    Nation n;
    private Plugin pl = Main.getPlugin(Main.class);

    public NationMenu(Nation n) {
        super( "Nation Panel");
        this.n = n;
        populateItems();
    }

    public void populateItems() {
        ItemStack banner = new ItemStack(Material.WHITE_BANNER);
        if (n != null) {
            if(n.getBanner() != null) {
                banner.setType(n.getBannerType());
                banner.setItemMeta(n.getBanner());
            }
        }
        super.contents.add(banner);
        refreshContents();
    }

    @EventHandler
    public void invClick(final InventoryClickEvent e) {
        if (!e.getInventory().equals(inv)) return;
        if (e.getCurrentItem() == null) return;
        if (e.getCurrentItem().getItemMeta() instanceof BannerMeta) {
            final Player p = (Player) e.getWhoClicked();
            if (n != null) {
                n.setBannerType(e.getCurrentItem().getType());
                n.setBanner((BannerMeta) e.getCurrentItem().getItemMeta());
                p.sendMessage("Nation Banner Set");
            } else {
                p.sendMessage("MUST BE IN A NATION TO DO THAT!");
            }
            p.updateInventory();
            p.openInventory(inv);
        }
        e.setCancelled(true);
    }

}
