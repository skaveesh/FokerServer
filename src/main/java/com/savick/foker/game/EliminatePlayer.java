package com.savick.foker.game;

import com.savick.foker.websocket.SessionHandler;
import org.springframework.web.socket.WebSocketSession;

import java.util.TimerTask;

/**
 * Created by skaveesh on 2017-06-18.
 */
public class EliminatePlayer extends TimerTask{

    public void run() {

        for (int key : SessionHandler.connectedPlayersList.keySet()) {

            if (SessionHandler.connectedPlayersList.get(key) == null || !SessionHandler.connectedPlayersList.get(key).getPlayerSession().isOpen())
                SessionHandler.connectedPlayersList.put(key,null);
        }

    }
}
