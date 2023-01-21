package org.puretripp.vassal.commands;

import org.apache.commons.lang.Validate;
import org.bukkit.*;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;
import org.puretripp.vassal.main.Main;
import org.puretripp.vassal.menus.TownshipMenu;
import org.puretripp.vassal.types.ranks.TownRanks;
import org.puretripp.vassal.types.townships.Township;
import org.puretripp.vassal.utils.Residence;
import org.puretripp.vassal.utils.VassalWorld;
import org.puretripp.vassal.utils.VassalsPlayer;

import java.util.ArrayList;
import java.util.UUID;

public class MasterCommand implements CommandExecutor {
    @Override

    public boolean onCommand(CommandSender sender, Command command, String alias, String[] args)
    throws IllegalArgumentException {
        if (!(sender instanceof Player)) return false;

        Player p = (Player) sender;
        if (!command.getName().equalsIgnoreCase("vassal")) {
            return true;
        }
        if(args.length < 1) {
            p.sendMessage("Vassals: Command Not Found");
            return true;
        }
        VassalsPlayer vp = VassalWorld.onlinePlayers.get(VassalWorld.onlinePlayers.indexOf(new VassalsPlayer(p.getUniqueId())));
        try {
            switch (args[0].toLowerCase()) {
                case "view":
                    p.openInventory((new TownshipMenu(vp.getSelected(), vp)).getInv());
                    return true;
                case "invite":
                    if (args.length <= 1) {
                        throw new IllegalArgumentException("Must include a Name!");
                    }
                    if (vp.getRank(vp.getSelected()).getValue() >= 8) {

                    }
                    return true;
                case "join":
                    return true;
                case "display":
                    vp.getSelected().display(p);
                    return true;
                case "claim":
                    vp.getSelected().claimChunk(p.getLocation().getChunk());
                    vp.getSelected().display(p);
                    return true;
                case "allocate":
                    //REDO SYSTEM TO BE GUI FOCUSED
                    if (args.length <= 1) {
                        throw new IllegalArgumentException("Must add <add/clear>!");
                    }
                    String mode = args[1];
                    if (mode.equals("toggle")) {
                        vp.setSelectionMode(!vp.getSelectionMode());
                        p.sendMessage("Toggled Vertex Selection Mode: " + ((vp.getSelectionMode()) ? "On" : "Off"));
                    }
                    if (mode.equals("create")) {
                        //Yes ik this is overdoing it but idc
                        if (args.length <= 2) {
                            throw new IllegalArgumentException("Need a height specifier");
                        }
                        ArrayList<Location> vertices = new ArrayList<>(vp.getAllVertices());
                        ArrayList <UUID> members = new ArrayList<>();
                        members.add(p.getUniqueId());
                        Township t = VassalWorld.getLandChunkByChunk(vertices.get(0).getChunk()).getTown();
                        Residence res = new Residence(vertices, Integer.parseInt(args[2]), p.getUniqueId(), members);
                        t.addResidence(res);
                        p.sendMessage("Added Residence");
                    }
                    if (mode.equals("clear")) { vp.clearVertices(); }
                    return true;
                case "create":
                    //Creates Name
                    if (args.length <= 1) {
                        throw new IllegalArgumentException("Must include a Name!");
                    }
                    String name = args[1];
                    if (args.length > 2) {
                        for (int i = 2; i < args.length; i++) {
                            name += (" " + args[i]);
                        }
                    }
                    ArrayList<UUID> players = new ArrayList<UUID>();
                    players.add(p.getUniqueId());
                    Chunk c = p.getLocation().getChunk();
                    if (VassalWorld.allLand.containsKey(c)) {
                        vp.getSelected().display(p);
                        p.sendMessage("Chunk is Already Claimed!");
                        return true;
                    }
                    Township t = new Township(name, p.getUniqueId(), 0, c, players);
                    //Add Town to Player's Info
                    vp.addTown(t, TownRanks.LEADER);
                    vp.setSelected(t);
                    //Send indicators
                    p.sendMessage("Township of " + name + " has been created!");
                    p.playSound(p, Sound.UI_TOAST_CHALLENGE_COMPLETE, 1f, 2f);
                    t.getChunk(0).displayChunk(p, true, true, true, true);
                    return true;
                default:
                    p.sendMessage("Vassals: Command Not Found");
                    return true;
            }
        } catch(Exception e) {
            p.sendMessage(e.getMessage());
        }
        return true;
    }
}
