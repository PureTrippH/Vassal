package org.puretripp.vassal.menus.submenus;

import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.Sound;
import org.bukkit.conversations.Conversation;
import org.bukkit.conversations.Prompt;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.puretripp.vassal.events.OnVertexSelectionOpen;
import org.puretripp.vassal.main.Main;
import org.puretripp.vassal.menus.Menu;
import org.puretripp.vassal.types.Nation;
import org.puretripp.vassal.types.townships.Township;
import org.puretripp.vassal.utils.claiming.Residence;
import org.puretripp.vassal.utils.claiming.ResidenceType;
import org.puretripp.vassal.utils.general.VassalsPlayer;

import java.util.ArrayList;
import java.util.Arrays;

public class SubclaimMenu extends Menu {
    private Residence subclaim;
    private VassalsPlayer vp;
    private Township town;


    public SubclaimMenu(Residence subclaim, Township town, VassalsPlayer vp) {
        super("Subclaim Manager");
        this.subclaim = subclaim;
        this.vp = vp;
        this.town = town;
        populateItems();
    }

    public void populateItems() {
        super.contents.add(Menu.generateItem(Material.SPRUCE_SIGN, ChatColor.GREEN + subclaim.getName(),
                "setName"));
        super.contents.add(Menu.generateItem(Material.TRIPWIRE_HOOK, ChatColor.GREEN + "Set Border",
                "setBorder"));
        super.contents.add(Menu.generateItem(Material.OBSERVER, ChatColor.GREEN + "Show Border",
                "showBorder"));
        super.contents.add(Menu.generateItem(Material.WRITABLE_BOOK, ChatColor.GREEN + "Set Owner",
                "setOwner"));
        super.contents.add(Menu.generateItem(Material.PHANTOM_MEMBRANE, ChatColor.GREEN + "Current Height: " + subclaim.getMaxHeight(),
                "setHeight", Arrays.asList(new String[]{ChatColor.GREEN + "Left Click to Increase",
                        ChatColor.RED + "Right Click to Decrease"})));
        super.contents.add(Menu.generateItem(Material.GREEN_BANNER, ChatColor.GREEN + "Save Claim",
                "save"));

        super.refreshContents();
    }

    @EventHandler
    public void invClick(final InventoryClickEvent e) {
        if (!e.getInventory().equals(inv)) return;
        if (e.getCurrentItem() == null) return;
        ItemMeta meta = e.getCurrentItem().getItemMeta();
        Player player = (Player) e.getWhoClicked();
        PersistentDataContainer data = meta.getPersistentDataContainer();
        NamespacedKey key = new NamespacedKey(Main.getPlugin(Main.class), "iconClickFunction");
        String funcData = data.get(key, PersistentDataType.STRING);
        player.sendMessage(funcData);
        switch(funcData) {
            case "showBorder":
                subclaim.display(vp, player);
                break;
            case "setName":
                player.sendTitle(ChatColor.GREEN + "Rename Mode Enabled",
                        ChatColor.GRAY +"Type New Name in Chat", 30, 30, 30);
            case "setBorder":
                vp.setSelectionMode(subclaim);
                OnVertexSelectionOpen playerSelect = new OnVertexSelectionOpen(subclaim, vp);
                Bukkit.getPluginManager().callEvent(playerSelect);
                player.closeInventory();
                player.playSound(player, Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1, 2);
                player.sendTitle(ChatColor.GREEN + "Selection Mode Enabled",
                        ChatColor.GRAY +"Right Click To Set a Vertex", 30, 30, 30);
                player.sendMessage(ChatColor.GREEN + "Selection Mode Enabled");
                player.sendMessage(ChatColor.GRAY + "Right Click To Set a Vertex");
                player.sendMessage(ChatColor.GRAY + "Left Click to Exit");
                break;
            case "setHeight":
                if (e.getClick() == ClickType.LEFT) {
                    subclaim.setMaxHeight(subclaim.getMaxHeight() + 1);
                    player.sendMessage("Fired! " + subclaim.getMaxHeight());
                } else if (e.getClick() == ClickType.RIGHT) {
                    subclaim.setMaxHeight(subclaim.getMaxHeight() - 1);
                }
                player.closeInventory();
                refreshContents();
                player.updateInventory();
                player.openInventory(inv);
                break;
            case "save":
                try {
                    if (subclaim.polygonSize() <= 2 || subclaim.getMaxHeight() == 0) {
                        throw new IllegalArgumentException("Please Fill Out All Fields");
                    }
                    vp.getSelected().addResidence(subclaim);
                    player.sendMessage(ChatColor.RED + vp.getSelected().getName());
                    player.closeInventory();
                    player.playSound(player, Sound.ENTITY_PLAYER_LEVELUP, 1f, 0.5f);
                    player.sendTitle(ChatColor.GREEN + "Residence Saved!",
                            ChatColor.GRAY +"You May Now See it In the Town Menu", 30, 30, 30);
                    vp.clearTasks();
                } catch (Exception err) {
                    player.sendMessage(ChatColor.RED + err.getMessage());
                }
                break;
        }
        e.setCancelled(true);
    }
}
