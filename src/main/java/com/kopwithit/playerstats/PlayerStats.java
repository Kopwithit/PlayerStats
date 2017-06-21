package com.kopwithit.playerstats;

import com.kopwithit.playerstats.command.PlayerStatsCommand;
import com.kopwithit.playerstats.command.StatsCommand;
import com.kopwithit.playerstats.listeners.EntityDeath;
import com.kopwithit.playerstats.listeners.PlayerJoin;
import com.kopwithit.playerstats.listeners.PlayerLeave;
import com.kopwithit.playerstats.profiles.Profile;
import com.kopwithit.playerstats.profiles.ProfileLoader;
import com.kopwithit.playerstats.profiles.ProfileSaver;
import com.kopwithit.playerstats.utils.Configuration;
import com.zaxxer.hikari.HikariDataSource;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
/*
TODO add api
TODO support looking up offline players stats
TODO add permissions
 */
@Getter
public class PlayerStats extends JavaPlugin {

    private HikariDataSource hikari = new HikariDataSource();
    private Configuration configuration;

    private Map<UUID, Profile> profiles = new HashMap<UUID, Profile>();

    @Override
    public void onEnable() {
        configuration = new Configuration(this);
        configuration.setupHikari(hikari, configuration.getSETTINGS_CONFIG());

        registerListeners();
        getCommand("playerstats").setExecutor(new PlayerStatsCommand(this));
        getCommand("stats").setExecutor(new StatsCommand(this));

        /**
         * Loads all player data for players already online, useful for when server is reloaded
         */
        for (Player player : Bukkit.getOnlinePlayers()) {
            final Profile PROFILE = new Profile(player);
            new ProfileLoader(PROFILE, this).runTaskAsynchronously(this);
        }
    }

    @Override
    public void onDisable() {
        /**
         * Saves all player data to database
         */
        for (Player player : Bukkit.getOnlinePlayers()) {
            final Profile PROFILE = getRemovedProfile(player);
            new ProfileSaver(PROFILE, this).runTaskAsynchronously(this);
        }
    }
    public Profile getProfile(Player player) {
        return profiles.get(player.getUniqueId());
    }

    public Profile getRemovedProfile(Player player) {
        return profiles.remove(player.getUniqueId());
    }

    private void registerListeners() {
        PluginManager pm = Bukkit.getPluginManager();

        pm.registerEvents(new PlayerJoin(this), this);
        pm.registerEvents(new PlayerLeave(this), this);
        pm.registerEvents(new EntityDeath(this), this);
    }
}
