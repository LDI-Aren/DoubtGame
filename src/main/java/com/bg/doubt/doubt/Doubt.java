package com.bg.doubt.doubt;

import com.bg.doubt.Player.Player;
import com.bg.doubt.card.CardList;
import com.bg.doubt.card.CardSetter;

import java.util.*;

public class Doubt {
    private String roomName;
    private final Map<String, Player> players;
    private final LinkedList<CardList> field;
    private final ArrayList<String> playerList;
    private int turn;

    private static final String[] ordered = {"A", "2", "3", "4", "5", "6", "7", "8", "9", "10", "J", "Q", "K"};

    public Doubt() {
        this.players = new HashMap<>();
        this.field = new LinkedList<>();
        this.playerList = new ArrayList<>();
        turn = 0;
    }

    public String getRoomName() {
        return roomName;
    }

    public void setRoomName(String roomName) {
        this.roomName = roomName;
    }

    public int numberOfPlayer(){
        return players.size();
    }

    public void join(Player player) throws Exception {
        if(players.size() >= 4){
            throw new Exception("Player already full");
        }

        playerList.add(player.getId());
        players.put(player.getId(), player);
    }

    public Player sendCard(String playerId ,CardList inputCards) throws Exception {
        Player player =  players.get(playerId);
        if(player == null){
            throw new Exception("Player Not Found");
        }

        if(!player.getId().equals(playerId)){
            throw new Exception("Player MisMatch");
        }

        int result = player.sendCards(inputCards);
        field.add(inputCards);

        if(result == 0){
            //해당 turn player의 승리
        }

        turn++;

        return players.get(playerId);
    }

    public DoubtResult callDoubt(String playerId){
        DoubtResult dr = new DoubtResult();

        List<String> last = field.peekLast().getCards();

        boolean result = last.stream().allMatch(e -> Objects.equals(e.split("_")[1], ordered[(turn-1) % 13]));

        dr.setLastCards(List.copyOf(last));

        Player player;
        if(result){
            player = players.get(playerList.get((turn-1)%4));
        } else {
            player = players.get(playerId);
        }

        dr.setResult(result);
        player.gainCard(field);
        dr.setPlayerId(player.getId());
        field.clear();

        return dr;
    }

    public GameStatus getStatus() {
        return GameStatus.builder()
                .field(this.field)
                .players(this.players)
                .build();
    }

    public Map<String, CardList> gameStart() throws Exception {
        boolean ready = players.entrySet().stream().allMatch(e -> e.getValue().isReady());

        if(!ready){
            throw new Exception("Not All Ready");
        }

        Map<String, CardList> hands = new HashMap<>();

        players.forEach((k,v) -> {
            CardList cl = new CardList();
            cl.setCards(CardSetter.getCards(13));

            v.gainCard(List.of(cl));

            hands.put(k,cl);
        });

        return hands;
    }

    public void gameReady(String userId, String value) {
        players.get(userId).doReady(Boolean.parseBoolean(value));
    }
}
