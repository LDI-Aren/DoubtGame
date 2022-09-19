package com.bg.doubt.Player;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PlayerAndCard {
    private String playerId;
    private int numOfCards;
}
