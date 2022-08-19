package com.bg.doubt.doubt;

import com.bg.doubt.Player.Player;

import java.util.*;

public class Doubt {
    private String roomName;
    private ArrayList<Player> players;
    private LinkedList<String> field;
    private List<String> last;
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

    public Player sendCard(List<String> inputCards){
        Player player =  players.get(turn);
        if(player == null){
            return null;
        }

        player.sendCards(inputCards);
        field.addAll(last);
        last = inputCards;

        turn = (turn+1)%players.size();

        return players.get(turn);
    }

    public void callDoubt(String playerId){
        boolean result = last.stream().allMatch(e -> Objects.equals(e.split("_")[1], ordered[turn % 13]));

        field.addAll(last);
        if(!result){
            players.stream()
                    .filter(e->e.getId().equals(playerId))
                    .findFirst()
                    .ifPresent(e->e.gainCard(field));
        } else {
            players.get((turn-1)%players.size()).gainCard(field);
        }
        field.clear();
        last.clear();
    }
}
