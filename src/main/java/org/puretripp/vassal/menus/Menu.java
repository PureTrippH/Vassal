package org.puretripp.vassal.menus;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.Plugin;
import org.bukkit.profile.PlayerProfile;
import org.puretripp.vassal.main.Main;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.UUID;

/**
 * A generic GUI that a player can interact with.
 * @author Tripp H.
 * @version 1.0
 */
public class Menu implements Listener {
    protected final Inventory inv;
    private Plugin pl = Main.getPlugin(Main.class);
    protected ArrayList<ItemStack> contents = new ArrayList<ItemStack>();
    protected ArrayList<Menu> subMenus = new ArrayList<Menu>();
    private int page = 1;

    /**
     * Creates a menu with multiple pages.
     * @param name String name of inventory.
     * @param page int page number.
     */
    public Menu(String name, int page) {
        inv = Bukkit.createInventory(null, 9*3, name);
        initializeItems(page);
        pl.getServer().getPluginManager().registerEvents(this, pl);
        Bukkit.getLogger().info(ChatColor.RED + "VASSALS: 1 Clicked!!!");
    }

    /**
     * Creates a One Page Menu
     * @param name String name of menu
     */
    public Menu(String name) {
        this(name, 1);
    }



    /**
     * Primes inventory with placeholders and next Page
     */
    public void initializeItems(int page) {
        initializeItems(page, false);
    }
    public void initializeItems(int page, boolean disablePages) {
        //Populates Void Slots
        for (int i = 0; i < 27; i++) {
            if (i / 9 != 1) {
                inv.setItem(i, new ItemStack(Material.BLACK_STAINED_GLASS_PANE));
            }
        }
        refreshContents();
        if(!disablePages) {
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
    }

    protected static ItemStack generateItem(Material m, String name, String value, List<String> lore) {
        ItemStack newIcon = new ItemStack(m);
        ItemMeta meta = newIcon.getItemMeta();
        meta.setDisplayName(name);
        meta.setLore(lore);
        NamespacedKey clickFunc = new NamespacedKey(Main.getPlugin(Main.class), "iconClickFunction");
        PersistentDataContainer data = meta.getPersistentDataContainer();
        data.set(clickFunc, PersistentDataType.STRING, value);
        if (lore != null) {
            meta.setLore(lore);
        }
        newIcon.setItemMeta(meta);
        return newIcon;
    }

    protected static ItemStack generateSkull(String name, String value, List<String> lore,
                                             UUID playerName) {
        OfflinePlayer p = Bukkit.getOfflinePlayer(playerName);
        ItemStack newIcon = new ItemStack(Material.PLAYER_HEAD);
        SkullMeta meta = (SkullMeta) newIcon.getItemMeta();
        meta.setDisplayName(name);
        meta.setLore(lore);
        meta.setOwner(p.getName());
        NamespacedKey clickFunc = new NamespacedKey(Main.getPlugin(Main.class), "iconClickFunction");
        PersistentDataContainer data = meta.getPersistentDataContainer();
        data.set(clickFunc, PersistentDataType.STRING, value);
        if (lore != null) {
            meta.setLore(lore);
        }
        newIcon.setItemMeta(meta);
        return newIcon;
    }
    protected static ItemStack generateCustomSkull(String name, String value, List<String> lore,
                                             String texture) {
        ItemStack item = new ItemStack(Material.PLAYER_HEAD, 1);
        SkullMeta itemMeta = (SkullMeta) item.getItemMeta();
        String url = ("http://textures.minecraft.net/texture/" + texture);
        GameProfile profile = new GameProfile(UUID.randomUUID(), null);
        byte[] encodedData = Base64.getEncoder().encode(String.format("{textures:{SKIN:{url:\"%s\"}}}", url).getBytes());
        profile.getProperties().put("textures", new Property("textures", new String(encodedData)));
        Field profileField = null;
        try
        {
            profileField = itemMeta.getClass().getDeclaredField("profile");
            profileField.setAccessible(true);
            profileField.set(itemMeta, profile);
        }
        catch (NoSuchFieldException | IllegalArgumentException | IllegalAccessException e)
        {
            Bukkit.getLogger().info(ChatColor.RED + e.getMessage());
        }
        item.setItemMeta(itemMeta);
        NamespacedKey clickFunc = new NamespacedKey(Main.getPlugin(Main.class), "iconClickFunction");
        PersistentDataContainer data = itemMeta.getPersistentDataContainer();
        data.set(clickFunc, PersistentDataType.STRING, value);
        itemMeta.setDisplayName(name);
        if (lore != null) {
            itemMeta.setLore(lore);
        }
        item.setItemMeta(itemMeta);
        System.out.println(item);
        return item;
    }


    protected static ItemStack generateItem(Material m, String name, String value) {
        return generateItem(m, name, value, new ArrayList<String>());
    }

    protected void refreshContents() {
        for (int i = 0 + (7*(page-1)); i < 7 + (7*(page-1)); i++) {
            if (i < contents.size()) {
                inv.setItem(10 + (i - (7*(page-1))), contents.get(i));
            }
        }

    }



    @EventHandler
    public void onInvClick(final InventoryClickEvent e) {
        if (e.getInventory().equals(inv)) {
            final ItemStack clickedItem = e.getCurrentItem();

            // verify current item is not null
            if (clickedItem == null || clickedItem.getType().isAir()) return;

            final Player p = (Player) e.getWhoClicked();
            if (e.getRawSlot() == 9 && page > 1) {
                --page;
                inv.clear();
                initializeItems(page);
            }
            if(e.getRawSlot() == 17 && page <= Math.ceil(contents.size()/7)) {
                ++page;
                inv.clear();
                initializeItems(page);
            }
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onInvDrag(final InventoryDragEvent e) {
        if (e.getInventory().equals(inv)) {
            e.setCancelled(true);
        }
    }

    public Inventory getInv() { return inv; }

}
