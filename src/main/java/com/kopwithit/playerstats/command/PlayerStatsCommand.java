package com.kopwithit.playerstats.command;

import com.kopwithit.playerstats.PlayerStats;
import com.kopwithit.playerstats.profiles.Profile;
import com.kopwithit.playerstats.utils.Utils;
import lombok.AllArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@AllArgsConstructor
public class PlayerStatsCommand implements CommandExecutor {

    private PlayerStats plugin;

    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (cmd.getName().equalsIgnoreCase("playerstats")) {
            if (args.length == 1) {
                sender.sendMessage(Utils.color("&cIncorrect usage /playerstats (task) (player) (data)"));
                return true;
            } else if (args.length == 2) {
                if (args[0].equalsIgnoreCase("reset")) {
                    Player target = Bukkit.getPlayer(args[1]);
                    if (target == null) {
                        sender.sendMessage(Utils.color("&6" + args[1] + " &cis offline or doesn't exist!"));
                        return true;
                    } else {
                        Profile profile = plugin.getProfile(target);
                        profile.setKills(0);
                        plugin.getProfiles().put(target.getUniqueId(), profile);
                        sender.sendMessage(Utils.color("&aYou have reset &6" + target.getName() + " &astats!"));
                        target.sendMessage(Utils.color("&aYour stats have been reset!"));
                        return true;
                    }
                } else if (args[0].equalsIgnoreCase("kills") || args[0].equalsIgnoreCase("deaths")) {
                    sender.sendMessage(Utils.color("&cIncorrect usage! /playerstats " + args[0] + " (player) (value)!"));
                } else {
                    sender.sendMessage(Utils.color("&6Help Menu: \n" +
                            "&b/stats - Displays stats\n" +
                            "/adminstats - Modify players stats"));
                    return true;
                }
            } else if (args.length == 3) {
                Player target = Bukkit.getPlayer(args[1]);
                if (target == null) {
                    sender.sendMessage(Utils.color("&6" + args[1] + " &cis offline or doesn't exist!"));
                    return true;
                } else {
                    Profile profile = plugin.getProfile(target);
                    int value = Integer.parseInt(args[2]);
                    switch (args[0].toLowerCase()) {
                        case "kills":
                            profile.setKills(value);
                            sender.sendMessage(Utils.color("&aYou set " + target.getName() + " kills to " + value +"!"));
                            return true;
                        case "deaths":
                            profile.setDeaths(value);
                            sender.sendMessage(Utils.color("&aYou set " + target.getName() + " deaths to " + value +"!"));
                            return true;
                        default:
                            sender.sendMessage(Utils.color("&cUnknown task " + args[0] + "!"));
                            return true;
                    }
                }
            } else {
                sender.sendMessage(Utils.color("&6Playerstats by Kopwithit \n" +
                                    "&bA basic stats plugin that stores kills and deaths!\n" +
                                    "&e/stats (player) - &7Displays the stats of a player!\n" +
                                    "e/playersstats (reset/kills/deaths) (player) (amount) - &7Allows to change a players stats"));
                return true;
            }
        }
        return false;
    }
}
