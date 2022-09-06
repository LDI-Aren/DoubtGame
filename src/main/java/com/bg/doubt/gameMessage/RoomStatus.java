package com.bg.doubt.gameMessage;

import lombok.Builder;
import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
@Builder
public class RoomStatus {
    Map<String, Integer> playerAndCards;
    int FieldNum;

    List<String> cards;
}
