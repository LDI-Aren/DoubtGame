package com.bg.doubt.card;

import com.google.gson.Gson;
import lombok.Data;
import lombok.ToString;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

@Data
@ToString
public class CardList {
    private List<String> cards;

    public CardList(){
        cards = new LinkedList<>();
    }
    public CardList(List<String> list){
        cards = new LinkedList<>();
        cards.addAll(list);
    }

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

    public String getCardsToJsonArray() {
        return (new Gson()).toJson(cards);
    }
}
