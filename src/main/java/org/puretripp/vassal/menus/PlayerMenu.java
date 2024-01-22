package org.puretripp.vassal.menus;

import org.bukkit.*;
import org.bukkit.entity.Player;
import org.lustrouslib.menu.Menu;
import org.lustrouslib.menu.MenuIcon;
import org.lustrouslib.wrapper.StateHandler;
import org.puretripp.vassal.menus.submenus.InviteMenu;
import org.puretripp.vassal.utils.general.VassalsPlayer;
import org.puretripp.vassal.utils.interfaces.Inviter;

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
            you.pushMenu(new InviteMenu(you, state, you.getAllInvites()));
        }));
        refreshContents();
    }

}



