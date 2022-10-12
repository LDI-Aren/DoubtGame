package com.bg.doubt.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor
@Table(name ="GAME_RESULT")
public class GameResult{
    @Id
    @Column(name = "GAME_ID")
    String gameId;

    @Column(name="PLAYER_IDS")
    String playerIds;

    @Column(name="PLAYER_CARDS")
    String playerCards;

    @Column(name="WIN_PLAYER_ID")
    String winPlayerId;

    @Column(name="NOW")
    LocalDateTime now;

    @Column(name="LOG")
    String log;

    @Builder
    public GameResult(String gameId, String playerIds, String playerCards, String winPlayerId, LocalDateTime now, String log) {
        this.gameId = gameId;
        this.playerIds = playerIds;
        this.playerCards = playerCards;
        this.winPlayerId = winPlayerId;
        this.now = now;
        this.log = log;
    }
}
