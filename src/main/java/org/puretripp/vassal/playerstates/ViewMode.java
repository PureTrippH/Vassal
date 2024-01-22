package org.puretripp.vassal.playerstates;

import org.bukkit.Bukkit;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.scheduler.BukkitRunnable;
import org.lustrouslib.menu.interfaces.ChatCatchPlayerState;
import org.lustrouslib.menu.interfaces.GUIMenu;
import org.lustrouslib.wrapper.PlayerWrapper;
import org.puretripp.vassal.utils.interfaces.Displayable;
import org.puretripp.vassal.utils.general.VassalsPlayer;

import java.util.List;

public class ViewMode<E> implements ChatCatchPlayerState {
    private Displayable display;
    private GUIMenu prevMenu;
    private List<E> runList;

    public ViewMode(Displayable display, GUIMenu prevMenu, VassalsPlayer vp) {
        if (display == null) throw new IllegalArgumentException("Display Is Null!");
        this.display = display;
        this.runList = runList;
        if (prevMenu != null) {
            this.prevMenu = prevMenu;
        }
        this.runList = display.display(vp);
        if (vp.getPlayer().getInventory() != null) {
            vp.getPlayer().closeInventory();
        }
    }

    @Override
    public void chatAction(AsyncPlayerChatEvent e, PlayerWrapper pw) {
        if (!(pw instanceof VassalsPlayer)) return;
        VassalsPlayer vp = (VassalsPlayer) pw;
        switch(e.getMessage()) {
            case "exit":
                this.destruct(vp);
                break;
        }
    }

    @Override
    public GUIMenu getPrevMenu() {
        return prevMenu;
    }

    @Override
    public GUIMenu getCurrMenu() {
        return null;
    }

    @Override
    public void destruct(PlayerWrapper pw) {
        if (!(pw instanceof VassalsPlayer)) return;
        VassalsPlayer vp = (VassalsPlayer) pw;
        display.destroyDisplay(runList, vp);
        if (prevMenu != null) {
            vp.pushMenu(prevMenu);
        }
        vp.setState(null);
    }

    @Override
    public void setCurrMenu(GUIMenu guiMenu) {

    }

    @Override
    public void setPrevMenu(GUIMenu guiMenu) {

    }
}
