package org.puretripp.vassal.menus;

import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.puretripp.vassal.main.Main;
import org.puretripp.vassal.types.townships.Township;
import org.puretripp.vassal.utils.general.VassalsPlayer;

public class PlayerMenu extends Menu {
    VassalsPlayer you;
    public PlayerMenu(VassalsPlayer vp) {
        super("You:");
        this.you = vp;
        populateItems();
    }

    public void populateItems() {
        super.contents.add(Menu.generateItem(Material.BOOKSHELF, (ChatColor.GREEN + "Invites"),  "Invites"));
        refreshContents();
    }

    @EventHandler
    public void onInventoryClick(final InventoryClickEvent e) {
        if (e.getInventory().equals(inv)) {
            ItemStack selected = e.getCurrentItem();
            ItemMeta meta = selected.getItemMeta();
            PersistentDataContainer data = meta.getPersistentDataContainer();
            NamespacedKey clickFunc = new NamespacedKey(Main.getPlugin(Main.class), "iconClickFunction");
            if (data.has(clickFunc, PersistentDataType.STRING)) {
                String val = data.get(clickFunc, PersistentDataType.STRING);
                switch (val) {
                    case ("Invites"):
                        e.getWhoClicked().openInventory(new InvitesMenu(you).inv);
                        break;
                }
            }
        }
    }

    /**
     * Private Menu for the Town Invites Tab
     */
    private class InvitesMenu extends Menu {
        VassalsPlayer you;
        public InvitesMenu(VassalsPlayer vp) {
            super("Your Invites:");
            this.you = vp;
            this.populateItems();
        }
        public void populateItems() {
            Township[] invites = you.getInvites();
            for (int i = 0; i < you.getInvites().length; i++) {
                super.contents.add(Menu.generateItem(Material.PAPER, (ChatColor.GREEN + invites[i].getName()),  "Invite_" + i));
            }
            refreshContents();
        }
        @EventHandler
        public void onInventoryClick(final InventoryClickEvent e) {
            if (e.getInventory().equals(inv)) {
                if ((e.getCurrentItem()) == null) return;
                ItemMeta meta = e.getCurrentItem().getItemMeta();
                PersistentDataContainer data = meta.getPersistentDataContainer();
                NamespacedKey key = new NamespacedKey(Main.getPlugin(Main.class), "iconClickFunction");
                String inviteData = data.get(key, PersistentDataType.STRING);
                if((inviteData.substring(0, 7)).equals("Invite_")) {
                    int index = Integer.parseInt(inviteData.substring(7));
                    if (e.isLeftClick()) {
                        e.getWhoClicked().closeInventory();
                        Player p = ((Player) e.getWhoClicked());
                        p.sendTitle(ChatColor.GREEN + "Welcome!", ChatColor.GREEN + "To the Town of " + you.getInvites()[index].getName(), 5, 30, 5);
                        p.playSound(p, Sound.BLOCK_AMETHYST_BLOCK_CHIME, 1f, 2f);
                        you.getInvites()[index].addPlayer(you);
                        you.removeInvite(you.getInvites()[index]);
                    } else if (e.isRightClick()) {
                        e.getWhoClicked().closeInventory();
                        Player p = ((Player) e.getWhoClicked());
                        p.sendTitle(ChatColor.RED + "Rejected Invite!", ChatColor.RED + "To the Town of " + you.getInvites()[index].getName(), 5, 30, 5);
                        p.playSound(p, Sound.BLOCK_AMETHYST_BLOCK_CHIME, 1f, 2f);
                        you.getInvites()[index].addPlayer(you);
                        you.removeInvite(you.getInvites()[index]);
                    }
                }
            }
        }
    }
}



