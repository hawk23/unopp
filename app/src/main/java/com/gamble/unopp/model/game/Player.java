package com.gamble.unopp.model.game;

import com.gamble.unopp.helper.CardComparator;
import com.gamble.unopp.model.ModelObject;
import com.gamble.unopp.model.cards.Card;
import com.gamble.unopp.model.game.GameSession;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by Albert on 02.05.2015.
 */
public class Player extends ModelObject implements Serializable {

    private int ID;
    private String name;
    private transient GameSession gameSession;
    private transient ArrayList<Card> hand;

    private boolean uno;
    private boolean hasToChooseColor;
    private boolean hasToCallUno;

    public Player(int ID, String name) {
        this.ID = ID;
        this.gameSession = null;
        this.name = name;
        this.uno = false;
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

        Collections.sort(this.hand, new CardComparator());
    }

    public void addCardsToHand(ArrayList<Card> cards) {
        for (Card card : cards) {
            this.hand.add(card);
        }
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

    public boolean isHost () {
        if (this.gameSession != null && this.gameSession.getHost() != null) {
            if (this.gameSession.getHost().getID() == this.getID()) {
                return true;
            }
        }

        return false;
    }

    public boolean isHasToCallUno() {
        return hasToCallUno;
    }

    public void setHasToCallUno(boolean hasToCallUno) {
        this.hasToCallUno = hasToCallUno;
    }
}
