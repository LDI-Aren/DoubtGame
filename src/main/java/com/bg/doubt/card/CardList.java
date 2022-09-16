package com.bg.doubt.card;

import lombok.Data;

import java.util.List;

@Data
public class CardList {
    private List<String> cards;

    public int getSize(){
        if(cards == null)
            return 0;

        return cards.size();
    }

    public boolean removeCard(CardList cl){
        return cards.removeAll(cl.getCards());
    }

    public void addCards(List<CardList> inputCards) {
        inputCards.forEach(e -> cards.addAll(e.getCards()));
    }
}
