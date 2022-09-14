package com.bg.doubt.Player;

import com.bg.doubt.card.CardList;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class Player {
    private String name;
    private String id;
    private CardList cards;
    private boolean ready;

    public static Player EmptyPlayer;


    public Player(String name, String id) {
        this.cards = new CardList();
        this.name = name;
        this.id = id;
        ready = false;

        EmptyPlayer = new Player();
    }

    public void doReady(boolean ready){
        this.ready = ready;
    }

    public boolean isReady(){
        return ready;
    }

    public PlayerAndCard getInfo(){
        return new PlayerAndCard(id, cards.getSize());
    }

    public List<String> getCards(){
        return cards.getCards();
    }

    public int sendCards(CardList inputCards){
        if(cards.removeCard(inputCards)){
            return -1;
        }

        return inputCards.getSize();
    }

    public int gainCard(List<CardList> inputCards){
        if(!inputCards.isEmpty()){
            cards.addCards(inputCards);
        }

        return cards.getSize();
    }
}
