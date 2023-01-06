package org.puretripp.vassal.commands;

import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.puretripp.vassal.events.OnNationCreation;
import org.puretripp.vassal.events.OnNationJoin;
import org.puretripp.vassal.menus.NationMenu;
import org.puretripp.vassal.menus.TownshipMenu;
import org.puretripp.vassal.types.Nation;
import org.puretripp.vassal.types.governments.GovStyles;
import org.puretripp.vassal.types.ranks.TownRanks;
import org.puretripp.vassal.types.townships.Township;
import org.puretripp.vassal.utils.VassalWorld;
import org.puretripp.vassal.utils.VassalsPlayer;

import java.util.ArrayList;
import java.util.UUID;

public class NationsMaster implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String alias, String[] args)
            throws IllegalArgumentException {
        if (!(sender instanceof Player)) return false;

        Player p = (Player) sender;
        if (!command.getName().equalsIgnoreCase("nation")) {
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
                    p.openInventory((new NationMenu(vp.getNation())).getInv());
                    return true;
                case "create":
                    if (args.length <= 1) {
                        throw new IllegalArgumentException("Must include a Name!");
                    }
                    String nationName = args[1];
                    if (args.length > 2) {
                        for (int i = 2; i < args.length; i++) {
                            nationName += (" " + args[i]);
                        }
                    }
                    //Check if they can create
                    if(vp.getNation() != null) {
                        throw new IllegalArgumentException("You can not create a Nation when you are in a nation!");
                    }
                    ArrayList<VassalsPlayer> leaders = new ArrayList<VassalsPlayer>();
                    leaders.add(VassalWorld.getPlayer(p));

                    ArrayList<Township> towns = new ArrayList<Township>();
                    towns.add(vp.getSelected());
                    Nation newNation = new Nation(nationName, null, GovStyles.MONARCHY,
                            leaders, null, towns);
                    /*
                    //Calls Nation Creation event
                    Bukkit.getServer().getPluginManager().callEvent(new OnNationCreation());
                    //Calls player Join
                    Bukkit.getServer().getPluginManager().callEvent(new OnNationJoin());
                    */
                    //To go Into Event Handler
                    vp.setNation(newNation);
                    VassalWorld.nations.add(newNation);
                    for (int i = 0; i < towns.size(); i++) {
                        towns.get(i).setNation(newNation);
                    }
                    p.playSound(p, Sound.MUSIC_OVERWORLD_MEADOW, 3f, 1.0f);
                    p.sendMessage("Nation of " + nationName + " has been created!");
                return true;
            }
        } catch(Exception e) {
            p.sendMessage(e.getMessage());
        }
        return true;
    }
}
