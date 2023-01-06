package org.puretripp.vassal.events;

import org.bukkit.Material;
import org.bukkit.Rotation;
import org.bukkit.block.Banner;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.BlockData;
import org.bukkit.block.data.Rotatable;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.puretripp.vassal.main.Main;
import org.puretripp.vassal.types.Nation;
import org.puretripp.vassal.utils.LandChunk;
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
            if(!vp.isInTown(lc.getTown())) {
                e.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onPlace(final BlockPlaceEvent e) {
        LandChunk lc = VassalWorld.getLandChunkByChunk(e.getBlock().getLocation().getChunk());
        if (lc != null) {
            VassalsPlayer vp = VassalWorld.getPlayer(e.getPlayer());
            if (!vp.isInTown(lc.getTown())) {
                e.setCancelled(true);
                return;
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
    public void onNationCreation(OnNationCreation e) {

    }
}
