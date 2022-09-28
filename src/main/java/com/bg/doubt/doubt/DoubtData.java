package com.bg.doubt.doubt;

import com.bg.doubt.card.CardList;

import java.util.ArrayList;
import java.util.List;

public class DoubtData {
    String playerId;
    List<String> cards;

    DoubtData(){
        playerId = "";
        cards = new ArrayList<>();
    }

    public void setData(String playerId, List<CardList> cardLists){
        this.playerId = playerId;
        if(!cards.isEmpty()){
            cards.clear();
        }

        cardLists.forEach( cl -> cards.addAll(cl.getCards()) );
    }

    public List<String> getData(){

        List<String> list = List.copyOf(cards);
        playerId = null;
        cards.clear();

        return list;
    }
}
