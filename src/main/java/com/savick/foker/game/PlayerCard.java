package com.savick.foker.game;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by skaveesh on 2017-06-21.
 */
public class PlayerCard {

    @SerializedName("cardId")
    @Expose
    private Integer cardId;

    @SerializedName("rank")
    @Expose
    private Integer rank;

    @SerializedName("suit")
    @Expose
    private String suit;

    @SerializedName("isInitial")
    @Expose
    private Boolean isInitial;

    public PlayerCard(int cardId, int rank, String suit, boolean isInitial) {
        this.cardId = cardId;
        this.rank = rank;
        this.suit = suit;
        this.isInitial = isInitial;
    }

    public int getCardId() {
        return cardId;
    }

    public void setCardId(int cardId) {
        this.cardId = cardId;
    }

    public String getSuit() {
        return suit;
    }

    public void setSuit(String suit) {
        this.suit = suit;
    }

    public int getRank() {
        return rank;
    }

    public void setRank(int rank) {
        this.rank = rank;
    }

    public boolean isInitial() {
        return isInitial;
    }

    public void setInitial(boolean initial) {
        isInitial = initial;
    }
}
