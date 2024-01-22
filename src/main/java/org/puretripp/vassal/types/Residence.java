package org.puretripp.vassal.types;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;
import org.puretripp.vassal.main.Main;
import org.puretripp.vassal.types.townships.Township;
import org.puretripp.vassal.utils.general.VassalsPlayer;
import org.puretripp.vassal.utils.interfaces.Displayable;
import org.puretripp.vassal.utils.runnables.LineRunnable;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

public class Residence implements Displayable<ArrayList<LineRunnable>> {
    private ArrayList<Location> vertices;
    private String name;
    private ArrayList<UUID> members;
    private UUID owner;
    private Township t;
    private World world;
    private boolean isComplexHeight;
    private int maxHeight;
    private int minHeight;

    public Residence(String name, Township town, ArrayList<Location> vertices, int maxHeight, UUID owner,
                     ArrayList<UUID> members, boolean isComplexHeight) {
        this.name = name;
        this.vertices = vertices;
        this.maxHeight = maxHeight;
        this.owner = owner;
        this.members = members;
        this.isComplexHeight = isComplexHeight;
    }


    public Residence(String name, Township town) {
        this(name, town, new ArrayList<Location>(), 0, null, new ArrayList<UUID>(), false);
    }

    public void addVertex(Location l) {
        vertices.add(l);
    }

    //Ty _Donut_ on Spigot <3
    public boolean contains(Location point) {
        int intersections = 0;
        for (int i = 0; i < vertices.size(); i++) {
            Location vertex1 = vertices.get(i);
            Location vertex2 = vertices.get(i + 1 < vertices.size() ? i + 1 : 0);
            double x0 = vertex1.getX();
            double y0 = vertex1.getY();
            double z0 = vertex1.getZ();
            double x1 = vertex2.getX();
            double y1 = vertex2.getY();
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

    public String getName() {
        return name;
    }

    public boolean containsVertex(Location vertex) {
        return vertices.contains(vertex);
    }
    public int polygonSize() {
        return vertices.size();
    }

    public Location getVertex(int index) {
        return vertices.get(index);
    }

    public void clearLines(VassalsPlayer vp) {

    }

    public void setMaxHeight(int maxHeight) {
        this.maxHeight = maxHeight;
    }

    public int getMaxHeight() {
        return maxHeight;
    }


    /**
     * Util for Vertices
     */
    public static ArrayList<LineRunnable> generateLine(VassalsPlayer vp, Player p, Location start, Location end) {
        Location vertex1Clone = start.clone();
        Vector borderVector = end.clone().subtract(start).toVector().normalize().multiply(0.2);
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



    public boolean removeVertex(Location vertex) {
        return vertices.remove(vertex);
    }

    @Override
    public List<ArrayList<LineRunnable>> display(VassalsPlayer vp) {
        Player p = vp.getPlayer();
        LinkedList<ArrayList<LineRunnable>> partViewStack = new LinkedList<>();
        for (int i = 0; i < vertices.size() - 1; i++) {
            partViewStack.addFirst(Residence.generateLine(vp, p, getVertex(i).clone(), getVertex(i + 1).clone()));
            partViewStack.addFirst(Residence.generateLine(vp, p, getVertex(i).clone()
                            .add(new Vector(0, maxHeight, 0)),
                    getVertex(i + 1).clone().add(new Vector(0, maxHeight, 0))));
            partViewStack.addFirst(Residence.generateLine(vp, p, getVertex(i).clone(),
                    getVertex(i).clone().add(new Vector(0, maxHeight, 0))));
        }
        partViewStack.addFirst(Residence.generateLine(vp, p, getVertex(polygonSize() - 1).clone(),
                getVertex(0).clone()));
        partViewStack.addFirst(Residence.generateLine(vp, p, getVertex(polygonSize() - 1).clone(),
                getVertex(polygonSize() - 1).clone().add(new Vector(0, maxHeight, 0))));
        partViewStack.addFirst(Residence.generateLine(vp, p, getVertex(0).clone().add(new Vector(0, maxHeight,
                0)), getVertex(polygonSize() - 1).clone().add(new Vector(0, maxHeight, 0))));
        return partViewStack;
    }

    @Override
    public void destroyDisplay(List<ArrayList<LineRunnable>> list, VassalsPlayer vp) {
        for (ArrayList<LineRunnable> line : list) {
            for (LineRunnable runnable : line) {
                runnable.stopParticles();
            }
        }
        Bukkit.getLogger().warning("" + list.size());
        vp.clearTasks();
    }
}