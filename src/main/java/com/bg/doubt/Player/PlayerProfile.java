package com.bg.doubt.Player;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
public class PlayerProfile {
    private String playerId;
    private String playerName;

    public static PlayerProfile getProfile(Player player){
        return new PlayerProfile(player.getId(), player.getName());
    }
}
