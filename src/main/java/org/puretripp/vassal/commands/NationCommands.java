package org.puretripp.vassal.commands;

import org.bukkit.*;
import org.bukkit.entity.Player;
import org.puretripp.vassal.menus.HelpMenu;
import org.puretripp.vassal.utils.SubCommand;

public class NationCommands {
    public static class helpCommand extends SubCommand {
        public final String name = "Help";
        public final String desc = ChatColor.WHITE + "Opens the Nation Help GUI\n" + ChatColor.GREEN + "Usage: " + ChatColor.WHITE + "/nation help";
        public void onCommand(Player p, String[] args) {
            HelpMenu cmds = new HelpMenu(NationCommandManager.getCommands());
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
}
