package org.puretripp.vassal.commands;

import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.puretripp.vassal.menus.NationMenu;
import org.puretripp.vassal.types.Nation;
import org.puretripp.vassal.types.governments.GovStyles;
import org.puretripp.vassal.types.townships.Township;
import org.puretripp.vassal.utils.general.VassalWorld;
import org.puretripp.vassal.utils.general.VassalsPlayer;

import java.util.ArrayList;

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
        VassalsPlayer vp = VassalWorld.getWorldInstance().onlinePlayers.get(p.getUniqueId());
        try {
            switch (args[0].toLowerCase()) {
                case "invites":
                    vp.pushMenu(new NationMenu(vp.getNation(), vp));
                    return true;
                case "view":
                    vp.pushMenu(new NationMenu(vp.getNation(), vp));
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
                    leaders.add(VassalWorld.getWorldInstance().getWrapper(p));

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
                    VassalWorld.getWorldInstance().nations.add(newNation);
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
