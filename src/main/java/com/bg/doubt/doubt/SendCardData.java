package com.bg.doubt.doubt;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SendCardData {
    private String playerId;
    private String cardNum;
    private int numOfCards;
    private String nextPlayer;
    private String nextCard;
}
