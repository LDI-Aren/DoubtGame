package com.bg.doubt.Player;

import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;

public class Player {
    private String name;
    private String id;
    private Set<String> cards;

    public Player(String name, String id) {
        this.name = name;
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int sendCards(List<String> inputCards){
        AtomicBoolean result = new AtomicBoolean(true);

        inputCards.forEach(e->{
            if(!cards.contains(e)){
                result.set(false);
            } else {
                cards.remove(e);
            }
        });

        if(!result.get()){
            return -1;
        }

        return inputCards.size();
    }

    public void gainCard(List<String> inputCards){
        if(inputCards.isEmpty()){
            return;
        }
        cards.addAll(inputCards);
    }
}
