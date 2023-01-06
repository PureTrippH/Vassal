package org.puretripp.vassal.commands;

import org.bukkit.*;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.puretripp.vassal.menus.TownshipMenu;
import org.puretripp.vassal.types.ranks.TownRanks;
import org.puretripp.vassal.types.townships.Township;
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
                    p.openInventory((new TownshipMenu(vp.getSelected())).getInv());
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
                    t.chunks.get(0).displayChunk(p, true, true, true, true);
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
