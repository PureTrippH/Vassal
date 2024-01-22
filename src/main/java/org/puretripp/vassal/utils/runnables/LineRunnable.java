package org.puretripp.vassal.utils.runnables;

import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;
import org.puretripp.vassal.main.Main;
import org.puretripp.vassal.utils.general.VassalsPlayer;

import java.util.ArrayList;

public class LineRunnable extends BukkitRunnable {
    private Location pos;
    private Player p;

    public LineRunnable(Location pos, Player p) {
        this.pos = pos;
        this.p = p;
    }

    @Override
    public void run() {
        Particle.DustOptions options = new Particle.DustOptions(Color.fromRGB(235, 185, 141), 0.7F);
        p.spawnParticle(Particle.REDSTONE, pos, 4,  0, 0, 0, 0, options);
    }


    public void stopParticles() {
        this.cancel();
    }
}
