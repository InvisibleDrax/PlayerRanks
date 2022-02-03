package me.ice.spigot.plugins.playerranks;
import de.tr7zw.nbtapi.NBTItem;
import lombok.Getter;
import net.luckperms.api.LuckPerms;
import net.luckperms.api.model.user.User;
import net.luckperms.api.node.Node;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.HashMap;

@Getter
public class Rank {

	private static PlayerRanksPlugin plugin = PlayerRanksPlugin.getInstance();

	public static ArrayList<Rank> ranks = new ArrayList<>();
	private static HashMap<Integer, Rank> byTier = new HashMap<>();
	private static HashMap<String, Rank> byName = new HashMap<>();

	public static Rank DEFAULT = new Rank("default", "", 0);

	private ItemStack item;
	private int tier;
	private String name;
	private String displayString;

	private Rank(String name, String displayString, int tier) {
		this.name = name;
		this.tier = tier;
		this.displayString = displayString;
		item = rankItem();
		ranks.add(this);
		byTier.put(tier, this);
		byName.put(name, this);
	}

	public static Rank getRank(String name) {
		return byName.get(name);
	}

	public static Rank getRank(int tier) {
		return byTier.get(tier);
	}

	public static void setRank(Player player, Rank rank) {
		LuckPerms api = PlayerRanksPlugin.getInstance().getLuckPerms();
		User user = api.getUserManager().getUser(player.getUniqueId());
		Rank oldRank = PlayerRanksPlugin.PlayerRankData.getRank(player);

		user.data().remove(Node.builder("group." + oldRank.getName()).build());
		Node node = Node.builder("group." + rank.getName()).build();
		user.data().add(node);
		api.getUserManager().saveUser(user);
		PlayerRanksPlugin.PlayerRankData.setRank(player, rank);
	}

	public static void loadRanks() {
		FileConfiguration config = plugin.getConfig();
		ConfigurationSection section = config.getConfigurationSection("ranks");
		for (String name : section.getKeys(false)) {
			new Rank(name, section.getString(name + ".displayString"), section.getInt(name + ".tier"));
		}
	}

	private ItemStack rankItem() {
		if (tier == 0) {
			return null;
		}
		Material mat = Material.getMaterial(plugin.getConfig().getString("itemMaterial"));
		if (mat == null) {
			mat = Material.PAPER;
		}
		ItemStack item = new ItemStack(mat);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName(ChatColor.WHITE + "" + ChatColor.BOLD + "Rank: " + ChatColor.translateAlternateColorCodes('&', displayString));
		ArrayList<String> lore = new ArrayList<>();
		lore.add(ChatColor.GRAY + "Right-Click to apply this rank to your player!");
		meta.setLore(lore);
		item.setItemMeta(meta);

		NBTItem ni = new NBTItem(item);
		ni.setString("item", "playerRank");
		ni.setInteger("tier", tier);
		ni.applyNBT(item);

		return item;
	}

}
