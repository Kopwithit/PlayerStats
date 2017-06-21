package com.kopwithit.playerstats.listeners;

import com.kopwithit.playerstats.PlayerStats;
import com.kopwithit.playerstats.profiles.Profile;
import com.kopwithit.playerstats.profiles.ProfileSaver;
import lombok.AllArgsConstructor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

@AllArgsConstructor
public class PlayerLeave implements Listener {

    private PlayerStats plugin;

    @EventHandler
    public void onPlayerLeave(PlayerQuitEvent event) {
        final Profile PROFILE = plugin.getRemovedProfile(event.getPlayer());
        new ProfileSaver(PROFILE, plugin).runTaskAsynchronously(plugin);
    }

}
