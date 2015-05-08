package com.gamble.unopp.model;

import com.gamble.unopp.model.cards.Card;

import org.w3c.dom.Element;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

/**
 * Created by Albert on 02.05.2015.
 */
public class Player extends ModelObject {

    private int ID;
    private String name;
    private GameSession gameSession;
    private ArrayList<Card> hand;

    private boolean isActive;
    private boolean hasDrawn;
    private boolean hasToChooseColor;

    public Player(int ID, String name) {
        this.ID = ID;
        this.name = name;
        this.gameSession = null;
        this.isActive = false;
        this.hasDrawn = false;
        this.hasToChooseColor = false;

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

    public boolean isActive() {
        return isActive;
    }

    public void setIsActive(boolean isActive) {
        this.isActive = isActive;
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

    @Override
    public String toString() {
        return this.name;
    }
}
