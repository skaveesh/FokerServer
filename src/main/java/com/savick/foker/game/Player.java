package com.savick.foker.game;

import org.springframework.web.socket.WebSocketSession;

/**
 * Created by skaveesh on 2017-06-18.
 */
public class Player {

    private int playerId;
    private String playerName;
    private int score;
    private WebSocketSession playerSession;
    private boolean playerReady;

    public Player(int playerId, String playerName, int score, WebSocketSession playerSession) {
        this.playerId = playerId;
        this.playerName = playerName;
        this.score = score;
        this.playerSession = playerSession;
        this.playerReady = false;
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

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public WebSocketSession getPlayerSession() {
        return playerSession;
    }

    public void setPlayerSession(WebSocketSession playerSession) {
        this.playerSession = playerSession;
    }

    public boolean isPlayerReady() {
        return playerReady;
    }

    public void setPlayerReady(boolean playerReady) {
        this.playerReady = playerReady;
    }
}
