package org.puretripp.vassal.events;

import org.bukkit.*;
import org.bukkit.block.Banner;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.Rotatable;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.scheduler.BukkitRunnable;
import org.lustrouslib.wrapper.StateHandler;
import org.puretripp.vassal.main.Main;
import org.puretripp.vassal.menus.submenus.SubclaimMenu;
import org.puretripp.vassal.types.Nation;
import org.puretripp.vassal.utils.claiming.LandChunk;
import org.puretripp.vassal.types.Residence;
import org.puretripp.vassal.utils.general.VassalWorld;
import org.puretripp.vassal.utils.general.VassalsPlayer;
import org.puretripp.vassal.utils.runnables.LineRunnable;

import java.util.ArrayList;

public class Events implements Listener {
    VassalWorld instance = Main.currentInstance;

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        VassalWorld.getWorldInstance().wrap(e.getPlayer());
    }

    /**
     * Checks to see who places a block and
     * @param e BlockBreakEvent
     */
    @EventHandler
    public void onBreak(final BlockBreakEvent e) {
        LandChunk lc = VassalWorld.getWorldInstance().getLandChunkByChunk(e.getBlock().getLocation().getChunk());
        if(lc != null) {
            VassalsPlayer vp = VassalWorld.getWorldInstance().getWrapper(e.getPlayer());
            e.setCancelled(!vp.isInTown(lc.getTown()));
            for (Residence r : lc.getTown().getAllResidences()) {
                if(r.contains(e.getBlock().getLocation())) {
                    Bukkit.getLogger().info(ChatColor.DARK_RED + "INSIDE POINT");
                    e.setCancelled(!r.getOwner().equals(e.getPlayer().getUniqueId()));
                    e.getPlayer().sendMessage("You are Inside The Region!");
                }
            }
        }
    }

    @EventHandler
    public void onPlace(final BlockPlaceEvent e) {
        LandChunk lc = VassalWorld.getWorldInstance().getLandChunkByChunk(e.getBlock().getLocation().getChunk());
        if (lc != null) {
            VassalsPlayer vp = VassalWorld.getWorldInstance().getWrapper(e.getPlayer());
            e.setCancelled(!vp.isInTown(lc.getTown()));
            for (Residence r : lc.getTown().getAllResidences()) {
                if(r.contains(e.getBlock().getLocation())) {
                    e.setCancelled(!r.getOwner().equals(e.getPlayer().getUniqueId()));
                }
            }
        }
        if(e.getBlock().getType() == Material.WHITE_BANNER
                || e.getBlock().getType() == Material.WHITE_WALL_BANNER) {
            Nation n = lc.getTown().getNation();
            if(n != null) {
                if(e.getBlock().getType() == Material.WHITE_WALL_BANNER) {
                    String color = n.getBannerType().toString().split("_")[0];
                    e.getBlock().setType(Material.getMaterial(color.toUpperCase() + "_WALL_BANNER"));
                    e.getBlock().getState().update(true);
                    Banner banner = (Banner) e.getBlock().getState();
                    banner.setPatterns(n.getBanner().getPatterns());
                    e.getPlayer().sendMessage("Banner Placed");
                    banner.update();
                } else {
                    //Initial State
                    Rotatable t = (Rotatable) e.getBlock().getBlockData();
                    BlockFace rotation = t.getRotation();

                    //Sets the Block Type
                    e.getBlock().setType(n.getBannerType());
                    e.getBlock().getState().update(true);

                    //Sets Rotation
                    t.setRotation(rotation);

                    //Sets Banner Data
                    Banner banner = (Banner) e.getBlock().getState();
                    banner.setPatterns(n.getBanner().getPatterns());
                    e.getPlayer().sendMessage("Banner Placed");
                    banner.update();
                    Rotatable newRotate = ((Rotatable) banner.getBlockData());
                    newRotate.setRotation(rotation);
                    e.getBlock().setBlockData(newRotate);
                }
            }
        }
    }
}
