package com.bg.doubt.gameMessage;

import lombok.Data;

@Data
public class GameMessage {
    MessageType type;
    String userId;
    String value;
}
