package com.bg.doubt.doubt;

import com.bg.doubt.Player.Player;
import com.bg.doubt.card.CardSetter;
import com.bg.doubt.controller.RoomElement;
import com.bg.doubt.gameMessage.GameMessage;
import com.bg.doubt.gameMessage.MessageType;
import com.google.gson.Gson;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;


@Service
public class DoubtService {
    Map<String, Doubt> gameRooms;
    Gson gson;

    public DoubtService() {
        this.gameRooms = new HashMap<>();
        gson = new Gson();
    }

    public Optional<Doubt> findRoomByRoomId(String roomId){
        return Optional.ofNullable(gameRooms.get(roomId));
    }

    public String createGameroom(String gameName, String name) {
        String id = String.format("%s-%s",gameName, UUID.randomUUID());

        Doubt doubt = new Doubt();
        doubt.setRoomName(name);

        gameRooms.put(id, doubt);

        return id;
    }

    public List<RoomElement> getRoomList() {

        return gameRooms.entrySet().stream()
                .map(e->RoomElement.builder()
                        .roomId(e.getKey())
                        .roomName(e.getValue().getRoomName())
                        .gameName("doubt")
                        .build())
                .collect(Collectors.toList());
    }

    public void canStartGame(GameMessage msg, String roomId) {

        if("1230".length() > 0){
            msg.setValue(gson.toJson(CardSetter.getCards(13)));
            return;
        }

        if(!isExistRoom(msg, roomId)){
            return;
        }

        if(gameRooms.get(roomId).numberOfPlayer() != 4){
            msg.setType(MessageType.ERROR);
            msg.setValue("게임을 시작하기 위한 인원이 부족합니다.");
            return;
        }

        msg.setValue(gson.toJson(CardSetter.getCards(13)));
    }

    public void joinPlayer(GameMessage msg, String roomId) {
        if(!isExistRoom(msg, roomId)){
            return;
        }

        if(gameRooms.get(roomId).numberOfPlayer() >= 4){
            msg.setType(MessageType.ERROR);
            msg.setValue("방안에 인원이 꽉 찼습니다.");
            return;
        }

        gameRooms.get(roomId).join(new Player(msg.getUserId(), "id"));
        msg.setValue("t");
    }

    public void sendCard(GameMessage msg, String roomId) {
        List<String> cards = List.of(gson.fromJson(msg.getValue(),String[].class));

        if(!isExistRoom(msg, roomId)){
            return;
        }

        Player nextPlayer = gameRooms.get(roomId).sendCard(cards);

        if(nextPlayer == null){
            msg.setType(MessageType.ERROR);
            msg.setValue("존재하지 않는 플레이어 입니다.");
            return;
        }

        List<String> value = List.of(nextPlayer.getName(), String.valueOf(cards.size()));
        msg.setValue(gson.toJson(value));
    }

    private boolean isExistRoom(GameMessage msg, String roomId){
        if(!gameRooms.containsKey(roomId)){
            msg.setType(MessageType.ERROR);
            msg.setValue("게임방이 존재하지 않습니다.");
            return false;
        }

        return true;
    }

    public void callDoubt(GameMessage msg, String roomId) {
        if(!isExistRoom(msg, roomId)){
            return;
        }

        gameRooms.get(roomId).callDoubt(msg.getUserId());
    }
}
