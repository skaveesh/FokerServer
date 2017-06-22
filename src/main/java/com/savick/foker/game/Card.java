package com.savick.foker.game;

/**
 *
 * @author C2K
 */
public class Card {
    // Private Fields
    private Suit suit;
    private Rank rank;
    public boolean isFaceUp;
    
    // Constructor Method
    public Card(Rank rank, Suit suit) {
        this.rank = rank;
        this.suit = suit;
        isFaceUp = false;
    }
    
    // Public Methods
    public String getSuit() {
        return suit.printSuit();
    }
    
    public int getRank() {
        return rank.getRank();
    }
    
    public String printRank() {
        // Get Rank As A String ( Primarily For Aces)
        return rank.printRank();
    }
    
    public void flipCard() {
        isFaceUp = !isFaceUp;
    }
    
    public String toString() {
        String str = "";
        if (isFaceUp) {
            str += rank.printRank() + " of " +
            suit.printSuit();
        } else {
            str = "Face Down (nothing to see here)";
        }
        
        return str;
    }
}