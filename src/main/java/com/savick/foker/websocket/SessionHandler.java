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
import java.util.function.Consumer;

@Component
public class SessionHandler extends TextWebSocketHandler {

//    private static HashMap<Integer, WebSocketSession> connectedGamePlayersList = new HashMap<>();

    //allowing maximum 6 players from this hash map
    public static HashMap<Integer, Player> connectedPlayersList = new HashMap<>();

    public static boolean gameStarted = false;

    public static int connectedPlayersCount = 0;

    private static ArrayList<Card> gameCardList = new ArrayList<>();

    public static Timer timerEliminatePlayers;
    public static Timer timerChangeCards;
    private static Timer timerGameDataSender;

    public static boolean canPlayerChangeCards = false;

    private static String maxScoredPlayerName = null;
    private static int maxScore = 0;
    private static int roundNumber;

    public SessionHandler() {
        connectedPlayersList.put(1, null);
        connectedPlayersList.put(2, null);
        connectedPlayersList.put(3, null);
        connectedPlayersList.put(4, null);
        connectedPlayersList.put(5, null);
        connectedPlayersList.put(6, null);

        //send game data to every player every 5 seconds
        timerGameDataSender = new Timer();
        timerGameDataSender.schedule(new GameDataDistribute(), 0, 5000);
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) {
        for (int i = 1; i <= 6; i++) {

            if (connectedPlayersList.get(i) == null || !connectedPlayersList.get(i).getPlayerSession().isOpen()) {

                //create dummy player and put it to list
                connectedPlayersList.put(i, new Player(i, "Player " + i, false, 0, 0, session, null));

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
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws IOException {

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

            //check the session to make sure same player sending the request
            if (connectedPlayersList.get(player.getPlayerId()).getPlayerSession() == session) {
                connectedPlayersList.put(player.getPlayerId(), new Player(player.getPlayerId(), player.getPlayerName(), false, 0, 0, session, null));

                //send player the name change
                session.sendMessage(new TextMessage(new JSONObject().put("PLAYERNAME", new JSONObject().put("playerName", connectedPlayersList.get(player.getPlayerId()).getPlayerName())).toString()));
            }

        } else if (isJsonObject && jsonObject.has("STARTGAME") && !gameStarted) {

            //check if 2 or more players connected. EliminatePlayer class responsible
            //for counting players each 3 seconds and eliminate connection lost players.
            int playersCount = 0;

            for (int key : SessionHandler.connectedPlayersList.keySet()) {

                if (SessionHandler.connectedPlayersList.get(key) == null || !SessionHandler.connectedPlayersList.get(key).getPlayerSession().isOpen()) {
                    SessionHandler.connectedPlayersList.put(key, null);
                } else {
                    playersCount++;
                    SessionHandler.connectedPlayersCount = playersCount;
                }
            }

            if (connectedPlayersCount < 2) {

                //sending to all. only one player is connected. so it will forward the message to that player
                sendMessageConnectedAllPlayers(new JSONObject().put("PLAYERSCONNECTED", new JSONObject().put("message", "Only one player is connected")).toString());
                return;
            }

            Gson gson = new Gson();
            PlayerGameStart pgs = gson.fromJson(jsonObject.getJSONObject("STARTGAME").toString(), PlayerGameStart.class);

            Player player = connectedPlayersList.get(pgs.getPlayerId());
            player.setPlayerReady(pgs.isStartGame());
            connectedPlayersList.put(pgs.getPlayerId(), player);


            for (int i = 1; i <= 6; i++) {
                //if player ready send message to all

                if (connectedPlayersList.get(i) != null && !connectedPlayersList.get(i).isPlayerReady()) {
                    //check if all the players are ready and if ready start the game
                    PlayerReady playerReady = new PlayerReady();

                    iterateOverEveryPlayer(player1 -> {
                        playerReady.PLAYERREADY.add(player1);
                    });

                    //sending to all
                    sendMessageConnectedAllPlayers(new Gson().toJson(playerReady));

                    break;
                } else if (i == 6) {

                    //game has 5 rounds
                    for (roundNumber = 1; roundNumber <= 5; roundNumber++) {
                        //start game
                        initializeDeckOfCards();

                        startGameAndDistributeCards();
                        startTimerEliminatePlayers();

                        /**
                         * allocate 30 seconds to allow players to change any 3 cards
                         * notify all players that they can now change their cards
                         */
                        canPlayerChangeCards = true;

                        ChangeCard changeCard = new ChangeCard("You can change any 3 of your 5 cards. Only 30 seconds are allowed.", true);

                        //send to all players
                        sendMessageConnectedAllPlayers(new JSONObject().put("PLAYERCHANGECARDS", new Gson().toJson(changeCard)).toString());

                        startTimerChangeCards();

                        //send to all about round details
                        iterateOverEveryPlayer(player1 -> {
                            if (player1.getPlayerSession().isOpen()) {
                                try {
                                    player1.getPlayerSession().sendMessage(new TextMessage(new JSONObject().put("ROUNDEND", new Gson().toJson(new Round(roundNumber, maxScore, maxScoredPlayerName))).toString()));
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                    }

                    // send to all
                    iterateOverEveryPlayer(player1 -> {
                        if (player1.getPlayerSession().isOpen()) {
                            try {
                                player1.getPlayerSession().sendMessage(new TextMessage(new JSONObject().put("GAMEEND", new JSONObject().put("message", "Game won by " + maxScoredPlayerName + " with the score of " + maxScore)).toString()));
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    });

                    //clear all player scores and card details and max score
                    iterateOverEveryPlayer((player1) -> {
                        player1.setScore(0);
                        player1.setPlayerHand(null);
                    });

                    gameStarted = false;
                    maxScore = 0;
                    maxScoredPlayerName = null;
                }
            }


        } else if (isJsonObject && jsonObject.has("CHANGECARD") && gameStarted && canPlayerChangeCards) {

            Gson gson = new Gson();
            PlayerChangeCard pcc = gson.fromJson(jsonObject.getJSONObject("CHANGECARD").toString(), PlayerChangeCard.class);

            Player tempPlayer = connectedPlayersList.get(pcc.getPlayerId());

            //player has to match the session with initial session which he used to start the game
            //this way make sures that the same player is requesting from the server
            if (tempPlayer.getPlayerSession() == session) {

                //check if player has already changed 3 cards. if not....
                if (tempPlayer.getNumberOfChangedCards() < 3) {

                    ArrayList<PlayerCard> tempPlayerHand = new ArrayList<>();
                    tempPlayerHand.addAll(tempPlayer.getPlayerHand());

                    //iterate through player's hand
                    for (int i = 0; i < tempPlayerHand.size(); i++) {
                        PlayerCard ph = tempPlayerHand.get(i);

                        //if player hand id == player sent card id from the game,
                        //changes that card with the card which in on the top of the card list
                        if (ph.getCardId() == pcc.getCardId()) {

                            mockScorePlayers();

                            //TODO:old card and new card send to scoring algorithm
                            PlayerCard oldCard = new PlayerCard(ph.getCardId(), ph.getRank(), ph.getSuit(), ph.isInitial());

                            PlayerCard newCard = new PlayerCard(ph.getCardId(), gameCardList.get(0).getRank(), gameCardList.get(0).getSuit(), false);
                            tempPlayerHand.set(i, newCard);
                        }
                    }

                    //replace that player cards with new cards
                    tempPlayer.setPlayerHand(tempPlayerHand);

                    //player only allowed to change 3 cards
                    tempPlayer.setNumberOfChangedCards(tempPlayer.getNumberOfChangedCards() + 1);

                    connectedPlayersList.put(tempPlayer.getPlayerId(), tempPlayer);
                }

            }

            PlayerGameData playerGameData = new PlayerGameData();

            iterateOverEveryPlayer(player -> {
                playerGameData.GAMEDATA.add(player);
            });

            //send to all players
            sendMessageConnectedAllPlayers(new Gson().toJson(playerGameData));
        } else {
            try {
                session.sendMessage(new TextMessage(new JSONObject().put("ERROR", "Something went wrong").toString()));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        super.afterConnectionClosed(session, status);

        iterateOverEveryPlayer(player -> {
            if (player.getPlayerSession() == session) {

                //Send to all
                try {
                    sendMessageConnectedAllPlayers(new JSONObject().put("PLAYERSDISCONNECTED", new JSONObject().put("message", player.getPlayerName() + " disconnected")).toString());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });


        System.out.println("Player Disconnected: " + status);
    }

    private void sendMessageConnectedAllPlayers(String messageNotifyPlayer) throws IOException {

        iterateOverEveryPlayer(player -> {

            if (player.getPlayerSession().isOpen()) {
                try {
                    player.getPlayerSession().sendMessage(new TextMessage(messageNotifyPlayer));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

    }


    private void startGameAndDistributeCards() throws IOException {

//            for (Card c : gameCardList)
//                System.out.println(c.getRank() + " " + c.getSuit());

        //game started
        gameStarted = true;

        //---------ROUND 1----------
        for (int i = 1; i <= 6; i++) {
            if (connectedPlayersList.get(i) != null && !gameCardList.isEmpty()) {

                List<PlayerCard> tempPlayerHand = new ArrayList<>();

                //distribute random first two initial cards to every player
                for (int j = 1; j <= 2; j++) {
                    Random random = new Random();

                    int randomCardNumber = random.nextInt(gameCardList.size());
                    PlayerCard playerCard = new PlayerCard(j, gameCardList.get(randomCardNumber).getRank(), gameCardList.get(randomCardNumber).getSuit(), true);
                    tempPlayerHand.add(playerCard);
                    gameCardList.remove(randomCardNumber);
                }

                Player tempPlayer = connectedPlayersList.get(i);
                tempPlayer.setPlayerHand(tempPlayerHand);
                connectedPlayersList.put(i, tempPlayer);
            }
        }

        //---------ROUND 2----------
        // distribute top card of the list to every one, one by one
        for (int j = 1; j <= 3; j++) {
            for (int i = 1; i <= 6; i++) {
                if (connectedPlayersList.get(i) != null && !gameCardList.isEmpty()) {

                    List<PlayerCard> tempPlayerHand = new ArrayList<>();
                    tempPlayerHand.addAll(connectedPlayersList.get(i).getPlayerHand());

                    //distribute first card on top of the list
                    PlayerCard playerCard = new PlayerCard(j + 2, gameCardList.get(0).getRank(), gameCardList.get(0).getSuit(), false);
                    tempPlayerHand.add(playerCard);
                    gameCardList.remove(0);

                    Player tempPlayer = connectedPlayersList.get(i);
                    tempPlayer.setPlayerHand(tempPlayerHand);
                    connectedPlayersList.put(i, tempPlayer);
                }
            }
        }

        Gson gson = new Gson();
        PlayerGameData playerGameData = new PlayerGameData();

        iterateOverEveryPlayer(player -> {
            playerGameData.GAMEDATA.add(player);
        });

        //send to all
        sendMessageConnectedAllPlayers(gson.toJson(playerGameData));

    }

    private void startTimerEliminatePlayers() {
        //eliminate player every 3 seconds if that player is disconnected
        timerEliminatePlayers = new Timer();
        timerEliminatePlayers.schedule(new EliminatePlayer(), 0, 3000);
    }

    private void startTimerChangeCards() {
        //allocate player to change their 3 cards for 30 seconds
        timerChangeCards = new Timer();
        timerChangeCards.schedule(new ChangeCardsTimeAllocate(), 30000);
    }

    private static void mockScorePlayers() {
        for (int i = 1; i <= 6; i++) {
            Player p = connectedPlayersList.get(i);
            if (p != null) {
                Random r = new Random();

                p.setScore(r.nextInt(50));
            }
        }
    }

    public static void finishTheGame() {
        //wait 5 seconds before finishing the game
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        timerGameDataSender.cancel();

        mockScorePlayers();

        PlayerGameData playerGameData = new PlayerGameData();

        iterateOverEveryPlayer(player -> {
            player.setPlayerReady(false);

            if (maxScore < player.getScore()) {
                maxScore = player.getScore();
                maxScoredPlayerName = player.getPlayerName();
            }

            playerGameData.GAMEDATA.add(player);
        });


        // send to all
        iterateOverEveryPlayer(player -> {
            if (player.getPlayerSession().isOpen()) {
                try {
                    player.getPlayerSession().sendMessage(new TextMessage(new Gson().toJson(playerGameData)));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private static void initializeDeckOfCards() {
        //remove all cards from the list
        gameCardList.clear();

        //get the card pack and shuffle it
        Deck d1 = new Deck();
        d1.populate();
        d1.shuffle();

        gameCardList.addAll(d1.getCards());
    }

    //passing method to execute inside below method (lambda functions)
    public static void iterateOverEveryPlayer(Consumer<Player> playerConsumer) {
        for (int i = 1; i <= 6; i++) {
            Player p = connectedPlayersList.get(i);

            if (p != null) {

                //any method passed here, get executed
                playerConsumer.accept(p);

            }
        }
    }
}
