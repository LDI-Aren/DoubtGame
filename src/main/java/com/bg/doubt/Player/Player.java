package com.bg.doubt.Player;

import com.bg.doubt.card.CardList;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;

@Data
@NoArgsConstructor
public class Player {
    private String name;
    private String id;
    private List<String> cards;
    private boolean ready;

    public Player(String name, String id) {
        this.cards = new ArrayList<>();
        this.name = name;
        this.id = id;
        ready = false;
    }

    public void doReady(boolean ready){
        this.ready = ready;
    }

    public boolean isReady(){
        return ready;
    }

    public int sendCards(CardList inputCards){
        AtomicBoolean result = new AtomicBoolean(true);

        inputCards.getCards().forEach(e->{
            if(!cards.contains(e)){
                result.set(false);
            } else {
                cards.remove(e);
            }
        });

        if(!result.get()){
            return -1;
        }

        return inputCards.getCards().size();
    }

    public void gainCard(List<CardList> inputCards){
        if(inputCards.isEmpty()){
            return;
        }

        inputCards.forEach(list -> {
            cards.addAll(list.getCards());
        });
    }
}
