package org.puretripp.vassal.menus;

import org.bukkit.*;
import org.bukkit.entity.Player;
import org.lustrouslib.menu.Menu;
import org.lustrouslib.menu.MenuIcon;
import org.lustrouslib.wrapper.StateHandler;
import org.puretripp.vassal.utils.general.VassalsPlayer;
import org.puretripp.vassal.utils.interfaces.InviteDeliverer;

public class PlayerMenu extends Menu {
    private VassalsPlayer you;
    private StateHandler<VassalsPlayer> state;
    public PlayerMenu(VassalsPlayer vp, StateHandler state) {
        super("You:", vp, state.getPlugin());
        this.state = state;
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
            super("Your Invites:", vp, state.getPlugin());
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



