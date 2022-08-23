package com.bg.doubt.Player;

import com.bg.doubt.card.CardList;
import lombok.Data;

import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;

@Data
public class Player {
    private String name;
    private String id;
    private List<String> cards;

    public Player(String name, String id) {
        this.name = name;
        this.id = id;
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
