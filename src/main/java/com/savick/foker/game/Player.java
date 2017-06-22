package com.savick.foker.game;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import org.springframework.web.socket.WebSocketSession;

import java.util.List;

/**
 * Created by skaveesh on 2017-06-18.
 */
public class Player {

    @SerializedName("playerId")
    @Expose
    private Integer playerId;

    @SerializedName("playerName")
    @Expose
    private String playerName;

    @SerializedName("playerReady")
    @Expose
    private Boolean playerReady;

    @SerializedName("score")
    @Expose
    private Integer score;

    @SerializedName("numberOfChangedCards")
    @Expose
    private Integer numberOfChangedCards;

    private transient WebSocketSession playerSession;

    @SerializedName("playerHand")
    @Expose
    private List<PlayerCard> playerHand;

    public Player(Integer playerId, String playerName, Boolean playerReady, Integer score, Integer numberOfChangedCards, WebSocketSession playerSession, List<PlayerCard> playerCard) {
        this.playerId = playerId;
        this.playerName = playerName;
        this.playerReady = playerReady;
        this.score = score;
        this.numberOfChangedCards = numberOfChangedCards;
        this.playerSession = playerSession;
        this.playerHand = playerCard;
    }

    public int getPlayerId() {
        return playerId;
    }

    public void setPlayerId(int playerId) {
        this.playerId = playerId;
    }

    public String getPlayerName() {
        return playerName;
    }

    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }

    public boolean isPlayerReady() {
        return playerReady;
    }

    public void setPlayerReady(boolean playerReady) {
        this.playerReady = playerReady;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public Integer getNumberOfChangedCards() {
        return numberOfChangedCards;
    }

    public void setNumberOfChangedCards(Integer numberOfChangedCards) {
        this.numberOfChangedCards = numberOfChangedCards;
    }

    public WebSocketSession getPlayerSession() {
        return playerSession;
    }

    public void setPlayerSession(WebSocketSession playerSession) {
        this.playerSession = playerSession;
    }

    public List<PlayerCard> getPlayerHand() {
        return playerHand;
    }

    public void setPlayerHand(List<PlayerCard> playerCard) {
        this.playerHand = playerCard;
    }
}
