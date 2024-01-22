package org.puretripp.vassal.main;

import com.comphenix.protocol.ProtocolManager;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import com.mongodb.client.MongoClient;
import org.lustrouslib.command.CommandManager;
import org.lustrouslib.config.ConfigBuilder;
import org.lustrouslib.config.ConfigFile;
import org.lustrouslib.event.PlayerStateHandler;
import org.lustrouslib.wrapper.StateHandler;
import org.puretripp.vassal.commands.TownCommands;
import org.puretripp.vassal.events.Events;
import org.puretripp.vassal.utils.general.VassalWorld;
import org.puretripp.vassal.utils.general.VassalsPlayer;

public final class Main extends JavaPlugin {
    //Btw this password is Old
    private String uri = "lolNOPENOTIT";
    ProtocolManager manager;
    private MongoClient client;
    //Holds all of the Server's data from the curent session.
    public static VassalWorld currentInstance;
    public static ConfigFile config;
    private Economy econ;
    private PlayerStateHandler stateHander;
    @Override
    public void onEnable() {
        currentInstance = VassalWorld.getWorldInstance();
        config = (new ConfigBuilder("settings.yml", this)).build();

        StateHandler<VassalsPlayer> globalState = new StateHandler(getPlugin(Main.class), currentInstance);
        CommandManager towncmd = new CommandManager("vassal", globalState);
        towncmd.registerCommand("pm", new TownCommands.SelfCommand());
        towncmd.registerCommand("invite", new TownCommands.inviteCommand());
        towncmd.registerCommand("create", new TownCommands.createCommand());
        towncmd.registerCommand("border", new TownCommands.borderCommand());
        towncmd.registerCommand("claim", new TownCommands.claimCommand());
        towncmd.registerCommand("subclaim", new TownCommands.subclaimCommand());
        towncmd.registerCommand("menu", new TownCommands.menu());
        //Register Events
        getServer().getPluginManager().registerEvents(new Events(), this);
        PlayerStateHandler stateHander = new PlayerStateHandler(globalState);
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