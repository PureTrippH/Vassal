package org.puretripp.vassal.main;

import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import com.mongodb.client.MongoClient;
import org.puretripp.vassal.commands.CommandManager;
import org.puretripp.vassal.events.Events;
import org.puretripp.vassal.utils.general.VassalWorld;

import java.util.logging.Level;
import java.util.logging.Logger;

public final class Main extends JavaPlugin {
    //Btw this password is Old
    private String uri = "lolNOPENOTIT";
    ProtocolManager manager;
    private MongoClient client;
    //Holds all of the Server's data from the curent session.
    public static VassalWorld currentInstance;
    private Economy econ;
    @Override
    public void onEnable() {
        // Plugin startup logic
        this.getCommand("vassal").setExecutor(new CommandManager());
        currentInstance = new VassalWorld();
        //Register Events
        getServer().getPluginManager().registerEvents(new Events(), this);

        //Try connecting to the database
        try {
            //Logger.getLogger("org.mongodb.driver").setLevel(Level.WARNING);
            setUpDependencies();
            Bukkit.getLogger().info(ChatColor.DARK_RED + "----------------------");
            Bukkit.getLogger().info(ChatColor.RED + "VASSALS: " + ChatColor.GREEN + "Enabled");
            Bukkit.getLogger().info(ChatColor.DARK_RED + "----------------------");
        } catch(Exception e) {
            Bukkit.getLogger().severe(e.getMessage());
            Bukkit.getLogger().info(ChatColor.DARK_RED + "----------------------");
            Bukkit.getLogger().info(ChatColor.RED + "VASSALS: DISABLED");
            Bukkit.getLogger().info(ChatColor.DARK_RED + "----------------------");
            Bukkit.getPluginManager().disablePlugin(this);
            return;
        }
    }
    private boolean setUpDependencies() throws ClassNotFoundException {
        if (Bukkit.getPluginManager().getPlugin("Vault") == null) {
            throw new ClassNotFoundException("Vault is Required to Operate Vassals");
        }
        if (Bukkit.getPluginManager().getPlugin("ProtocolLib") == null) {
            throw new ClassNotFoundException("Protocollib is Required to Operate Vassals");
        }
        RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null) {
            return false;
        }
        econ = rsp.getProvider();
        return econ != null;
        return econ != null;
    }

    public Economy getEconomy() {
        return econ;
    }

    @Override
    public void onDisable() {
        //Shuts Down ALL Threads
        Bukkit.getServer().getScheduler().cancelTasks(this);
    }

}
