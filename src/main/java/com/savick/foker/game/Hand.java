package com.savick.foker.game;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author C2K
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
    
    public String showHand() {
        /* Show Cards & Their Total Points, But Only Show Total Points If ALL Cards Are Face Up*/      
        String str = "";
        boolean allFaceUp = true;
        for (Card c: cards) {
            str += c.toString() + "\n";
            if (!c.isFaceUp) {
                allFaceUp = false;
            }
        }
        // If All Cards Are Face Up, Show Total
        if (allFaceUp) {
            str += "Total points = " + getTotal() + "\n";
        }
        return str;
    }

    public ArrayList<Card> getCards(){
        return this.cards;
    }
    
    public void flipCard() {
        for (Card c: cards) {
            c.flipCard();
        }
    }
    
    public boolean give(Card card, Hand otherHand) {
        if (!cards.contains(card)) {
            return false;
        } else {
          /*  // Get Index Position Of Card
            int i = cards.indexOf(card); */
            cards.remove(card);
            otherHand.add(card);
            return true;
        }
    }
    
    // Return the total points of a hand
    public int getTotal() {
        int totalPts = 0;
        boolean hasAce = false;
        // Getting total points (any Aces by default will be worth 1)
        for (int i = 0; i < cards.size(); i++) {
            totalPts += cards.get(i).getRank();
            // Check to see if the card is an Ace
            if (cards.get(i).printRank() == "Ace" ) {
                hasAce = true;
            }
            // Make Ace worth 11 if total points are less than or equal to 11
            if (hasAce && totalPts <= 11) {
                totalPts += 10; // Add 10 more to the Ace
            }
        }
        
        return totalPts;
    }
}