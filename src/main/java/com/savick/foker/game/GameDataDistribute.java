package com.savick.foker.game;

import com.google.gson.Gson;
import com.savick.foker.gson.PlayerGameData;
import com.savick.foker.websocket.SessionHandler;

import java.util.TimerTask;

/**
 * Created by samintha on 6/23/2017.
 */
public class GameDataDistribute extends TimerTask {

    public void run() {

        if (SessionHandler.gameStarted) {

            PlayerGameData playerGameData = new PlayerGameData();

            for (int i = 1; i <= 6; i++) {
                Player player = SessionHandler.connectedPlayersList.get(i);
                if (player != null) {

                    playerGameData.GAMEDATA.add(player);

                }
            }

            System.out.println(new Gson().toJson(playerGameData));
        }
    }
}
