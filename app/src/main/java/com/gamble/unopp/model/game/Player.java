package com.gamble.unopp.model.game;

import com.gamble.unopp.model.ModelObject;
import com.gamble.unopp.model.cards.Card;
import com.gamble.unopp.model.game.GameSession;

import java.util.ArrayList;

/**
 * Created by Albert on 02.05.2015.
 */
public class Player extends ModelObject {

    private int ID;
    private String name;
    private GameSession gameSession;
    private ArrayList<Card> hand;

    private boolean uno;
    private boolean hasDrawn;
    private boolean hasToChooseColor;

    public Player(int ID, String name) {
        this.ID = ID;
        this.gameSession = null;
        this.name = name;
        this.uno = false;
        this.hasDrawn = false;
        this.hasToChooseColor = false;
        this.hand = new ArrayList<Card>();

    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public int getID() {
        return ID;
    }

    public String getName() {
        return name;
    }

    public GameSession getGameSession() {
        return gameSession;
    }

    public void setGameSession(GameSession gameSession) {
        this.gameSession = gameSession;
    }

    public boolean joinGameSession(GameSession gameSession) {
        return gameSession.addPlayer(this);
    }

    public boolean leaveGameSession() {
        return this.gameSession != null && this.gameSession.removePlayer(this);
    }

    public void sortHand () {

        // TODO
    }

    public void addCardsToHand(ArrayList<Card> cards) {
        for (Card card : cards) {
            this.hand.add(card);
        }
    }

    public boolean hasDrawn() {
        return hasDrawn;
    }

    public void setHasDrawn(boolean hasDrawn) {
        this.hasDrawn = hasDrawn;
    }

    public boolean hasToChooseColor() {
        return hasToChooseColor;
    }

    public void setHasToChooseColor(boolean hasToChooseColor) {
        this.hasToChooseColor = hasToChooseColor;
    }

    public void removeCardFromHand(Card card) {
        for (Card c : hand) {
            if (c.getID() == card.getID()) {
                hand.remove(c);
                break;
            }
        }
    }

    public int getHandCount() {
        return this.hand.size();
    }

    public ArrayList<Card> getHand() {
        return hand;
    }

    public void setHand(ArrayList<Card> hand) {
        this.hand = hand;
    }

    public boolean isUno() {
        return uno;
    }

    public void setUno(boolean uno) {
        this.uno = uno;
    }

    @Override
    public String toString() {
        return this.name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Player player = (Player) o;

        if (ID != player.ID) return false;

        return true;
    }
}
