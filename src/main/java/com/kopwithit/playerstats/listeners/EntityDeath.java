package com.kopwithit.playerstats.listeners;

import com.kopwithit.playerstats.PlayerStats;
import com.kopwithit.playerstats.profiles.Profile;
import lombok.AllArgsConstructor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;

@AllArgsConstructor
public class EntityDeath implements Listener {

    private PlayerStats plugin;

    @EventHandler
    public void onEntityDeath(EntityDeathEvent event) {
        if (event.getEntity() instanceof Player) {
            Player player = (Player) event.getEntity();
            Profile profile = plugin.getProfile(player);
            profile.setDeaths(profile.getDeaths() + 1);
            if (player.getKiller() != null) {
                profile = plugin.getProfile(player.getKiller());
                profile.setKills(profile.getKills() + 1);
            }
        }
    }
}
