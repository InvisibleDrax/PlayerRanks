package me.ice.spigot.plugins.playerranks;

import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;
import net.luckperms.api.LuckPerms;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.UUID;

public class PlayerRanksPlugin extends JavaPlugin {

    private static PlayerRanksPlugin instance;

    public void onEnable() {
        instance = this;
        saveResource("config.yml", false);
        saveResource("playerRankData.yml", false);
        PlayerRankData.config = YamlConfiguration.loadConfiguration(new File(getDataFolder(), "playerRankData.yml"));
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

    public static class PlayerRankData {
        static FileConfiguration config;

        @SneakyThrows
        public static void setRank(Player player, Rank rank) {
            config.set(player.getUniqueId().toString(), rank.getName());
            config.save(new File(PlayerRanksPlugin.getInstance().getDataFolder(), "playerRankData.yml"));
        }

        public static Rank getRank(Player player) {
            Rank rank = Rank.getRank(config.getString(player.getUniqueId().toString()));
            if (rank == null) {
                rank = Rank.DEFAULT;
            }
            return rank;
        }
    }

}
