package org.puretripp.vassal.menus.submenus;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.lustrouslib.menu.Menu;
import org.lustrouslib.menu.MenuIcon;
import org.lustrouslib.wrapper.StateHandler;
import org.puretripp.vassal.utils.general.VassalsPlayer;
import org.puretripp.vassal.utils.interfaces.Inviter;

import java.util.List;

public class InviteMenu extends Menu {
    VassalsPlayer you;
    Inviter[] invites;
    public InviteMenu(VassalsPlayer vp, StateHandler state, Inviter[] invites) {
        super("Your Invites:", vp, state.getPlugin());
        this.you = vp;
        this.populateItems();
        this.invites = invites;
    }
    public void populateItems() {
        Inviter[] invites = you.getAllInvites();
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
