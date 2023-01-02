package org.puretripp.vassal.main;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;

import org.bson.Document;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import org.puretripp.vassal.commands.MasterCommand;
import org.puretripp.vassal.commands.NationsMaster;
import org.puretripp.vassal.events.Events;
import org.puretripp.vassal.menus.Menu;
import org.puretripp.vassal.utils.VassalWorld;

import java.util.logging.Level;
import java.util.logging.Logger;

public final class Main extends JavaPlugin {
    private String uri = "mongodb+srv://PluginDevGem:L6da01wLdVw1WDgF@plugindata.wriycde.mongodb.net/?retryWrites=true&w=majority";
    private MongoClient client;
    //Holds all of the Server's data from the curent session.
    public static VassalWorld currentInstance;
    @Override
    public void onEnable() {
        // Plugin startup logic
        this.getCommand("vassal").setExecutor(new MasterCommand());
        this.getCommand("nation").setExecutor(new NationsMaster());
        currentInstance = new VassalWorld();
        //Register Events
        getServer().getPluginManager().registerEvents(new Events(), this);

        //Try connecting to the database
        try {
            Logger.getLogger("org.mongodb.driver").setLevel(Level.WARNING);
            Bukkit.getLogger().info(ChatColor.DARK_RED + "----------------------");
            Bukkit.getLogger().info(ChatColor.RED + "VASSALS: Enabled");
            Bukkit.getLogger().info(ChatColor.DARK_RED + "----------------------");
            client = MongoClients.create(uri);
            MongoDatabase database = client.getDatabase("Vassals");
            MongoCollection<Document> fetchedNations = database.getCollection("Townships");
           /* fetchedNations.find().forEach(e -> {
                Township.nations.put(e.getString("name")

            });*/

        } catch(Exception e) {
            Bukkit.getLogger().severe(e.getMessage());
        }
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
