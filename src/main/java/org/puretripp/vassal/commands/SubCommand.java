package org.puretripp.vassal.commands;

import org.bukkit.entity.Player;

public abstract class SubCommand {
    public abstract void onCommand(Player p, String[] args);
    public abstract String getName();
    public abstract String getDesc();
    public String name;
    public String desc;
}
