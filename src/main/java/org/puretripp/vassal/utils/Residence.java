package org.puretripp.vassal.utils;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.UUID;

public class Residence {
    private ArrayList<Location> vertices;
    private ArrayList<UUID> members;
    private UUID owner;
    private World world;
    private int maxHeight;
    private int minHeight;

    public Residence(ArrayList<Location> vertices, int maxHeight, UUID owner, ArrayList<UUID> members) {
        this.vertices = vertices;
        this.maxHeight = maxHeight;
        this.owner = owner;
        this.members = members;
    }

    public void addVertex(Location l) {
        vertices.add(l);
    }

    public void showBorder() {

    }


    //Ty _Donut_ on Spigot <3
    public boolean contains(Location point) {
        int intersections = 0;
        for (int i = 0; i < vertices.size(); i++) {
            Location vertex1 = vertices.get(i);
            Location vertex2 = vertices.get(i + 1 < vertices.size() ? i + 1 : 0);
            double x0 = vertex1.getX();
            double z0 = vertex1.getZ();
            double x1 = vertex2.getX();
            double z1 = vertex2.getZ();

            Vector borderVector = vertex2.toVector().subtract(vertex1.toVector());
            double deltaZ = borderVector.getZ();
            double deltaX = borderVector.getX();

            if (deltaZ != 0) {
                double intersectionZ = point.getZ();
                double intersectionX = deltaX == 0 ? x0 : x0 + (intersectionZ - z0) / (deltaZ / deltaX);

                if (Math.abs(intersectionX - x0) < Vector.getEpsilon()) {
                    return false;
                }

                if (intersectionX > point.getX()
                        && intersectionX >= Math.min(x0, x1) && intersectionX <= Math.max(x0, x1)
                        && intersectionZ >= Math.min(z0, z1) && intersectionZ <= Math.max(z0, z1)) {
                    intersections++;
                }
            } else if (point.getZ() == z0) {
                return false;
            }
        }

        return (intersections % 2 == 1) && (point.getY() <= maxHeight && (point.getY() >= minHeight));
    }

    public UUID getOwner() { return this.owner; }
}
