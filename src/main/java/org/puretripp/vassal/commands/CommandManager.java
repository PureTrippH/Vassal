package org.puretripp.vassal.commands;

import net.md_5.bungee.api.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.puretripp.vassal.main.Main;

import java.awt.*;
import java.util.HashMap;

public class CommandManager implements CommandExecutor {
    //Efficiency: O(n) to run help command. External Collision. KEEP THAT IN MIND!
    private String msgPrefix = ChatColor.of(new Color(140, 212, 191)) + "Vassals: ";
    private static HashMap<String, SubCommand> commands = new HashMap<>();
    private Main plugin = Main.getPlugin(Main.class);

    public CommandManager() {
        plugin.getCommand("vassal").setExecutor(this);
        this.commands.put("pm", new TownCommands.SelfCommand());
        this.commands.put("invite", new TownCommands.inviteCommand());
        this.commands.put("help", new TownCommands.helpCommand());
        this.commands.put("create", new TownCommands.createCommand());
        this.commands.put("border", new TownCommands.borderCommand());
        this.commands.put("claim", new TownCommands.claimCommand());
        this.commands.put("subclaim", new TownCommands.subclaimCommand());
        this.commands.put("menu", new TownCommands.menu());
    }

    public static HashMap<String, SubCommand> getCommands() {
        return commands;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        if (!(sender instanceof Player)) throw new IllegalArgumentException("Only Players Can Run This Command!");

        if (!command.getName().equalsIgnoreCase("vassal")) {
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
