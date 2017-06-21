package com.kopwithit.playerstats.profiles;

import com.kopwithit.playerstats.PlayerStats;
import lombok.AllArgsConstructor;
import org.bukkit.scheduler.BukkitRunnable;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

@AllArgsConstructor
public class ProfileSaver extends BukkitRunnable {

    private Profile profile;
    private PlayerStats plugin;

    private static final String SAVE = "UPDATE players SET kills=?, deaths=? WHERE uuid=?";

    public void run() {
        Connection connection = null;

        try {
            connection = plugin.getHikari().getConnection();

            PreparedStatement preparedStatement = connection.prepareStatement(SAVE);
            preparedStatement.setInt(1, profile.getKills());
            preparedStatement.setInt(2, profile.getDeaths());
            preparedStatement.setString(3, profile.getUuid().toString());
            preparedStatement.execute();
            preparedStatement.close();
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
