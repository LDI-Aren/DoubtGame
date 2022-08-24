package com.bg.doubt.doubt;

import com.bg.doubt.Player.Player;
import com.bg.doubt.card.CardList;

import java.util.*;

public class Doubt {
    private String roomName;
    private ArrayList<Player> players;
    private LinkedList<CardList> field;
    private int turn;

    private static String[] ordered = {"A", "2", "3", "4", "5", "6", "7", "8", "9", "10", "J", "Q", "K"};

    public Doubt() {
        this.players = new ArrayList();
        this.field = new LinkedList<>();
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

    public void join(Player player){
        if(players.size() >= 4){
            return;
        }

        players.add(player);
    }

    public Player sendCard(CardList inputCards){
        Player player =  players.get(turn%players.size());
        if(player == null){
            return null;
        }

        int result = player.sendCards(inputCards);
        field.add(inputCards);

        if(result == 0){
            //해당 turn player의 승리
        }

        turn++;

        return players.get(turn%players.size());
    }

    public DoubtResult callDoubt(String playerId){
        DoubtResult dr = new DoubtResult();

        List<String> last = field.peekLast().getCards();

        boolean result = last.stream().allMatch(e -> Objects.equals(e.split("_")[1], ordered[(turn-1) % 13]));

        dr.setLastCards(List.copyOf(last));

        if(!result){
            dr.setResult(false);
            dr.setPlayerId(playerId);
            players.stream()
                    .filter(e->e.getId().equals(playerId))
                    .findFirst()
                    .ifPresent(e->{
                        e.gainCard(field);
                    });
        } else {
            dr.setResult(true);

            Player player = players.get((turn-1)%players.size());
            player.gainCard(field);

            dr.setPlayerId(player.getId());
        }
        field.clear();

        return dr;
    }

    public GameStatus getStatus() {
        return GameStatus.builder()
                .field(this.field)
                .players(this.players)
                .build();
    }

    public void gameStart(String userId, ArrayList<String> cards) {
        CardList cl = new CardList();
        cl.setCards(cards);
        cards.forEach(System.out::println);
        players.get(0).gainCard(List.of(cl));
    }
}
