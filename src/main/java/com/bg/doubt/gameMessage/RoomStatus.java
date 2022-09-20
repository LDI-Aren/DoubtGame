package com.bg.doubt.gameMessage;

import com.bg.doubt.Player.PlayerAndCard;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class RoomStatus {
    PlayerAndCard[] playerAndCards;
    int FieldNum;
    String turnPlayer;

    List<String> myCards;
}
