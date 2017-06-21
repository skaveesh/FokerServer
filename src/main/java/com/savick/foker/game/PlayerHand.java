package com.savick.foker.game;

/**
 * Created by skaveesh on 2017-06-21.
 */
public class PlayerHand {

    private int handId;
    private String suit;
    private String rank;
    private boolean isInitial;

    public PlayerHand(int handId, String suit, String rank, boolean isInitial) {
        this.handId = handId;
        this.suit = suit;
        this.rank = rank;
        this.isInitial = isInitial;
    }

    public int getHandId() {
        return handId;
    }

    public void setHandId(int handId) {
        this.handId = handId;
    }

    public String getSuit() {
        return suit;
    }

    public void setSuit(String suit) {
        this.suit = suit;
    }

    public String getRank() {
        return rank;
    }

    public void setRank(String rank) {
        this.rank = rank;
    }

    public boolean isInitial() {
        return isInitial;
    }

    public void setInitial(boolean initial) {
        isInitial = initial;
    }
}
