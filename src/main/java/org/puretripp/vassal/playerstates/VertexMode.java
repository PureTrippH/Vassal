package org.puretripp.vassal.playerstates;

import org.bukkit.Color;
import org.bukkit.Particle;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.scheduler.BukkitRunnable;
import org.lustrouslib.menu.Menu;
import org.lustrouslib.menu.interfaces.ChatCatchPlayerState;
import org.lustrouslib.menu.interfaces.ClickablePlayerState;
import org.lustrouslib.menu.interfaces.GUIMenu;
import org.lustrouslib.menu.interfaces.PlayerState;
import org.lustrouslib.wrapper.PlayerWrapper;
import org.lustrouslib.wrapper.StateHandler;
import org.puretripp.vassal.main.Main;
import org.puretripp.vassal.menus.submenus.SubclaimMenu;
import org.puretripp.vassal.types.Residence;
import org.puretripp.vassal.utils.claiming.LandChunk;
import org.puretripp.vassal.utils.general.VassalWorld;
import org.puretripp.vassal.utils.general.VassalsPlayer;
import org.puretripp.vassal.utils.runnables.LineRunnable;

import java.util.ArrayList;
import java.util.LinkedList;

public class VertexMode implements ClickablePlayerState, ChatCatchPlayerState {
    private GUIMenu currMenu;
    private GUIMenu prevMenu;
    private boolean canChat;
    private Residence res;
    private LinkedList<ArrayList<LineRunnable>> partViewStack = new LinkedList<>();
    private LinkedList<BukkitRunnable> vertexDisplayList = new LinkedList<>();

    public VertexMode(Residence res, GUIMenu prevMenu) {
        this.prevMenu = prevMenu;
        this.res = res;
    }

    public GUIMenu getPrevMenu() {
        return prevMenu;
    }
    public GUIMenu getCurrMenu() {
        return currMenu;
    }
    public void rightClickAction(PlayerInteractEvent e, PlayerWrapper pw) {
        if(e.getClickedBlock() == null) return;
        LandChunk c = VassalWorld.getWorldInstance().getLandChunkByChunk(e.getClickedBlock().getChunk());
        if (c == null) return;
        VassalsPlayer vp = VassalWorld.getWorldInstance().getWrapper(e.getPlayer());
        if (!vp.cooldowns.getOrDefault("clickAgainCooldown", true)) return;
        if (res.containsVertex(e.getClickedBlock().getLocation())) {
            res.removeVertex(e.getClickedBlock().getLocation());
        } else {
            res.addVertex(e.getClickedBlock().getLocation());
            if (res.polygonSize() >= 2) {
                //Add To Stack
                if (res.polygonSize() > 2) {
                    ArrayList<LineRunnable> removeLine = partViewStack.removeFirst();
                    for (LineRunnable particles : removeLine) {
                        particles.cancel();
                    }
                }
                partViewStack.addFirst(Residence.generateLine(vp, e.getPlayer(), res.getVertex(res.polygonSize() - 2).clone(), res.getVertex(res.polygonSize() - 1).clone()));
                partViewStack.addFirst(Residence.generateLine(vp, e.getPlayer(), res.getVertex(res.polygonSize() - 1).clone(), res.getVertex(0).clone()));
            }
            BukkitRunnable showParticles = new BukkitRunnable() {
                @Override
                public void run() {
                    Particle.DustOptions options = new Particle.DustOptions(Color.fromRGB(207, 131, 64), 2.0F);
                    e.getPlayer().spawnParticle(Particle.REDSTONE, e.getClickedBlock().getLocation().add(0.5, 1.5, 0.5), 50,  0, 0, 0, 0, options);
                }
            };
            vertexDisplayList.add(showParticles);
            showParticles.runTaskTimerAsynchronously(Main.getPlugin(Main.class), 1L, 20L);
            vertexDisplayList.add(showParticles);
        }
        vp.cooldowns.put("clickAgainCooldown", false);
        BukkitRunnable delayClick = new BukkitRunnable() {
            @Override
            public void run() {
                vp.cooldowns.put("clickAgainCooldown", true);
            }
        };
        delayClick.runTaskLater(Main.getPlugin(Main.class), 40L);
    }
    public void leftClickAction(PlayerInteractEvent e, PlayerWrapper pw) {
        e.setCancelled(true);
        this.destruct(pw);
        return;
    }
    public boolean getChatEnabled() {
        return canChat;
    }

    @Override
    public void destruct(PlayerWrapper pw) {
        if (!(pw instanceof VassalsPlayer)) return;
        VassalsPlayer vp = (VassalsPlayer) pw;
        if (!vp.cooldowns.getOrDefault("clickAgainCooldown", true)) return;
        for (ArrayList<LineRunnable> line : partViewStack) {
            for(BukkitRunnable runnable : line) {
                runnable.cancel();
            }
        }
        for (BukkitRunnable vertex : vertexDisplayList) {
            vertex.cancel();
        }
        vp.clearTasks();
        vp.setState(null);
        vp.pushMenu(prevMenu);
        return;
    }

    @Override
    public void setCurrMenu(GUIMenu menu) {
        this.currMenu = menu;
    }

    @Override
    public void setPrevMenu(GUIMenu menu) {
        this.prevMenu = menu;
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
}
