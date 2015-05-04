package com.gamble.unopp.model.management;

import com.gamble.unopp.model.GameSession;
import com.gamble.unopp.model.Player;

/**
 * Created by Mario on 04.05.2015.
 */
public class UnoDatabase {

    private static UnoDatabase instance;

    private Player          localPlayer;
    private GameSession     currentGameSession;

    private UnoDatabase () {

    }

    public static UnoDatabase getInstance () {

        if (instance == null) {
            instance = new UnoDatabase();
        }

        return instance;
    }

    public Player getLocalPlayer() {
        return localPlayer;
    }

    public void setLocalPlayer(Player localPlayer) {
        this.localPlayer = localPlayer;
    }

    public GameSession getCurrentGameSession() {
        return currentGameSession;
    }

    public void setCurrentGameSession(GameSession currentGameSession) {
        this.currentGameSession = currentGameSession;
    }
}
