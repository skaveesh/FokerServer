package com.savick.foker.game;

import com.google.gson.Gson;
import com.savick.foker.gson.ChangeCard;
import com.savick.foker.websocket.SessionHandler;
import org.json.JSONObject;
import org.springframework.web.socket.TextMessage;

import java.io.IOException;
import java.util.TimerTask;

/**
 * Created by samintha on 6/23/2017.
 */
public class ChangeCardsTimeAllocate extends TimerTask {

    public void run() {
        SessionHandler.canPlayerChangeCards = false;
        SessionHandler.timerChangeCards.cancel();

        //send to all players
        ChangeCard changeCard = new ChangeCard("You time is up", SessionHandler.canPlayerChangeCards);

        SessionHandler.iterateOverEveryPlayer(player -> {
            if (player.getPlayerSession().isOpen()) {
                try {
                    player.getPlayerSession().sendMessage(new TextMessage(new JSONObject().put("PLAYERCHANGECARDS", new Gson().toJson(changeCard)).toString()));

                    //after this game will end the round
                    //wait 5 seconds before ending the game
                    Thread.sleep(5000);
                } catch (IOException | InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });

        SessionHandler.finishTheGame();
    }
}
