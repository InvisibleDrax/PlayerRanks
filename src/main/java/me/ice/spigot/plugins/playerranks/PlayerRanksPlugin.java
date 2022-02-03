package me.ice.spigot.plugins.playerranks;

import net.luckperms.api.LuckPerms;
import org.bukkit.Bukkit;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

public class PlayerRanksPlugin extends JavaPlugin {

    private static PlayerRanksPlugin instance;

    public void onEnable() {
        instance = this;
        saveResource("config.yml", false);
        getCommand("prankgive").setExecutor(new GiveCommand());
        getServer().getPluginManager().registerEvents(new RankRedeemListener(), this);
        Rank.loadRanks();
    }

    public void onDisable() {

    }

    public LuckPerms getLuckPerms() {
        RegisteredServiceProvider<LuckPerms> provider = Bukkit.getServicesManager().getRegistration(LuckPerms.class);
        if (provider != null) {
            return provider.getProvider();
        }
        return null;
    }

    public static PlayerRanksPlugin getInstance() {
        return instance;
    }

}
