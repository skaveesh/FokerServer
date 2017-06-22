package com.savick.foker.websocket;

import com.google.gson.Gson;
import com.savick.foker.game.EliminatePlayer;
import com.savick.foker.game.Player;
import com.savick.foker.gson.PlayerGameStart;
import com.savick.foker.gson.PlayerIdSend;
import com.savick.foker.gson.PlayerInitialize;
import org.json.JSONObject;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.util.HashMap;
import java.util.Timer;

@Component
public class SessionHandler extends TextWebSocketHandler {

//    private static HashMap<Integer, WebSocketSession> connectedGamePlayersList = new HashMap<>();

    //allowing maximum 6 players from this hash map
    public static HashMap<Integer, Player> connectedPlayersList = new HashMap<>();

    private static boolean gameStarted = false;

    public SessionHandler() {
        connectedPlayersList.put(1, null);
        connectedPlayersList.put(2, null);
        connectedPlayersList.put(3, null);
        connectedPlayersList.put(4, null);
        connectedPlayersList.put(5, null);
        connectedPlayersList.put(6, null);

        //eliminate player every 3 seconds
//        Timer timer = new Timer();
//        timer.schedule(new EliminatePlayer(), 0, 3000);
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) {
        for (int i = 1; i <= 6; i++) {

            if (connectedPlayersList.get(i) == null || !connectedPlayersList.get(i).getPlayerSession().isOpen()) {

                //create dummy player and put it to list
                connectedPlayersList.put(i, new Player(i, "Player " + i, 0, session));

                try {
                    PlayerIdSend playerIdSend = new PlayerIdSend();
                    playerIdSend.setPlayerId(i);
                    Gson gson = new Gson();

                    session.sendMessage(new TextMessage(gson.toJson(playerIdSend)));
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
            }
        }
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {

        boolean isJsonObject;
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject = new JSONObject(message.getPayload());
            isJsonObject = true;
        } catch (Exception e) {
            isJsonObject = false;
        }

        if (!isJsonObject && "KEEPLIVE".equalsIgnoreCase(message.getPayload())) {
            return;
        } else if (!isJsonObject && "CLOSE".equalsIgnoreCase(message.getPayload())) {
            session.close();
        } else if (isJsonObject && jsonObject.has("SETPLAYERNAME") && !gameStarted) {

            Gson gson = new Gson();
            PlayerInitialize player = gson.fromJson(jsonObject.getJSONObject("SETPLAYERNAME").toString(), PlayerInitialize.class);

            connectedPlayersList.put(player.getPlayerId(), new Player(player.getPlayerId(), player.getPlayerName(), 0, session));

        } else if (isJsonObject && jsonObject.has("STARTGAME") && !gameStarted) {

            Gson gson = new Gson();
            PlayerGameStart pgs = gson.fromJson(jsonObject.getJSONObject("STARTGAME").toString(), PlayerGameStart.class);

            Player player = connectedPlayersList.get(pgs.getPlayerId());
            player.setPlayerReady(pgs.isStartGame());
            connectedPlayersList.put(pgs.getPlayerId(), player);

            for (int key : connectedPlayersList.keySet()) {
                //if player ready send message to all
                if (connectedPlayersList.get(key) != null) {
                    try {
                        sendMessageToOnGoingGameConnectedPlayers(connectedPlayersList.get(key).getPlayerId() + " number " + connectedPlayersList.get(key).getPlayerName() + " is ready= " + connectedPlayersList.get(key).isPlayerReady());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }

        } else {
            session.sendMessage(new TextMessage("error"));
        }
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        super.afterConnectionClosed(session, status);
        System.out.println(status);
    }

    private void sendMessageToOnGoingGameConnectedPlayers(String messageNotifyPlayer) throws IOException {
        try {

            for (int key : connectedPlayersList.keySet()) {

                if (connectedPlayersList.get(key) != null && connectedPlayersList.get(key).getPlayerSession().isOpen()) {

                    connectedPlayersList.get(key).getPlayerSession().sendMessage(new TextMessage(messageNotifyPlayer));

                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
//testing purpose

}
