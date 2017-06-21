package com.kopwithit.playerstats.listeners;

import com.kopwithit.playerstats.PlayerStats;
import com.kopwithit.playerstats.profiles.Profile;
import com.kopwithit.playerstats.profiles.ProfileLoader;
import lombok.AllArgsConstructor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;


@AllArgsConstructor
public class PlayerJoin implements Listener {

    private PlayerStats plugin;

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        final Profile PROFILE = new Profile(event.getPlayer());
        new ProfileLoader(PROFILE, plugin).runTaskAsynchronously(plugin);
    }
}
