package org.puretripp.vassal.events;

import org.bukkit.*;
import org.bukkit.block.Banner;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.BlockData;
import org.bukkit.block.data.Rotatable;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;
import org.puretripp.vassal.main.Main;
import org.puretripp.vassal.types.Nation;
import org.puretripp.vassal.utils.LandChunk;
import org.puretripp.vassal.utils.Residence;
import org.puretripp.vassal.utils.VassalWorld;
import org.puretripp.vassal.utils.VassalsPlayer;

public class Events implements Listener {
    VassalWorld instance = Main.currentInstance;

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        instance.onlinePlayers.add(new VassalsPlayer(e.getPlayer().getUniqueId()));
    }

    /**
     * Checks to see who places a block and
     * @param e BlockBreakEvent
     */
    @EventHandler
    public void onBreak(final BlockBreakEvent e) {
        LandChunk lc = VassalWorld.getLandChunkByChunk(e.getBlock().getLocation().getChunk());
        if(lc != null) {
            VassalsPlayer vp = VassalWorld.getPlayer(e.getPlayer());
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
        LandChunk lc = VassalWorld.getLandChunkByChunk(e.getBlock().getLocation().getChunk());
        if (lc != null) {
            VassalsPlayer vp = VassalWorld.getPlayer(e.getPlayer());
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

    @EventHandler
    public void onInteract(PlayerInteractEvent e) {
        if(e.getClickedBlock() == null) return;
        LandChunk c = VassalWorld.getLandChunkByChunk(e.getClickedBlock().getChunk());
        if (c == null) return;
        VassalsPlayer vp = VassalWorld.getPlayer(e.getPlayer());
        if (vp.getSelectionMode() && vp.getRank(vp.getSelected()).getValue() > 8) {
            if (vp.containsVertex(e.getClickedBlock().getLocation())) {
                vp.removeVertex(e.getClickedBlock().getLocation());
            } else {
                vp.addVertex(e.getClickedBlock().getLocation());
                BukkitRunnable showParticles = new BukkitRunnable() {
                    @Override
                    public void run() {
                        e.getPlayer().spawnParticle(Particle.CAMPFIRE_COSY_SMOKE, e.getClickedBlock().getLocation().add(0, 1, 0), 5,  0, 1, 0, 0);
                    }
                };
                showParticles.runTaskTimerAsynchronously(Main.getPlugin(Main.class), 1L, 20L);
                vp.addTask(showParticles);
            }
        }

    }

    @EventHandler
    public void onNationCreation(OnNationCreation e) {

    }
}
