package me.ice.spigot.plugins.playerranks;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.util.StringUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class GiveCommand implements CommandExecutor, TabCompleter {
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if (strings.length < 2) {
            commandSender.sendMessage(ChatColor.RED + "Invalid syntax! Usage: /prankgive [playerName] [rankName]");
            return true;
        }
        ArrayList<String> players = new ArrayList<>();
        for (Player p : Bukkit.getOnlinePlayers()) {
            players.add(p.getName());
        }
        if (!players.contains(strings[0])) {
            commandSender.sendMessage(ChatColor.RED + "This player is not online!");
            return true;
        }
        // If rank doesn't exist or rank is default rank (default rank is tier 0)
        if (!Rank.ranks.contains(Rank.getRank(strings[1])) || Rank.getRank(strings[1]).getTier() < 1) {
            commandSender.sendMessage(ChatColor.RED + "This rank does not exist!");
            return true;
        }
        Player recipient = Bukkit.getPlayer(strings[0]);
        if (recipient.getInventory().firstEmpty() == -1) {
            commandSender.sendMessage(ChatColor.RED + "This player's inventory is full!");
            return true;
        }
        Rank rank = Rank.getRank(strings[1]);
        recipient.getInventory().addItem(rank.getItem());
        recipient.playSound(recipient.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1.0f, 1.0f);
        recipient.sendMessage(ChatColor.GREEN + "You have recieved " + ChatColor.translateAlternateColorCodes('&',
                rank.getDisplayString()) + ChatColor.GREEN + " rank.");
        if (!commandSender.equals(recipient)) {
            commandSender.sendMessage(ChatColor.GREEN + "Successfully gave " + recipient.getName() + " " + ChatColor.translateAlternateColorCodes('&',
                    rank.getDisplayString() + ChatColor.GREEN + " rank."));
        }
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender commandSender, Command command, String s, String[] strings) {
        if (strings.length == 1) {
            ArrayList<String> pNames = new ArrayList<>();
            for (Player p : Bukkit.getOnlinePlayers()) {
                if (commandSender instanceof Player) {
                    if (((Player) commandSender).canSee(p)) {
                        pNames.add(p.getName());
                    }
                } else {
                    pNames.add(p.getName());
                }
            }
            final List<String> completions = new ArrayList<>();
            StringUtil.copyPartialMatches(strings[0], pNames, completions);
            Collections.sort(completions);
            return completions;
        }
        if (strings.length == 2) {
            ArrayList<String> ranks = new ArrayList<>();
            for (Rank r : Rank.ranks) {
                if (r.getTier() < 1) {
                    continue;
                }
                ranks.add(r.getName());
            }
            final List<String> completions = new ArrayList<>();
            StringUtil.copyPartialMatches(strings[1], ranks, completions);
            Collections.sort(completions);
            return completions;
        }
        return null;
    }
}
