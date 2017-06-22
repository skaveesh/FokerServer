package com.savick.foker.game;

import java.util.Random;

/**
 *
 * @author C2K
 */
public class Deck extends Hand{
    Random rand = new Random();
    public void populate() {
        for (Suit suit: Suit.values()) {
            for (Rank rank: Rank.values()) {
                Card card = new Card(rank, suit);
                card.flipCard();
                this.add(card);
            }
        }
    }
    
    public void shuffle() {
        for (int i = cards.size() - 1; i > 0; i--) {
            // Swap A Random Card Between The Beginning & The Last Card Of The Loop
            int pick = rand.nextInt(i);
            Card randCard = cards.get(pick);
            Card lastCard = cards.get(i);
            cards.set(i, randCard);
            cards.set(pick, lastCard);
    }
    }
    
    /* deal() method */
    public void deal(Hand[] hands, int perHand) {
        for (int i = 0; i < perHand; i++) {
            for (int j=0; j < hands.length; j++) {
                this.give(cards.get(0), hands[j]);
            }
        }
    }
    
    public void deal(Hand hand, int perHand) {
        // Deal to a single hand
        for (int i = 0; i < perHand; i++) {
            this.give(cards.get(0), hand);
        }
    }
    
    public void flipCard(Card c) {
        c.flipCard();
    }
    
}
