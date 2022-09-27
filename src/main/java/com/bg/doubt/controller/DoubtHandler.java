package com.bg.doubt.controller;

import com.bg.doubt.doubt.DoubtService;
import com.bg.doubt.gameMessage.*;
import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import java.util.List;


@Controller
@Slf4j
public class DoubtHandler {
    DoubtService doubtService;
    SimpMessagingTemplate messagingTemplate;
    Gson gson;

    @Autowired
    public DoubtHandler(DoubtService doubtService, SimpMessagingTemplate messagingTemplate) {
        this.doubtService = doubtService;
        this.messagingTemplate = messagingTemplate;
        gson = new Gson();
    }

    private GameMessage messageWrapper(GameMessage msg, String roomId, MessageWrapper<GameMessage, String , Object> func){
        GameMessage returnMessage = GameMessage.builder()
                .type(msg.getType())
                .playerId(msg.getPlayerId())
                .build();

        try {
            Object result = func.apply(msg, roomId);
            returnMessage.setValue(gson.toJson(result));
        } catch (Exception e) {
            returnMessage.setType(MessageType.ERROR);
            returnMessage.setValue(e.getMessage());
        }

        return returnMessage;
    }

    public void sendToPlayer(String player, GameMessage msg){
        messagingTemplate.convertAndSend(String.format("/topic/player/%s",player), msg);
    }

    public void sendToPlayer(List<String> players, GameMessage msg){
        players.forEach(
                p -> messagingTemplate.convertAndSend(String.format("/topic/player/%s",p), msg)
        );
    }

    @MessageMapping("/join/{roomId}")
    public void joinRoom(GameMessage msg, @DestinationVariable("roomId") String roomId){
        String playerId = msg.getPlayerId();

        GameMessage gameMessage = messageWrapper(msg, roomId, (gm, s) -> doubtService.joinPlayer(gm, s));
        if(!gameMessage.getType().equals(MessageType.ERROR)){
            sendToPlayer(doubtService.getDestinationPlayerId(roomId, playerId), msg);
        }

        sendToPlayer(msg.getPlayerId(), gameMessage);
    }

    @MessageMapping("/ready/{roomId}")
    @SendTo("/topic/game-room/{roomId}")
    public GameMessage ready(GameMessage msg , @DestinationVariable("roomId") String roomId){
        return messageWrapper(msg, roomId,  (gm, s) -> doubtService.gameReady(gm, s));
    }

    @MessageMapping("/start/{roomId}")
    public void startGame(GameMessage msg, @DestinationVariable("roomId") String roomId){
        GameMessage gameMessage = messageWrapper(msg, roomId,(gm, s) -> doubtService.startGame(gm, s));
        sendToPlayer(msg.getPlayerId(), gameMessage);
    }

    @MessageMapping("/send/{roomId}")
    @SendTo("/topic/game-room/{roomId}")
    public GameMessage sendCard(GameMessage msg, @DestinationVariable("roomId") String roomId){
        GameMessage gameMessage = messageWrapper(msg, roomId, (gm, s) -> doubtService.sendCard(gm, s));

        if(gameMessage.getType().equals(MessageType.SEND)){
            sendToPlayer(msg.getPlayerId(), successMessage(msg.getPlayerId()));
        }

        return gameMessage;
    }

    private GameMessage successMessage(String playerId) {
        return GameMessage.builder().
                playerId(playerId).
                type(MessageType.SUCCESS).
                build();
    }

    @MessageMapping("/doubt/{roomId}")
    public void callDoubt(GameMessage msg, @DestinationVariable("roomId") String roomId) {
        GameMessage gameMessage = messageWrapper(msg, roomId, (gm, s) -> doubtService.callDoubt(gm, s));

        List<String> players = doubtService.getPlayerIdByGameMessage(roomId, gameMessage.getType());
        sendToPlayer(players, gameMessage);
    }
}
