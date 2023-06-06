package org.puretripp.vassal.events;

import org.bukkit.*;
import org.bukkit.block.Banner;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.BlockData;
import org.bukkit.block.data.Rotatable;
import org.bukkit.entity.Player;
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
import org.puretripp.vassal.utils.runnables.LineRunnable;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;

public class Events implements Listener {
    VassalWorld instance = Main.currentInstance;

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        instance.onlinePlayers.put(e.getPlayer().getUniqueId(), new VassalsPlayer(e.getPlayer().getUniqueId()));
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
        if (vp.getSelectionMode() && vp.getRank(vp.getSelected()).getValue() > 8 && vp.canClickAgain) {
            if (vp.containsVertex(e.getClickedBlock().getLocation())) {
                vp.removeVertex(e.getClickedBlock().getLocation());
            } else {
                vp.addVertex(e.getClickedBlock().getLocation());
                if (vp.vertexSelections.size() >= 2) {
                    //Add To Stack
                    if (vp.vertexSelections.size() > 2) {
                        ArrayList<LineRunnable> removeLine = vp.partViewStack.removeFirst();
                        for (LineRunnable particles : removeLine) {
                            particles.stopParticles();
                            particles.cancel();
                        }
                    }
                    vp.partViewStack.addFirst(generateLine(vp, e.getPlayer(), vp.vertexSelections.get(vp.vertexSelections.size() - 2).clone(), vp.vertexSelections.getLast().clone()));
                    vp.partViewStack.addFirst(generateLine(vp, e.getPlayer(), vp.vertexSelections.getLast().clone(), vp.vertexSelections.getFirst().clone()));
                }
                BukkitRunnable showParticles = new BukkitRunnable() {
                    @Override
                    public void run() {
                        Particle.DustOptions options = new Particle.DustOptions(Color.fromRGB(207, 131, 64), 2.0F);
                        e.getPlayer().spawnParticle(Particle.REDSTONE, e.getClickedBlock().getLocation().add(0.5, 1.5, 0.5), 50,  0, 0, 0, 0, options);
                    }
                };
                showParticles.runTaskTimerAsynchronously(Main.getPlugin(Main.class), 1L, 20L);
                vp.addTask(showParticles);
            }
            vp.canClickAgain = false;
            BukkitRunnable delayClick = new BukkitRunnable() {
                @Override
                public void run() {
                    vp.canClickAgain = true;
                }
            };
            delayClick.runTaskLater(Main.getPlugin(Main.class), 40L);
        }

    }

    /**
     * Util for Vertices
     */
    public ArrayList<LineRunnable> generateLine(VassalsPlayer vp, Player p, Location start, Location end) {
        Location vertex1Clone = start.clone();
        Vector borderVector = end.clone().subtract(start).toVector().normalize().multiply(0.2);
        p.sendMessage(String.valueOf(vertex1Clone.distance(end)));
        Location addedBorder = start;
        ArrayList<LineRunnable> line = new ArrayList<>();
        for (double covered = 0 ; covered < vertex1Clone.distance(end); start.add(borderVector)) {
            LineRunnable showParticles = new LineRunnable(start.clone().add(new Vector(0.5, 1.5, 0.5)), p);
            showParticles.runTaskTimerAsynchronously(Main.getPlugin(Main.class), 1L, 20L);
            line.add(showParticles);
            covered += 0.2;
        }
        return line;
    }

    @EventHandler
    public void onNationCreation(OnNationCreation e) {

    }
}
