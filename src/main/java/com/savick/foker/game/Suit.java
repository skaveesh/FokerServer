package com.savick.foker.game;

/**
 *
 * @author C2K
 */
public enum Suit {
    HEARTS("Hearts"),
    SPADES("Spades"),
    DIAMONDS("Diamonds"),
    CLUBS("Clubs");
    
    private final String suitText;
    
    // Constructor
    private Suit(String suitText) {
        this.suitText = suitText;
    }
    
    // Public Method
    public String printSuit() {
        return suitText;
    }
}