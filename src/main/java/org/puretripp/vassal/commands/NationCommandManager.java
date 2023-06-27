package org.puretripp.vassal.commands;

import net.md_5.bungee.api.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.puretripp.vassal.main.Main;
import org.puretripp.vassal.utils.SubCommand;

import java.awt.*;
import java.util.HashMap;

public class NationCommandManager implements CommandExecutor {
    //Efficiency: O(n) to run help command. External Collision. KEEP THAT IN MIND!
    private String msgPrefix = ChatColor.of(new Color(140, 212, 191)) + "Vassals: ";
    private static HashMap<String, SubCommand> commands = new HashMap<>();
    private Main plugin = Main.getPlugin(Main.class);

    public NationCommandManager() {
        plugin.getCommand("vassal").setExecutor(this);
        this.commands.put("help", new TownCommands.helpCommand());
    }

    public static HashMap<String, SubCommand> getCommands() {
        return commands;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        if (!(sender instanceof Player)) throw new IllegalArgumentException("Only Players Can Run This Command!");

        if (!command.getName().equalsIgnoreCase("nation")) {
            return true;
        }
        Player p = (Player) sender;
        p.sendMessage();
        try {
            if (args == null) {
                commands.get("help").onCommand(p, args);
            } else if (args.length == 0) {
                commands.get("help").onCommand(p, args);
            } else if (!commands.containsKey(args[0])) {
                p.sendMessage(msgPrefix + "Command Does Not Exist! Check the List Below");
                commands.get("help").onCommand(p, args);
            } else {
                commands.get(args[0]).onCommand(p, args);
            }
        } catch(Exception e) {
            p.sendMessage(msgPrefix + e.getMessage());
        }
        return true;
    }
}
