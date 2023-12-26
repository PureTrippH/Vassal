package org.puretripp.vassal.commands;

import org.bukkit.*;
import org.bukkit.entity.Player;
import org.lustrouslib.command.CommandManager;
import org.lustrouslib.command.SubCommand;
import org.lustrouslib.menu.specificmenus.HelpMenu;
import org.lustrouslib.wrapper.PlayerWrapper;
import org.lustrouslib.wrapper.StateHandler;
import org.puretripp.vassal.menus.PlayerMenu;
import org.puretripp.vassal.menus.submenus.SubclaimMenu;
import org.puretripp.vassal.menus.TownshipMenu;
import org.puretripp.vassal.types.townships.Township;
import org.puretripp.vassal.types.Residence;
import org.puretripp.vassal.utils.claiming.perms.PermClass;
import org.puretripp.vassal.utils.general.VassalWorld;
import org.puretripp.vassal.utils.general.VassalsPlayer;

import java.util.HashMap;
import java.util.UUID;

public class TownCommands {
    public static class SelfCommand implements SubCommand {
        public final String name = "Player Menu";
        public final String desc = ChatColor.WHITE + "Open Up The Personal Player Menu\n"+ ChatColor.GREEN + "Usage: " + ChatColor.WHITE + "/vassal pm";
        @Override
        public void onCommand(PlayerWrapper wrapper, StateHandler<? extends PlayerWrapper> state, String[] args) {
            Player p = wrapper.getPlayer();
            VassalsPlayer vp = (VassalsPlayer) state.getPlayerWrapper(p);
            p.openInventory((new PlayerMenu(vp)).getInv());
        }
        public String getName() { return name; }
        public String getDesc() {
            return desc;
        }
    }


    public static class inviteCommand implements SubCommand {
        public final String name = "Invite";
        public final String desc = ChatColor.WHITE + "Sends an invite to the Targetted Player\n" + ChatColor.GREEN + "Usage: " + ChatColor.WHITE + "/vassal invite <player>";
        @Override
        public void onCommand(PlayerWrapper wrapper, StateHandler<? extends PlayerWrapper> state, String[] args) {
            Player p = wrapper.getPlayer();
            VassalsPlayer vp = (VassalsPlayer) state.getPlayerWrapper(p);
            if (args.length <= 1) {
                throw new IllegalArgumentException("Must include a Name!");
            }
            OfflinePlayer target = Bukkit.getOfflinePlayer(args[1]);
            if (vp.getSelected().getPlayerRank(p.getUniqueId()).isCanInvite() && target.hasPlayedBefore()) {
                VassalWorld.getWorldInstance().getWrapper((Player) target).addInvite(vp.getSelected());
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


    public static class borderCommand implements SubCommand {
        public final String name = "Display Borders";
        public final String desc = ChatColor.WHITE + "Shows the border of the currently selected claim\n" + ChatColor.GREEN + "Usage: " + ChatColor.WHITE + "/vassal display";
        @Override
        public void onCommand(PlayerWrapper wrapper, StateHandler<? extends PlayerWrapper> state, String[] args) {
            Player p = wrapper.getPlayer();
            VassalsPlayer vp = (VassalsPlayer) state.getPlayerWrapper(p);
            vp.getSelected().display(p);
        }
        public String getName() {
            return name;
        }
        public String getDesc() {
            return desc;
        }
    }


    public static class claimCommand implements SubCommand {
        public final String name = "Claim Land";
        public final String desc = ChatColor.WHITE + "Claims a chunk for your current town\n" + ChatColor.GREEN + "Usage: " + ChatColor.WHITE + "/vassal claim";

        @Override
        public void onCommand(PlayerWrapper wrapper, StateHandler<? extends PlayerWrapper> state, String[] args) {
            Player p = wrapper.getPlayer();
            VassalsPlayer vp = (VassalsPlayer) state.getPlayerWrapper(p);
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


    public static class menu implements SubCommand {
        public final String name = "Town Menu";
        public final String desc = ChatColor.WHITE + "See Your Town's Stats\n" + ChatColor.GREEN + "Usage: " + ChatColor.WHITE + "/vassal menu";

        @Override
        public void onCommand(PlayerWrapper wrapper, StateHandler<? extends PlayerWrapper> state, String[] args) {
            Player p = wrapper.getPlayer();
            VassalsPlayer vp = (VassalsPlayer) state.getPlayerWrapper(p);
            vp.pushMenu(new TownshipMenu(vp.getSelected(), vp));
        }
        public String getName() { return name; }
        public String getDesc() {
            return desc;
        }
    }


    public static class createCommand implements SubCommand {
        public final String name = "Create";
        public final String desc = ChatColor.WHITE + "Create a New Town\n" + ChatColor.GREEN + "Usage: " + ChatColor.WHITE + "/vassal create (town name spaces included)";

        @Override
        public void onCommand(PlayerWrapper wrapper, StateHandler<? extends PlayerWrapper> state, String[] args) {
            Player p = wrapper.getPlayer();
            VassalsPlayer vp = (VassalsPlayer) state.getPlayerWrapper(p);
            if (args.length <= 1) {
                throw new IllegalArgumentException("Must include a Name!");
            }
            String name = args[1];
            if (args.length > 2) {
                for (int i = 2; i < args.length; i++) {
                    name += (" " + args[i]);
                }
            }
            HashMap<UUID, PermClass> players = new HashMap<UUID, PermClass>();
            Chunk c = p.getLocation().getChunk();
            if (VassalWorld.getWorldInstance().allLand.containsKey(c)) {
                p.sendMessage("Chunk is Already Claimed!");
                return;
            }
            Township t = new Township(name, p.getUniqueId(), 0, c, players);
            //Add Town to Player's Info
            t.addPlayer(vp, t.getRank(0));
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


    public static class subclaimCommand implements SubCommand {
        public final String name = "Create Subclaim";
        public final String desc = ChatColor.WHITE + "Claims a 2d, infinite vertex subclaim\n" + ChatColor.GREEN + "Usage: " + ChatColor.WHITE + "/vassal subclaim <toggle/create>";;

        @Override
        public void onCommand(PlayerWrapper wrapper, StateHandler<? extends PlayerWrapper> state, String[] args) {
            Player p = wrapper.getPlayer();
            VassalsPlayer vp = (VassalsPlayer) state.getPlayerWrapper(p);
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
