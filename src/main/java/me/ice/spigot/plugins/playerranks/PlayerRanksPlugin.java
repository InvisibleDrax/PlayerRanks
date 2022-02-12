package me.ice.spigot.plugins.playerranks;

import net.luckperms.api.LuckPerms;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Objects;

public class PlayerRanksPlugin extends JavaPlugin {

    private static PlayerRanksPlugin instance;
    public static Material RANKMAT;

    public void onEnable() {
        instance = this;
        saveResource("config.yml", false);
        Objects.requireNonNull(getCommand("prankgive")).setExecutor(new GiveCommand());
        getServer().getPluginManager().registerEvents(new RankRedeemListener(), this);
        RANKMAT = Material.getMaterial(Objects.requireNonNull(PlayerRanksPlugin.getInstance().getConfig().getString("itemMaterial")));
        Rank.loadRanks();
        ConfigVersionUpdate.update();
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
