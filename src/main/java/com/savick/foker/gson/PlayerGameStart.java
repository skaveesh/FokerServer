package com.savick.foker.gson;

/**
 * Created by skaveesh on 2017-06-18.
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
