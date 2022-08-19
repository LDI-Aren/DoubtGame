package com.bg.doubt.card;

import java.util.ArrayList;

public class CardSetter {
    private static String[] CARDS = {
            "clover_A",
            "clover_2",
            "clover_3",
            "clover_4",
            "clover_5",
            "clover_6",
            "clover_7",
            "clover_8",
            "clover_9",
            "clover_10",
            "clover_J",
            "clover_Q",
            "clover_K",
            "spade_A",
            "spade_2",
            "spade_3",
            "spade_4",
            "spade_5",
            "spade_6",
            "spade_7",
            "spade_8",
            "spade_9",
            "spade_10",
            "spade_J",
            "spade_Q",
            "spade_K",
            "hart_A",
            "hart_2",
            "hart_3",
            "hart_4",
            "hart_5",
            "hart_6",
            "hart_7",
            "hart_8",
            "hart_9",
            "hart_10",
            "hart_J",
            "hart_Q",
            "hart_K",
            "diamond_A",
            "diamond_2",
            "diamond_3",
            "diamond_4",
            "diamond_5",
            "diamond_6",
            "diamond_7",
            "diamond_8",
            "diamond_9",
            "diamond_10",
            "diamond_J",
            "diamond_Q",
            "diamond_K"
    };

    public static ArrayList<String> getCards(int num){
        ArrayList<String> cards = new ArrayList<>();

        for (int i = 0; i < num; i++) {
            cards.add(CARDS[(i*5)%52]);
        }

        return cards;
    }
}
