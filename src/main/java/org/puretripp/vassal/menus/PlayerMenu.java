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
import org.puretripp.vassal.utils.MenuIcon;
import org.puretripp.vassal.utils.general.VassalWorld;
import org.puretripp.vassal.utils.general.VassalsPlayer;
import org.puretripp.vassal.utils.interfaces.Invitable;
import org.puretripp.vassal.utils.interfaces.InviteDeliverer;

import java.util.List;

public class PlayerMenu extends Menu {
    VassalsPlayer you;
    public PlayerMenu(VassalsPlayer vp) {
        super("You:");
        this.you = vp;
        populateItems();
    }

    public void populateItems() {
        super.contents.add(new MenuIcon(MenuIcon.generateItem(Material.BOOKSHELF,
                (ChatColor.GREEN + "Invites"),  "Invites"), () -> {
            you.pushMenu(new InvitesMenu(you));
        }));
        refreshContents();
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
            InviteDeliverer[] invites = you.getAllInvites();
            for (int i = 0; i < invites.length; i++) {
                int index = i;
                super.contents.add(new MenuIcon(MenuIcon.generateItem(Material.PAPER,
                        (ChatColor.GREEN + invites[i].toString()),  "Invite_" + i),
                        () -> {
                            Player p = you.getPlayer();
                            p.closeInventory();
                            you.acceptInvite(you.getInvite(index));
                            p.sendTitle(ChatColor.GREEN + "Invite Accepted!","", 5, 30, 5);
                            p.playSound(p, Sound.BLOCK_AMETHYST_BLOCK_CHIME, 1f, 2f);
                        }, () -> {
                            Player p = you.getPlayer();
                            p.closeInventory();
                            p.sendTitle(ChatColor.RED + "Rejected Invite!", "", 5, 30, 5);
                            p.playSound(p, Sound.BLOCK_AMETHYST_BLOCK_CHIME, 1f, 2f);
                            you.rejectInvite(you.getInvite(index));
                }));
            }
            refreshContents();
        }
    }
}



