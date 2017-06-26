package com.savick.foker.gson;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Samintha Kaveesh.
 */

public class Round {

    @SerializedName("roundNumber")
    @Expose
    private int roundNumber;

    @SerializedName("maxScore")
    @Expose
    private int maxScore;

    @SerializedName("maxScoredPlayer")
    @Expose
    private String maxScoredPlayer;

    public Round(int roundNumber, int maxScore, String maxScoredPlayer) {
        this.roundNumber = roundNumber;
        this.maxScore = maxScore;
        this.maxScoredPlayer = maxScoredPlayer;
    }

    public int getRoundNumber() {
        return roundNumber;
    }

    public void setRoundNumber(int roundNumber) {
        this.roundNumber = roundNumber;
    }

    public int getMaxScore() {
        return maxScore;
    }

    public void setMaxScore(int maxScore) {
        this.maxScore = maxScore;
    }

    public String getMaxScoredPlayer() {
        return maxScoredPlayer;
    }

    public void setMaxScoredPlayer(String maxScoredPlayer) {
        this.maxScoredPlayer = maxScoredPlayer;
    }
}
