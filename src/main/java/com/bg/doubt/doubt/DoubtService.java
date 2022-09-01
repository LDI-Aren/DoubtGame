package com.bg.doubt.doubt;

import com.bg.doubt.Player.Player;
import com.bg.doubt.card.CardList;
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

    public List<GameMessage> canStartGame(GameMessage msg, String roomId) {

        /*
        if(!gameRooms.containsKey(roomId)){
            setErrorMessage(msg, "게임방이 존재하지 않습니다.");
            return null;
        }

        if(gameRooms.get(roomId).numberOfPlayer() < 4){
            setErrorMessage(msg, "게임을 시작하기 위한 인원이 부족합니다.");
            return null;
        }
        */

        Map<String, CardList> hands;

        try {
            hands = gameRooms.get(roomId).gameStart();
        } catch (Exception e) {
            setErrorMessage(msg, "모두 레디상태여야합니다.");
            return null;
        }

        List<GameMessage> result = new ArrayList<>();
        hands.forEach((k,v)->{
            result.add(
                    GameMessage.builder()
                            .type(MessageType.START)
                            .userId(k)
                            .value(gson.toJson(v.getCards()))
                            .build());
        });

        return result;
    }

    public void joinPlayer(GameMessage msg, String roomId) {
        if(!gameRooms.containsKey(roomId)){
            setErrorMessage(msg, "게임방이 존재하지 않습니다.");
            return;
        }

        if(gameRooms.get(roomId).numberOfPlayer() >= 4){
            setErrorMessage(msg, "방안에 인원이 꽉 찼습니다.");
            return;
        }

        try {
            gameRooms.get(roomId).join(new Player(msg.getValue(), msg.getUserId()));
        } catch (Exception e) {
            setErrorMessage(msg, e.getMessage());
            return;
        }

        msg.setValue("t");
    }

    public void sendCard(GameMessage msg, String roomId) {
        CardList cards = new CardList();
        cards.setCards(gson.fromJson(msg.getValue(),LinkedList.class));

        if(!gameRooms.containsKey(roomId)){
            setErrorMessage(msg, "게임방이 존재하지 않습니다.");
            return;
        }

        Player nextPlayer = null;

        try {
            nextPlayer = gameRooms.get(roomId).sendCard(msg.getUserId(),cards);
        } catch (Exception e) {
            setErrorMessage(msg, e.getMessage());
            return;
        }

        List<String> value = List.of(nextPlayer.getName(), String.valueOf(cards.getCards().size()));
        msg.setValue(gson.toJson(value));
    }

    private void setErrorMessage(GameMessage msg, String value){
            msg.setType(MessageType.ERROR);
            msg.setValue(value);
    }

    public void callDoubt(GameMessage msg, String roomId) {
        if(!gameRooms.containsKey(roomId)){
            setErrorMessage(msg, "게임방이 존재하지 않습니다.");
            return;
        }

        DoubtResult dr = gameRooms.get(roomId).callDoubt(msg.getUserId());
        msg.setValue(gson.toJson(dr));
    }

    public GameStatus getGameStatus(String id) {
        Doubt game = gameRooms.get(id);
        return game.getStatus();
    }

    public void gameReady(GameMessage msg, String roomId) {
        if(!gameRooms.containsKey(roomId)){
            setErrorMessage(msg, "게임방이 존재하지 않습니다.");
            return;
        }

        gameRooms.get(roomId).gameReady(msg.getUserId(), msg.getValue());
    }
}
