package org.puretripp.vassal.menus;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.plugin.Plugin;
import org.bukkit.profile.PlayerProfile;
import org.puretripp.vassal.main.Main;

import java.util.ArrayList;
import java.util.UUID;

/**
 * A generic GUI that
 * @author Tripp H.
 * @version 1.0
 */
public abstract class Menu<T> implements Listener {
    protected final Inventory inv;
    private Plugin pl = Main.getPlugin(Main.class);
    protected ArrayList<T> contents = new ArrayList<T>();
    private int page = 1;

    public Menu(String name) {
        inv = Bukkit.createInventory(null, 9*3, name);
        initializeItems();
        pl.getServer().getPluginManager().registerEvents(this, pl);
    }


    /**
     * Primes inventory with placeholders and next Page
     */
    public void initializeItems() {
        for (int i = 0; i < 27; i++) {
            if (i / 9 != 1) {
                inv.setItem(i, new ItemStack(Material.BLACK_STAINED_GLASS_PANE));
            }
        }
        ItemStack leftArrow = new ItemStack(Material.PLAYER_HEAD);
        SkullMeta leftMeta = (SkullMeta) leftArrow.getItemMeta();
        leftMeta.setDisplayName(ChatColor.RED + "<- BACK");
        leftMeta.setOwner("MHF_ArrowLeft");
        leftArrow.setItemMeta(leftMeta);
        inv.setItem(9, leftArrow);
        ItemStack rightArrow = new ItemStack(Material.PLAYER_HEAD);
        SkullMeta rightMeta = (SkullMeta) leftArrow.getItemMeta();
        rightMeta.setDisplayName(ChatColor.GREEN + "NEXT ->");
        rightMeta.setOwner("MHF_ArrowRight");
        rightArrow.setItemMeta(rightMeta);
        inv.setItem(17, rightArrow);
    }

    @EventHandler
    public void onInventoryClick(final InventoryClickEvent e) {
        if (!e.getInventory().equals(inv)) return;

        e.setCancelled(true);

        final ItemStack clickedItem = e.getCurrentItem();

        // verify current item is not null
        if (clickedItem == null || clickedItem.getType().isAir()) return;

        final Player p = (Player) e.getWhoClicked();
        if(e.getRawSlot() == 9 && page > 1) {
            --page;
        }
        if(e.getRawSlot() == 17 && page > contents.size()/7) {
            ++page;
        }
    }

    @EventHandler
    public void onInventoryClick(final InventoryDragEvent e) {
        if (e.getInventory().equals(inv)) {
            e.setCancelled(true);
        }
    }

    public Inventory getInv() { return inv; }

}
