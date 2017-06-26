package com.savick.foker.gson;

/**
 * Created by Samintha Kaveesh.
 */

public class PlayerGameStart {

    private int playerId;
    private boolean startGame;

    public int getPlayerId() {
        return playerId;
    }

    public void setPlayerId(int playerId) {
        this.playerId = playerId;
    }

    public boolean isStartGame() {
        return startGame;
    }

    public void setStartGame(boolean startGame) {
        this.startGame = startGame;
    }
}
