package com.savick.foker.websocket;

import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.util.HashMap;

@Component
public class SessionHandler extends TextWebSocketHandler {

    //allowing maximum 5 players from this hash map
    private static HashMap<Integer, WebSocketSession> gamePlayersHashList = new HashMap<>();

    public SessionHandler() {
        gamePlayersHashList.put(1, null);
        gamePlayersHashList.put(2, null);
        gamePlayersHashList.put(3, null);
        gamePlayersHashList.put(4, null);
        gamePlayersHashList.put(5, null);
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) {
        for (int i = 1; i < 6; i++) {
            WebSocketSession tempSession = gamePlayersHashList.get(i);

            if (tempSession == session)
                break;
            else if (tempSession == null || !tempSession.isOpen()) {
                gamePlayersHashList.put(i, session);
                break;
            }
        }
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {

        if ("KEEPLIVE".equalsIgnoreCase(message.getPayload())) {
            return;
        } else if ("CLOSE".equalsIgnoreCase(message.getPayload())) {
            session.close();
        } else if (session != null && session.isOpen()) {

            try {
                sendMessageToAllPlayers(message.getPayload());
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }

    private void sendMessageToAllPlayers(String messageNotifyPlayer) throws IOException {
        try {

            for (int i = 1; i < 6; i++) {
                WebSocketSession tempSession = gamePlayersHashList.get(i);

                if (tempSession != null && tempSession.isOpen()) {
                    tempSession.sendMessage(new TextMessage(messageNotifyPlayer));
                } else if (tempSession != null)
                    tempSession.close();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
