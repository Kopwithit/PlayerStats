package com.kopwithit.playerstats.utils;

import com.kopwithit.playerstats.PlayerStats;
import com.zaxxer.hikari.HikariDataSource;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class Configuration {

    private File SETTINGS_FILE;
    private final YamlConfiguration SETTINGS_CONFIG;
    private PlayerStats plugin;

    public Configuration(PlayerStats plugin) {
        this.plugin = plugin;
        this.SETTINGS_FILE = new File(this.plugin.getDataFolder(), "settings.yml");
        this.SETTINGS_CONFIG = YamlConfiguration.loadConfiguration(SETTINGS_FILE);
        if (!SETTINGS_CONFIG.isConfigurationSection("Database")) {
            SETTINGS_CONFIG.set("Database.Address", "localhost:3306");
            SETTINGS_CONFIG.set("Database.Schema", "playerstats");
            SETTINGS_CONFIG.set("Database.Username", "root");
            SETTINGS_CONFIG.set("Database.Password", "root");
            saveSettingsConfig();
        }
    }

    public YamlConfiguration getSETTINGS_CONFIG() {
        return SETTINGS_CONFIG;
    }

    public void saveSettingsConfig() {
        try {
            SETTINGS_CONFIG.save(SETTINGS_FILE);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setupHikari(HikariDataSource hikari, FileConfiguration settings) {
        String address = settings.getString("Database.Address");
        String database = settings.getString("Database.Schema");
        String username = settings.getString("Database.Username");
        String password = settings.getString("Database.Password");

        hikari.setMaximumPoolSize(10);
        hikari.setDataSourceClassName("com.mysql.jdbc.jdbc2.optional.MysqlDataSource");
        hikari.addDataSourceProperty("serverName", address.split(":")[0]);
        hikari.addDataSourceProperty("port", address.split(":")[1]);
        hikari.addDataSourceProperty("databaseName", database);
        hikari.addDataSourceProperty("user", username);
        hikari.addDataSourceProperty("password", password);

        Connection connection = null;

        try {
            connection = hikari.getConnection();

            PreparedStatement preparedStatement = connection.prepareStatement("CREATE TABLE IF NOT EXISTS `players` (`uuid` varchar(36) NOT NULL, `name` varchar(16) NOT NULL, `kills` int(11) NOT NULL" +
                    ", `deaths` int(11) NOT NULL, PRIMARY KEY (`uuid`)) ENGINE=InnoDB DEFAULT CHARSET=latin1;");
            preparedStatement.execute();
            preparedStatement.close();
        } catch (SQLException e) {
            e.printStackTrace();
            plugin.getLogger().severe("Plugin is disabled! Error setting up MySQL!");
            Bukkit.getServer().getPluginManager().disablePlugin(plugin);
        } finally {
            if(connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
