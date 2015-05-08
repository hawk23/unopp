package com.gamble.unopp.model.game;

import com.gamble.unopp.model.GameLogic;
import com.gamble.unopp.model.Player;
import com.gamble.unopp.model.cards.Card;
import com.gamble.unopp.model.cards.UnoColor;

import java.util.ArrayList;

/**
 * Created by Albert on 05.05.2015.
 */
public class GameState {

    private GameLogic logic;
    private GameRound gameRound;
    private UnoDirection direction;
    private Card topCard;
    private int drawCounter;
    private Player actualPlayer;
    private Player nextPlayer;
    private UnoColor actualColor;

    public GameState(GameRound gameRound, int[] shuffledCardIDs) {
        this.gameRound = gameRound;
        this.logic = new GameLogic(this, shuffledCardIDs);
    }

    public UnoDirection getDirection() {
        return direction;
    }

    public void setDirection(UnoDirection direction) {
        this.direction = direction;
    }

    public Card getTopCard() {
        return topCard;
    }

    public void setTopCard(Card topCard) {
        this.topCard = topCard;
    }

    public int getDrawCounter() {
        return drawCounter;
    }

    public void setDrawCounter(int drawCounter) {
        this.drawCounter = drawCounter;
    }

    public Player getActualPlayer() {
        return actualPlayer;
    }

    public void setActualPlayer(Player actualPlayer) {
        this.actualPlayer = actualPlayer;
    }

    public Player getNextPlayer() {
        return nextPlayer;
    }

    public void setNextPlayer(Player nextPlayer) {
        this.nextPlayer = nextPlayer;
    }

    public UnoColor getActualColor() {
        return actualColor;
    }

    public void setActualColor(UnoColor actualColor) {
        this.actualColor = actualColor;
    }
}

