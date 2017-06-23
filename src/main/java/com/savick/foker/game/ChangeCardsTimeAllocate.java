package com.savick.foker.game;

import com.savick.foker.websocket.SessionHandler;
import org.json.JSONObject;

import java.util.TimerTask;

/**
 * Created by samintha on 6/23/2017.
 */
public class ChangeCardsTimeAllocate extends TimerTask {

    public void run() {
        SessionHandler.canPlayerChangeCards = false;
        SessionHandler.timerChangeCards.cancel();

        //TODO: send this to all players
        JSONObject jsonPlayerCards = new JSONObject();
        jsonPlayerCards.put("message","You time is up");
        jsonPlayerCards.put("isAllowed",SessionHandler.canPlayerChangeCards);
        System.out.println(new JSONObject().put("PLAYERCHANGECARDS",jsonPlayerCards));


        SessionHandler.finishTheGame();
    }
}
