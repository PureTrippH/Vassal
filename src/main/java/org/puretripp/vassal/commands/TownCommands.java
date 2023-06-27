package org.puretripp.vassal.commands;

import org.bukkit.*;
import org.bukkit.entity.Player;
import org.puretripp.vassal.menus.HelpMenu;
import org.puretripp.vassal.menus.PlayerMenu;
import org.puretripp.vassal.menus.submenus.SubclaimMenu;
import org.puretripp.vassal.menus.TownshipMenu;
import org.puretripp.vassal.types.ranks.TownRanks;
import org.puretripp.vassal.types.townships.Township;
import org.puretripp.vassal.utils.claiming.Residence;
import org.puretripp.vassal.utils.SubCommand;
import org.puretripp.vassal.utils.general.VassalWorld;
import org.puretripp.vassal.utils.general.VassalsPlayer;

import java.util.ArrayList;
import java.util.UUID;

public class TownCommands {
    public static class SelfCommand extends SubCommand {
        public final String name = "Player Menu";
        public final String desc = ChatColor.WHITE + "Open Up The Personal Player Menu\n"+ ChatColor.GREEN + "Usage: " + ChatColor.WHITE + "/vassal pm";
        public void onCommand(Player p, String[] args) {
            VassalsPlayer vp = VassalWorld.onlinePlayers.get(p.getUniqueId());
            p.openInventory((new PlayerMenu(vp)).getInv());
        }
        public String getName() { return name; }
        public String getDesc() {
            return desc;
        }
    }


    public static class inviteCommand extends SubCommand {
        public final String name = "Invite";
        public final String desc = ChatColor.WHITE + "Sends an invite to the Targetted Player\n" + ChatColor.GREEN + "Usage: " + ChatColor.WHITE + "/vassal invite <player>";
        public void onCommand(Player p, String[] args) {
            VassalsPlayer vp = VassalWorld.onlinePlayers.get(p.getUniqueId());
            if (args.length <= 1) {
                throw new IllegalArgumentException("Must include a Name!");
            }
            OfflinePlayer target = Bukkit.getOfflinePlayer(args[1]);
            if (vp.getRank(vp.getSelected()).getValue() >= 8 || target.hasPlayedBefore()) {
                VassalWorld.getPlayer((Player) target).addInvite(vp.getSelected());
                ((Player) target).sendTitle(ChatColor.GREEN + "New Invite!", ChatColor.GREEN + p.getPlayer().getName() + "Has Invited You To a Town", 5, 30, 5);
            }
        }
        public String getName() {
            return name;
        }
        public String getDesc() {
            return desc;
        }
    }


    public static class helpCommand extends SubCommand {
        public final String name = "Help";
        public final String desc = ChatColor.WHITE + "Sends a list of all of the commands in Vassals\n" + ChatColor.GREEN + "Usage: " + ChatColor.WHITE + "/vassal help";
        public void onCommand(Player p, String[] args) {
            HelpMenu cmds = new HelpMenu(CommandManager.getCommands());
            p.playSound(p, Sound.BLOCK_CHEST_OPEN, 1f, 0.3f);
            p.openInventory(cmds.getInv());
        }
        public String getName() {
            return name;
        }
        public String getDesc() {
            return desc;
        }
    }


    public static class borderCommand extends SubCommand {
        public final String name = "Display Borders";
        public final String desc = ChatColor.WHITE + "Shows the border of the currently selected claim\n" + ChatColor.GREEN + "Usage: " + ChatColor.WHITE + "/vassal display";

        public void onCommand(Player p, String[] args) {
            VassalsPlayer vp = VassalWorld.onlinePlayers.get(p.getUniqueId());
            vp.getSelected().display(p);
        }
        public String getName() {
            return name;
        }
        public String getDesc() {
            return desc;
        }
    }


    public static class claimCommand extends SubCommand {
        public final String name = "Claim Land";
        public final String desc = ChatColor.WHITE + "Claims a chunk for your current town\n" + ChatColor.GREEN + "Usage: " + ChatColor.WHITE + "/vassal claim";

        public void onCommand(Player p, String[] args) {
            VassalsPlayer vp = VassalWorld.onlinePlayers.get(p.getUniqueId());
            try {
                vp.getSelected().claimChunk(p.getLocation().getChunk());
                vp.clearTasks();
                vp.getSelected().display(p);
            } catch (NoSuchFieldException e) {
                e.printStackTrace();
            }
            vp.getSelected().display(p);
        }
        public String getName() { return name; }
        public String getDesc() {
            return desc;
        }
    }


    public static class menu extends SubCommand {
        public final String name = "Town Menu";
        public final String desc = ChatColor.WHITE + "See Your Town's Stats\n" + ChatColor.GREEN + "Usage: " + ChatColor.WHITE + "/vassal menu";

        public void onCommand(Player p, String[] args) {
            VassalsPlayer vp = VassalWorld.onlinePlayers.get(p.getUniqueId());
            p.openInventory((new TownshipMenu(vp.getSelected(), vp)).getInv());
        }
        public String getName() { return name; }
        public String getDesc() {
            return desc;
        }
    }


    public static class createCommand extends SubCommand {
        public final String name = "Create";
        public final String desc = ChatColor.WHITE + "Create a New Town\n" + ChatColor.GREEN + "Usage: " + ChatColor.WHITE + "/vassal create (town name spaces included)";

        public void onCommand(Player p, String[] args) {
            VassalsPlayer vp = VassalWorld.onlinePlayers.get(p.getUniqueId());
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
                p.sendMessage("Chunk is Already Claimed!");
                return;
            }
            Township t = new Township(name, p.getUniqueId(), 0, c, players);
            //Add Town to Player's Info
            vp.addTown(t, TownRanks.LEADER);
            vp.setSelected(t);
            //Send indicators
            p.sendMessage("Township of " + name + " has been created!");
            p.playSound(p, Sound.UI_TOAST_CHALLENGE_COMPLETE, 1f, 2f);
            t.getChunk(0).displayChunk(p, true, true, true, true);
        }
        public String getName() { return name; }
        public String getDesc() {
            return desc;
        }
    }


    public static class subclaimCommand extends SubCommand {
        public final String name = "Create Subclaim";
        public final String desc = ChatColor.WHITE + "Claims a 2d, infinite vertex subclaim\n" + ChatColor.GREEN + "Usage: " + ChatColor.WHITE + "/vassal subclaim <toggle/create>";;

        public void onCommand(Player p, String[] args) {
            VassalsPlayer vp = VassalWorld.onlinePlayers.get(p.getUniqueId());
            if (args.length <= 1) {
                throw new IllegalArgumentException("Must include a Name!");
            }
            String name = args[1];
            if (args.length > 2) {
                for (int i = 2; i < args.length; i++) {
                    name += (" " + args[i]);
                }
            }
            Township t = vp.getSelected();
            Residence newRes = new Residence(name, t);
            SubclaimMenu menu = new SubclaimMenu(newRes, t, vp);
            p.openInventory(menu.getInv());
        }
        public String getName() { return name; }
        public String getDesc() {
            return desc;
        }
    }
}
