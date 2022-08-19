package com.bg.doubt.controller;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RoomElement {
    String gameName;
    String roomName;
    String roomId;
}
