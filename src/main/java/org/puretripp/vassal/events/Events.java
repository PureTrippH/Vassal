package org.puretripp.vassal.events;

import org.bukkit.*;
import org.bukkit.block.Banner;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.Rotatable;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;
import org.puretripp.vassal.main.Main;
import org.puretripp.vassal.menus.submenus.SubclaimMenu;
import org.puretripp.vassal.types.Nation;
import org.puretripp.vassal.utils.claiming.LandChunk;
import org.puretripp.vassal.utils.claiming.Residence;
import org.puretripp.vassal.utils.general.VassalWorld;
import org.puretripp.vassal.utils.general.VassalsPlayer;
import org.puretripp.vassal.utils.runnables.LineRunnable;

import java.util.ArrayList;

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
        if (vp.getIsSelectionMode() && vp.getRank(vp.getSelected()).getValue() > 8 && vp.cooldowns.getOrDefault("clickAgainCooldown", true)) {
            Residence res = vp.getSelectedResidence();
            if (e.getAction() == Action.LEFT_CLICK_BLOCK) {
                e.getPlayer().openInventory(new SubclaimMenu(vp.getSelectedResidence(), vp.getSelected(), vp).getInv());
                res.clearLines(vp);
                vp.clearTasks();
                vp.setSelectionMode(null);
                e.setCancelled(true);
                return;
            }
            if (res.containsVertex(e.getClickedBlock().getLocation())) {
                res.removeVertex(e.getClickedBlock().getLocation());
            } else {
                vp.getSelectedResidence().addVertex(e.getClickedBlock().getLocation());
                if (res.polygonSize() >= 2) {
                    //Add To Stack
                    if (res.polygonSize() > 2) {
                        ArrayList<LineRunnable> removeLine = res.removeBackVert();
                        for (LineRunnable particles : removeLine) {
                            particles.stopParticles();
                            particles.cancel();
                        }
                    }
                    res.addLine(Residence.generateLine(vp, e.getPlayer(), res.getVertex(res.polygonSize() - 2).clone(), res.getVertex(res.polygonSize() - 1).clone()));
                    res.addLine(Residence.generateLine(vp, e.getPlayer(), res.getVertex(res.polygonSize() - 1).clone(), res.getVertex(0).clone()));
                }
                BukkitRunnable showParticles = new BukkitRunnable() {
                    @Override
                    public void run() {
                        Particle.DustOptions options = new Particle.DustOptions(Color.fromRGB(207, 131, 64), 2.0F);
                        e.getPlayer().spawnParticle(Particle.REDSTONE, e.getClickedBlock().getLocation().add(0.5, 1.5, 0.5), 50,  0, 0, 0, 0, options);
                    }
                };
                vp.addTask(showParticles);
                showParticles.runTaskTimerAsynchronously(Main.getPlugin(Main.class), 1L, 20L);
                vp.addTask(showParticles);
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

    }


}
