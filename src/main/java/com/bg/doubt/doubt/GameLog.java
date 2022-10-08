package com.bg.doubt.doubt;

import com.bg.doubt.Player.Player;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
public class GameLog {
    String gameId;
    String playerIds;
    String playerCards;
    String winPlayerId;
    LocalDate now;
    StringBuilder log;

    public GameLog(String gameId){
        this.gameId = gameId;
        now = LocalDate.now();
        log = new StringBuilder();
        log.append("START");
    }

    public void setPlayerIds(List<Player> players) {
        playerIds = players.stream()
                .map(Player::getId)
                .collect(Collectors.joining("\",\"", "[\"", "\"]"));
    }

    public List<String> getPlayerIds() {
        return (new Gson()).fromJson(playerIds, new TypeToken<List<String>>(){}.getType());
    }

    public void setPlayerCards(List<Player> players){
        Map<String, String> map = players.stream()
                .collect(Collectors.toMap(Player::getId, Player::getCardsToString));

        playerCards = (new Gson()).toJson(map);
    }

    public void setLog(String log){
        this.log.append("-").append(log);
    }

    public String getLog(){ return log.toString(); }
}
