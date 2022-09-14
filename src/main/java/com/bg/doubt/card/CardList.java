package com.bg.doubt.card;

import lombok.Data;

import java.util.List;

@Data
public class CardList {
    private List<String> cards;

    public int getSize(){
        return cards.size();
    }

    public boolean removeCard(CardList cl){
        return cards.removeAll(cl.getCards());
    }

    public void addCards(List<CardList> inputCards) {
        inputCards.forEach(e -> cards.addAll(e.getCards()));
    }
}
