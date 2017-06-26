package com.savick.foker.game;

import com.google.gson.Gson;
import com.savick.foker.gson.ChangeCard;
import com.savick.foker.gson.PlayerGameData;
import com.savick.foker.websocket.SessionHandler;
import org.json.JSONObject;
import org.springframework.web.socket.TextMessage;

import java.io.IOException;

/**
 * @author Samintha Kaveesh.
 */

public class ChangeCardsTimeAllocate implements Runnable {

    public void run() {

        SessionHandler.canPlayerChangeCards = false;
        SessionHandler.timerChangeCards.shutdown();

        //send to all players
        ChangeCard changeCard = new ChangeCard("You time is up", SessionHandler.canPlayerChangeCards);

        SessionHandler.iterateOverEveryPlayer(player -> {
            if (player.getPlayerSession().isOpen()) {
                try {
                    synchronized (player.getPlayerSession()) {
                        player.getPlayerSession().sendMessage(new TextMessage(new JSONObject().put("PLAYERCHANGECARDS", new JSONObject(new Gson().toJson(changeCard))).toString()));
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        //calculate score
        SessionHandler.iterateOverEveryPlayer(player -> {

            //check if player changed any card during the game
            boolean playerChangedCards = player.getNumberOfChangedCards() > 0;

            if (!playerChangedCards)
                player.setScore(player.getScore() + SessionHandler.getHandScoreAfterReplacing(player.getPlayerHand(), player.getPlayerHand()));
        });

        PlayerGameData playerGameData = new PlayerGameData();

        SessionHandler.iterateOverEveryPlayer(player -> {

            if (SessionHandler.maxScore < player.getScore()) {
                SessionHandler.maxScore = player.getScore();
                SessionHandler.maxScoredPlayerName = player.getPlayerName();
            }

            playerGameData.GAMEDATA.add(player);
        });


        // send to all
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
