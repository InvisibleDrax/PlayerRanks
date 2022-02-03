package me.ice.spigot.plugins.playerranks;

import de.tr7zw.nbtapi.NBTItem;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;

public class RankRedeemListener implements Listener {

    @EventHandler
    public void onRightClick(PlayerInteractEvent e) {
        Player p = e.getPlayer();
        ItemStack item = p.getInventory().getItemInMainHand();
        //Check if hand is offhand
        if (e.getHand() == EquipmentSlot.OFF_HAND) {
            return;
        }
        //Check if item exists
        if (item.getType().equals(Material.AIR)) {
            return;
        }
        //Check if click is Right-Click
        if (!(e.getAction() == Action.RIGHT_CLICK_AIR || e.getAction() == Action.RIGHT_CLICK_BLOCK)) {
            return;
        }
        NBTItem ni = new NBTItem(item);
        //Check if item is custom item and if it is a rank
        if (ni.getString("item").equals("playerRank")) {
            int tier = ni.getInteger("tier");
            runRankRedeem(p, Rank.getRank(tier));
        }
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent e) {
        NBTItem ni = new NBTItem(e.getItemInHand());
        //Check if item is custom item and if it is a rank
        if (ni.getString("item").equals("playerRank")) {
            e.setCancelled(true);
        }
    }

    public void runRankRedeem(Player player, Rank rank) {
        Rank currentRank = Rank.getRank(player);
        int tier = rank.getTier();
        //Check if new rank is lower than old rank
        if (!(currentRank.getTier() < tier)) {
            player.sendMessage(ChatColor.RED + "You have already redeemed a better or equivalent of this rank!");
            return;
        }
        //Set luckperms group
        Rank.setRank(player, rank);
        //Take item from inventory
        ItemStack item = player.getInventory().getItemInMainHand();
        item.setAmount(item.getAmount() - 1);
        //Front-end notifications
        player.sendMessage(ChatColor.GREEN + "Successfully redeemed " + rank.getItem().getItemMeta().getDisplayName() + ChatColor.GREEN +
                " on your account!");
        player.playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1.0f, 1.0f);
    }


}
