package com.savick.foker.game;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Chulan Kotudurage.
 */

public class Hand {
    public ArrayList<Card> cards;

    // Constructor
    public Hand() {
        cards = new ArrayList<Card>();
    }

    public void clear() {
        cards.clear();
    }

    public void add(Card card) {
        cards.add(card);
    }


    public ArrayList<Card> getCards() {
        return this.cards;
    }
}