package org.puretripp.vassal.main;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import org.bson.Document;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import org.puretripp.vassal.commands.CommandManager;
import org.puretripp.vassal.commands.MasterCommand;
import org.puretripp.vassal.commands.NationsMaster;
import org.puretripp.vassal.events.Events;
import org.puretripp.vassal.menus.Menu;
import org.puretripp.vassal.utils.VassalWorld;

import java.util.logging.Level;
import java.util.logging.Logger;

public final class Main extends JavaPlugin {
    //Btw this password is Old
    private String uri = "lolNOPENOTIT";
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
            if (!setupEconomy()) {
                this.getLogger().severe("Disabled due to no Vault dependency found!");
                Bukkit.getPluginManager().disablePlugin(this);
                return;
            }
            Logger.getLogger("org.mongodb.driver").setLevel(Level.WARNING);
            Bukkit.getLogger().info(ChatColor.DARK_RED + "----------------------");
            Bukkit.getLogger().info(ChatColor.RED + "VASSALS: Enabled");
            Bukkit.getLogger().info(ChatColor.DARK_RED + "----------------------");
            /*client = MongoClients.create(uri);
            MongoDatabase database = client.getDatabase("Vassals");
            MongoCollection<Document> fetchedNations = database.getCollection("Townships");*/
           /* fetchedNations.find().forEach(e -> {
                Township.nations.put(e.getString("name")

            });*/

        } catch(Exception e) {
            Bukkit.getLogger().severe(e.getMessage());
        }
    }
    private boolean setupEconomy() {
        if (Bukkit.getPluginManager().getPlugin("Vault") == null) {
            return false;
        }
        RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null) {
            return false;
        }
        econ = rsp.getProvider();
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
