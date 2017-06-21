package com.kopwithit.playerstats.profiles;

import com.kopwithit.playerstats.utils.Utils;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.entity.Player;

import java.util.UUID;

@Getter

public class Profile {

    private String name;
    private UUID uuid;

    @Setter private int kills;
    @Setter private int deaths;

    public Profile(Player player) {
        this.name = player.getName();
        this.uuid = player.getUniqueId();
        this.kills = 0;
        this.deaths = 0;
    }

    @Override
    public String toString() {
        return Utils.color("&a&l&m-------&r[ &l" + name + " &r]&a&l&m-------\n" + "" +
                "&eKills: &7" + kills + "\n" +
                "&eDeaths: &7" + deaths + "\n");
    }
}
