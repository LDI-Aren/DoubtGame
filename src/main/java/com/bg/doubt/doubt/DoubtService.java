package com.bg.doubt.doubt;

import com.bg.doubt.Player.Player;
import com.bg.doubt.card.CardList;
import com.bg.doubt.controller.RoomElement;
import com.bg.doubt.gameMessage.GameMessage;
import com.bg.doubt.gameMessage.GameStatus;
import com.bg.doubt.gameMessage.MessageType;
import com.bg.doubt.gameMessage.RoomStatus;
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

    public RoomStatus startGame(GameMessage msg, String roomId) throws Exception {

        /*
        if(!gameRooms.containsKey(roomId)){
            throw new Exception("게임방이 존재하지 않습니다.");
        }

        if(gameRooms.get(roomId).numberOfPlayer() < 4){
            throw new Exception("게임방을 시작하기 위한 인원이 부족합니다.");
        }
        */

        Doubt gameRoom = gameRooms.get(roomId);
        gameRoom.gameStart();

        return gameRoom.getRoomStatusByPlayerId(msg.getPlayerId());
    }

    public RoomStatus joinPlayer(GameMessage msg, String roomId) throws Exception {
        if(!gameRooms.containsKey(roomId)){
            throw new Exception("게임방이 존재하지 않습니다.");
        }

        if(gameRooms.get(roomId).numberOfPlayer() >= 4){
            throw new Exception("방에 인원이 가득 찼습니다.");
        }

        RoomStatus rs = gameRooms.get(roomId).join(new Player(msg.getValue(), msg.getPlayerId()));

        return rs;
    }

    public RoomStatus sendCard(GameMessage msg, String roomId) throws Exception {
        CardList cards = new CardList();
        cards.setCards(gson.fromJson(msg.getValue(),LinkedList.class));

        if(!gameRooms.containsKey(roomId)){
            throw new Exception("게임방이 존재하지 않습니다.");
        }

        Player nextPlayer = null;

        RoomStatus rs = gameRooms.get(roomId).sendCard(msg.getPlayerId(),cards);

        return rs;
    }

    public DoubtResult callDoubt(GameMessage msg, String roomId) throws Exception {
        if(!gameRooms.containsKey(roomId)){
            throw new Exception("게임방이 존재하지 않습니다.");
        }

        DoubtResult dr = gameRooms.get(roomId).callDoubt(msg.getPlayerId());
        return dr;
    }

    public GameStatus getGameStatus(String id) {
        Doubt game = gameRooms.get(id);
        return game.getStatus();
    }

    public String gameReady(GameMessage msg, String roomId) throws Exception {
        if(!gameRooms.containsKey(roomId)){
            throw new Exception("게임방이 존재하지 않습니다.");
        }

        boolean isReady = gameRooms.get(roomId).gameReady(msg.getPlayerId(), msg.getValue());

        return String.valueOf(isReady);
    }
}
