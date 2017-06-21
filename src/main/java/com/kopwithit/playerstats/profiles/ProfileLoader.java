package com.kopwithit.playerstats.profiles;

import com.kopwithit.playerstats.PlayerStats;
import org.bukkit.scheduler.BukkitRunnable;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ProfileLoader extends BukkitRunnable {

    private Profile profile;
    private PlayerStats plugin;

    private static final String INSERT = "INSERT INTO players VALUES(?, ?, ?, ?) ON DUPLICATE KEY UPDATE name=?";
    private static final String SELECT = "SELECT kills,deaths FROM players WHERE uuid=?";

    public ProfileLoader(Profile profile, PlayerStats plugin) {
        this.profile = profile;
        this.plugin = plugin;
    }
    public void run() {
        Connection connection = null;

        try {
            connection = plugin.getHikari().getConnection();

            PreparedStatement preparedStatement = connection.prepareStatement(INSERT);
            preparedStatement.setString(1, profile.getUuid().toString());
            preparedStatement.setString(2, profile.getName());
            preparedStatement.setInt(3, 0);
            preparedStatement.setInt(4, 0);
            preparedStatement.setString(5, profile.getName());
            preparedStatement.execute();

            preparedStatement = connection.prepareStatement(SELECT);
            preparedStatement.setString(1, profile.getUuid().toString());

            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                profile.setKills(resultSet.getInt("kills"));
                profile.setDeaths(resultSet.getInt("deaths"));
                plugin.getProfiles().put(profile.getUuid(), profile);
                plugin.getLogger().info("Loaded " + profile.getName() + " profile!");
            }

            preparedStatement.close();
            resultSet.close();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
