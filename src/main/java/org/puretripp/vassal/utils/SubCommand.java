package org.puretripp.vassal.utils;

import jline.internal.Nullable;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;

import java.util.List;

public abstract class SubCommand {
    public abstract void onCommand(Player p, String[] args);
    public abstract String getName();
    public abstract String getDesc();
    public String name;
    public String desc;

}
