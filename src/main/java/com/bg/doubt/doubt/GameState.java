package com.bg.doubt.doubt;

import lombok.Setter;

@Setter
public class GameState {
    NowProcess now = NowProcess.BEFORE_GAME;
    String turnPlayerId;

    boolean canStart(){
        return now.equals(NowProcess.BEFORE_GAME);
    }

    boolean canSendCard(String playerId){
        return playerId.equals(turnPlayerId) && (now.equals(NowProcess.TURN));
    }

    boolean canDoubt(){
        return now.equals(NowProcess.STANDBY_DOUBT);
    }
}

enum NowProcess{
    BEFORE_GAME, TURN, STANDBY_DOUBT
}
