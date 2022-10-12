package com.bg.doubt.doubt;

import com.bg.doubt.Player.Player;
import com.bg.doubt.Player.PlayerProfile;
import com.bg.doubt.card.CardList;
import com.bg.doubt.controller.RoomElement;
import com.bg.doubt.gameMessage.GameMessage;
import com.bg.doubt.gameMessage.GameStatus;
import com.bg.doubt.gameMessage.MessageType;
import com.bg.doubt.gameMessage.RoomStatus;
import com.bg.doubt.repository.GameResultRepository;
import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;


@Service
@Slf4j
public class DoubtService {
    Map<String, Doubt> gameRooms;
    GameResultRepository gameResultRepository;
    Gson gson;

    @Autowired
    public DoubtService(GameResultRepository gameResultRepository) {
        this.gameRooms = new HashMap<>();
        this.gameResultRepository = gameResultRepository;
        gson = new Gson();
    }

    public Optional<Doubt> findRoomByRoomId(String roomId){
        return Optional.ofNullable(gameRooms.get(roomId));
    }

    public String createGameroom(String gameName, String name) {
        String id = String.format("%s-%s",gameName, UUID.randomUUID());

        Doubt doubt = new Doubt(name, id);
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
        if(!gameRooms.containsKey(roomId)){
            throw new Exception("게임방이 존재하지 않습니다.");
        }

        if(gameRooms.get(roomId).numberOfPlayer() < 4){
            throw new Exception("게임방을 시작하기 위한 인원이 부족합니다.");
        }

        Doubt gameRoom = gameRooms.get(roomId);

        gameRoom.gameStart();

        return gameRoom.getRoomStatusByPlayerId(msg.getPlayerId());
    }

    public List<PlayerProfile> joinPlayer(GameMessage msg, String roomId) throws Exception {
        if(!gameRooms.containsKey(roomId)){
            throw new Exception("게임방이 존재하지 않습니다.");
        }

        if(gameRooms.get(roomId).isExistPlayer(msg.getPlayerId())){
            throw new Exception("이미 존재하는 플레이어 입니다.");
        }

        if(gameRooms.get(roomId).numberOfPlayer() >= 4){
            throw new Exception("방에 인원이 가득 찼습니다.");
        }

        List<PlayerProfile> profiles = gameRooms.get(roomId).getPlayersProfile();
        gameRooms.get(roomId).join(new Player(msg.getPlayerId()));

        return profiles;
    }

    public SendCardData sendCard(GameMessage msg, String roomId) throws Exception {
        CardList cards = new CardList();
        cards.setCards(gson.fromJson(msg.getValue(),LinkedList.class));

        if(!gameRooms.containsKey(roomId)){
            throw new Exception("게임방이 존재하지 않습니다.");
        }

        SendCardData rs = gameRooms.get(roomId).sendCard(msg.getPlayerId(),cards);

        return rs;
    }

    public DoubtResult callDoubt(GameMessage msg, String roomId) throws Exception {
        if(!gameRooms.containsKey(roomId)){
            throw new Exception("게임방이 존재하지 않습니다.");
        }

        DoubtResult dr = gameRooms.get(roomId).callDoubt(msg.getPlayerId(), msg.getValue());

        return dr;
    }

    public GameStatus getGameStatus(String id) {
        Doubt game = gameRooms.get(id);
        return game.getStatus();
    }

    public boolean gameReady(GameMessage msg, String roomId) throws Exception {
        if(!gameRooms.containsKey(roomId)){
            throw new Exception("게임방이 존재하지 않습니다.");
        }

        boolean isReady = gameRooms.get(roomId).gameReady(msg.getPlayerId(), msg.getValue());

        return isReady;
    }

    public boolean isDuplicate(String roomId, String playerId) {
        if(!gameRooms.containsKey(roomId)){
            return true;
        }

        return gameRooms.get(roomId).isDuplicate(playerId);
    }

    public List<String> getDestinationPlayerId(String roomId, String playerId) {

        return gameRooms.get(roomId).getPlayerIdWithout(playerId);
    }

    public List<String> getPlayerIdByGameMessage(String roomId, MessageType type) {
        if(type.equals(MessageType.ERROR)){
            return new ArrayList<>();
        }

        return gameRooms.get(roomId).getPlayerIdAll();
    }

    public List<String> getGainCards(String roomId, String playerId) throws Exception {
        if(!gameRooms.containsKey(roomId)){
            throw new Exception("게임방이 존재하지 않습니다.");
        }

        return gameRooms.get(roomId).getGainCards(playerId);
    }

    public GameLog getFinishGameData(String roomId, String playerId) throws Exception {
        if(!gameRooms.containsKey(roomId)){
            throw new Exception("게임방이 존재하지 않습니다.");
        }

        if(!gameRooms.get(roomId).isExistPlayer(playerId)){
            throw new Exception("게임방의 플레이어가 아닙니다.");
        }

        gameRooms.get(roomId).gameFinish();

        GameLog gameFinishData = gameRooms.get(roomId).getFinishData();
        gameResultRepository.save(gameFinishData.toGameResult());

        return gameFinishData;
    }
}
