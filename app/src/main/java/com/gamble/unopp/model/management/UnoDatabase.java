package com.gamble.unopp.model.management;

import com.gamble.unopp.model.cards.Card;
import com.gamble.unopp.model.game.GameSession;
import com.gamble.unopp.model.game.Player;

import java.util.ArrayList;

/**
 * Created by Mario on 04.05.2015.
 */
public class UnoDatabase {

    private static UnoDatabase instance;

    private Player          localPlayer;
    private GameSession     currentGameSession;
    private ArrayList<Card> deck;

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

    public ArrayList<Card> getDeck() {
        return deck;
    }

    public void setDeck(ArrayList<Card> deck) {
        this.deck = deck;
    }
}
