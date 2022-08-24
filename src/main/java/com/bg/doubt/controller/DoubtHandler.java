package com.bg.doubt.controller;

import com.bg.doubt.doubt.DoubtResult;
import com.bg.doubt.doubt.DoubtService;
import com.bg.doubt.gameMessage.GameMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;


@Controller
@Slf4j
public class DoubtHandler {
    DoubtService doubtService;
    SimpMessagingTemplate messagingTemplate;

    @Autowired
    public DoubtHandler(DoubtService doubtService, SimpMessagingTemplate messagingTemplate) {
        this.doubtService = doubtService;
        this.messagingTemplate = messagingTemplate;
    }

    @MessageMapping("/join/{roomId}")
    public GameMessage joinRoom(GameMessage msg, @DestinationVariable("roomId") String roomId){
        doubtService.joinPlayer(msg, roomId);

        return msg;
    }

    @MessageMapping("/play/{roomId}")
    @SendTo("/topic/game-room/{roomId}")
    public GameMessage playCard(GameMessage msg , @DestinationVariable("roomId") String roomId){

        log.info(" roomId : " + roomId);
        return msg;
    }

    @MessageMapping("/start/{roomId}")
    @SendTo("/topic/game-room/{roomId}")
    public GameMessage startGame(GameMessage msg, @DestinationVariable("roomId") String roomId){
        doubtService.canStartGame(msg, roomId);
        return msg;
    }

    @MessageMapping("/send/{roomId}")
    @SendTo("/topic/game-room/{roomId}")
    public GameMessage sendCard(GameMessage msg, @DestinationVariable("roomId") String roomId){
        doubtService.sendCard(msg, roomId);
        return msg;

    }

    @MessageMapping("/doubt/{roomId}")
    @SendTo("/topic/game-room/{roomId}")
    public GameMessage callDoubt(GameMessage msg, @DestinationVariable("roomId") String roomId){
        doubtService.callDoubt(msg, roomId);
        return msg;
    }
}
