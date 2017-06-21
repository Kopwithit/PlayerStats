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
public class StatsCommand implements CommandExecutor {

    private PlayerStats plugin;

    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (cmd.getName().equalsIgnoreCase("stats")) {
            if (args.length == 0) {
                if (!(sender instanceof Player)) {
                    sender.sendMessage("Console can not lookup its stats!");
                    return true;
                } else {
                    Player player = (Player) sender;
                    Profile profile = plugin.getProfiles().get(player.getUniqueId());
                    player.sendMessage(profile.toString());
                    return true;
                }
            } else {
                Player target = Bukkit.getPlayer(args[0]);
                if (target == null) {
                    sender.sendMessage(Utils.color("&6" + args[0] + " &cis offline or doesn't exist!"));
                    return true;
                } else {
                    sender.sendMessage(plugin.getProfile(target).toString());
                    return true;
                }
            }
        }
        return false;
    }
}
