package com.savick.foker.websocket;


import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.util.ArrayList;

@Component
public class SessionHandler extends TextWebSocketHandler {

    private static ArrayList<WebSocketSession> gamePlayersSessionList = new ArrayList<>();

    private WebSocketSession session;

    @Override
    public void afterConnectionEstablished(WebSocketSession session) {
        this.session = session;
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {

        if ("KEEPLIVE".equalsIgnoreCase(message.getPayload())) {
            return;
        } else if ("CLOSE".equalsIgnoreCase(message.getPayload())) {
            session.close();
        } else if (session != null && session.isOpen()) {

            try {
                gamePlayersSessionList.add(session);

                sendMessageToAllPlayers(message.getPayload());

            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }

    private void sendMessageToAllPlayers(String messageNotifyPlayer) throws IOException {
        try {
            for (WebSocketSession ws : gamePlayersSessionList) {
                if (ws.isOpen()) {
                    ws.sendMessage(new TextMessage(messageNotifyPlayer));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
