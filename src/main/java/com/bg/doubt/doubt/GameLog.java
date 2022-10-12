package com.bg.doubt.doubt;

import com.bg.doubt.Player.Player;
import com.bg.doubt.entity.GameResult;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GameLog {
    String gameId;
    String playerIds;
    String playerCards;
    String winPlayerId;
    LocalDateTime now;
    StringBuilder log;

    public GameLog(String gameId){
        this.gameId = gameId;
        now = LocalDateTime.now();
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

    public void setSendCardLog(String log){
        this.log.append("-").append(log);
    }

    public void setDoubtLog(String playerId){
        this.log.append("-doubt:").append(playerId);
    }

    public String getLog(){ return log.toString(); }

    public GameResult toGameResult(){
        return GameResult.builder()
                .gameId(this.gameId)
                .playerIds(this.playerIds)
                .playerCards(this.playerCards)
                .winPlayerId(this.winPlayerId)
                .now(this.now)
                .log(this.log.toString())
                .build();
    }

    public static GameLog gameResultToGameLog(GameResult gameResult){
        return GameLog.builder()
                .gameId(gameResult.getGameId())
                .playerIds(gameResult.getPlayerIds())
                .playerCards(gameResult.getPlayerCards())
                .winPlayerId(gameResult.getWinPlayerId())
                .now(gameResult.getNow())
                .log(new StringBuilder(gameResult.getLog()))
                .build();
    }
}
