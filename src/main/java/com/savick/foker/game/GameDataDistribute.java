package com.savick.foker.game;

import com.google.gson.Gson;
import com.savick.foker.gson.PlayerGameData;
import com.savick.foker.websocket.SessionHandler;
import org.springframework.web.socket.TextMessage;

import java.io.IOException;
import java.util.TimerTask;

/**
 * Created by samintha on 6/23/2017.
 */
public class GameDataDistribute extends TimerTask {

    public void run() {

        if (SessionHandler.gameStarted) {

            PlayerGameData playerGameData = new PlayerGameData();

            SessionHandler.iterateOverEveryPlayer(player -> {
                playerGameData.GAMEDATA.add(player);
            });

            //send message to all players - game is end
            SessionHandler.iterateOverEveryPlayer(player -> {
                if (player.getPlayerSession().isOpen()) {
                    try {
                        player.getPlayerSession().sendMessage(new TextMessage(new Gson().toJson(playerGameData)));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });

        }
    }
}
