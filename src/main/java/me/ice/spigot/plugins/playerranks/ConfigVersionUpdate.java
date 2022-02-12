package me.ice.spigot.plugins.playerranks;

import org.bukkit.configuration.file.FileConfiguration;

public class ConfigVersionUpdate {

    private static final PlayerRanksPlugin plugin = PlayerRanksPlugin.getInstance();

    public static void update() {
        float version = Float.parseFloat(plugin.getDescription().getVersion());
        if (plugin.getConfig().getDouble("version") == version) {
            return;
        }
        plugin.saveResource("config.yml", true);
        FileConfiguration config = plugin.getConfig();
        config.set("ranks", null);
        for (Rank rank : Rank.ranks) {
            addToConfig(rank);
        }
        config.set("itemMaterial", PlayerRanksPlugin.RANKMAT);
    }

    private static void addToConfig(Rank rank) {
        FileConfiguration config = plugin.getConfig();
        config.set("ranks." + rank.getName() + "tier", rank.getTier());
        config.set("ranks." + rank.getName() + "tier", rank.getDisplayString());
    }

}
