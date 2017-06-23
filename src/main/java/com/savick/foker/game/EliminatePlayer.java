package com.savick.foker.game;

import com.savick.foker.websocket.SessionHandler;
import org.json.JSONObject;
import org.springframework.web.socket.WebSocketSession;

import java.util.TimerTask;

/**
 * Created by skaveesh on 2017-06-18.
 */
public class EliminatePlayer extends TimerTask {

    public void run() {

        int playersCount = 0;

        for (int key : SessionHandler.connectedPlayersList.keySet()) {

            if (SessionHandler.connectedPlayersList.get(key) == null || !SessionHandler.connectedPlayersList.get(key).getPlayerSession().isOpen()) {
                SessionHandler.connectedPlayersList.put(key, null);
            } else {
                playersCount++;
                SessionHandler.connectedPlayersCount = playersCount;
            }
        }

        if(playersCount < 2 && SessionHandler.gameStarted) {
            SessionHandler.gameStarted = false;
            SessionHandler.timerEliminatePlayers.cancel();
            SessionHandler.timerChangeCards.cancel();

            //TODO: send message to all players - game is end
            System.out.println(new JSONObject().put("GAMEEND", new JSONObject().put("message", "Only one player is connected")));
        }
    }
}
