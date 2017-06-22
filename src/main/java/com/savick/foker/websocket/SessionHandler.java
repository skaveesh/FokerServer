package com.savick.foker.websocket;

import com.google.gson.Gson;
import com.savick.foker.game.*;
import com.savick.foker.gson.*;
import org.json.JSONObject;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.util.*;

@Component
public class SessionHandler extends TextWebSocketHandler {

//    private static HashMap<Integer, WebSocketSession> connectedGamePlayersList = new HashMap<>();

    //allowing maximum 6 players from this hash map
    public static HashMap<Integer, Player> connectedPlayersList = new HashMap<>();

    private static boolean gameStarted = false;

    private ArrayList<Card> gameCardList = new ArrayList<>();

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

        Deck d1 = new Deck();
        d1.populate();
        d1.shuffle();


        gameCardList.addAll(d1.getCards());
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) {
        for (int i = 1; i <= 6; i++) {

            if (connectedPlayersList.get(i) == null || !connectedPlayersList.get(i).getPlayerSession().isOpen()) {

                //create dummy player and put it to list
                connectedPlayersList.put(i, new Player(i, "Player " + i,false, 0, 0,session, null));

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
    protected void handleTextMessage(WebSocketSession session, TextMessage message) {

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
            try {
                session.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else if (isJsonObject && jsonObject.has("SETPLAYERNAME") && !gameStarted) {

            Gson gson = new Gson();
            PlayerInitialize player = gson.fromJson(jsonObject.getJSONObject("SETPLAYERNAME").toString(), PlayerInitialize.class);

            connectedPlayersList.put(player.getPlayerId(), new Player(player.getPlayerId(), player.getPlayerName(),false, 0,0, session, null));

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

        } else if (!isJsonObject && "start".equalsIgnoreCase(message.getPayload())) {

//            for (Card c : gameCardList)
//                System.out.println(c.getRank() + " " + c.getSuit());

            //game started
            gameStarted = true;

            //---------ROUND 1----------
            for (int i = 1; i <= 6; i++) {
                if (connectedPlayersList.get(i) != null && !gameCardList.isEmpty()) {

                    List<PlayerHand> tempPlayerHand = new ArrayList<>();

                    //distribute random first two initial cards to every player
                    for (int j = 1; j <= 2; j++) {
                        Random random = new Random();

                        int randomCardNumber = random.nextInt(gameCardList.size());
                        PlayerHand playerHand = new PlayerHand(j, gameCardList.get(randomCardNumber).getRank(), gameCardList.get(randomCardNumber).getSuit(), true);
                        tempPlayerHand.add(playerHand);
                        gameCardList.remove(randomCardNumber);
                    }

                    Player tempPlayer = connectedPlayersList.get(i);
                    tempPlayer.setPlayerHand(tempPlayerHand);
                    connectedPlayersList.put(i, tempPlayer);
                }
            }

            System.out.println("______________________");
            for (Card c : gameCardList)
                System.out.println(c.getRank() + " " + c.getSuit());
            System.out.println("______________________");

            //---------ROUND 2----------
            // distribute top card of the list to every one, one by one
            for (int j = 1; j <= 3; j++) {
                for (int i = 1; i <= 6; i++) {
                    if (connectedPlayersList.get(i) != null && !gameCardList.isEmpty()) {

                        List<PlayerHand> tempPlayerHand = new ArrayList<>();
                        tempPlayerHand.addAll(connectedPlayersList.get(i).getPlayerHand());

                        //distribute first card on top of the list
                        PlayerHand playerHand = new PlayerHand(j + 2, gameCardList.get(0).getRank(), gameCardList.get(0).getSuit(), false);
                        tempPlayerHand.add(playerHand);
                        gameCardList.remove(0);

                        Player tempPlayer = connectedPlayersList.get(i);
                        tempPlayer.setPlayerHand(tempPlayerHand);
                        connectedPlayersList.put(i, tempPlayer);
                    }
                }
            }

            System.out.println("______________________");
            for (Card c : gameCardList)
                System.out.println(c.getRank() + " " + c.getSuit());
            System.out.println("______________________");

            Gson gson = new Gson();
            PlayerGameData playerGameData = new PlayerGameData();

            for (int i = 1; i <= 6; i++) {
                if (connectedPlayersList.get(i) != null) {

                    playerGameData.GAMEDATA.add(connectedPlayersList.get(i));

                }
            }

            System.out.println(gson.toJson(playerGameData));


        } else if (isJsonObject && jsonObject.has("CHANGECARD") && gameStarted) {

            Gson gson = new Gson();
            PlayerChangeCard pcc = gson.fromJson(jsonObject.getJSONObject("CHANGECARD").toString(), PlayerChangeCard.class);

            Player tempPlayer = connectedPlayersList.get(pcc.getPlayerId());

            //player has to match the session with initial session which he used to start the game
            //this way make sures that the same player is requesting from the server
            if(tempPlayer.getPlayerSession() == session) {

                //check if player has already changed 3 cards. if not....
                if(tempPlayer.getNumberOfChangedCards() < 3){

                    ArrayList<PlayerHand> tempPlayerHand = new ArrayList<>();
                    tempPlayerHand.addAll(tempPlayer.getPlayerHand());

                    //iterate through player's hand
                    for(int i=0; i<tempPlayerHand.size(); i++){
                        PlayerHand ph = tempPlayerHand.get(i);

                        //if player hand id == player sent card id from the game,
                        //changes that card with the card which in on the top of the card list
                        if(ph.getHandId() == pcc.getHandId()){
                            //TODO:old card and new card send to scoring algorithm
                            PlayerHand oldCard = new PlayerHand(ph.getHandId(),ph.getRank(),ph.getSuit(),ph.isInitial());

                            PlayerHand newCard = new PlayerHand(ph.getHandId(),gameCardList.get(0).getRank(),gameCardList.get(0).getSuit(),false);
                            tempPlayerHand.set(i,newCard);
                        }
                    }

                    //replace that player with new cards
                    tempPlayer.setPlayerHand(tempPlayerHand);
                    connectedPlayersList.put(tempPlayer.getPlayerId(), tempPlayer);
                }

            }

            PlayerGameData playerGameData = new PlayerGameData();

            for (int i = 1; i <= 6; i++) {
                if (connectedPlayersList.get(i) != null) {

                    playerGameData.GAMEDATA.add(connectedPlayersList.get(i));

                }
            }

            System.out.println(new Gson().toJson(playerGameData));
        } else {
            try {
                session.sendMessage(new TextMessage("error"));
            } catch (IOException e) {
                e.printStackTrace();
            }
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

}
