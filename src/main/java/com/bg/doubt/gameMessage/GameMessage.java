package com.bg.doubt.gameMessage;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class GameMessage {
    MessageType type;
    String userId;
    String value;
}
