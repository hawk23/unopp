package com.gamble.unopp.model.game;

import com.gamble.unopp.logic.GameLogic;
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
    private UnoColor actualColor;
    private ArrayList<Player> playerOrder;

    public GameState(GameRound gameRound, int[] shuffledCardIDs, int[] playerOrder) {
        this.gameRound = gameRound;
        this.playerOrder = new ArrayList<Player>();
        initPlayerOrder(playerOrder);
        this.logic = new GameLogic(this, shuffledCardIDs);
        this.logic.setInitialState();
    }

    private void initPlayerOrder(int[] playerOrder) {
        int n = 0;
        while (n < playerOrder.length) {
            for (Player p : this.gameRound.getPlayers()) {
                if (playerOrder[n] == p.getID()) {
                    this.playerOrder.add(p);
                    n++;
                    break;
                }
            }
        }
        actualPlayer = this.playerOrder.get(0);
    }

    public void nextPlayer() {

        if (this.direction == UnoDirection.CLOCKWISE) {

            if (this.playerOrder.indexOf(this.actualPlayer) + 1 < this.playerOrder.size()) {
                actualPlayer = this.playerOrder.get(this.playerOrder.indexOf(this.actualPlayer) + 1);
            }
            else {
                actualPlayer = this.playerOrder.get(0);
            }
        }
        else {

            if (this.playerOrder.indexOf(this.actualPlayer) - 1 >= 0) {
                actualPlayer = this.playerOrder.get(this.playerOrder.indexOf(this.actualPlayer) - 1);
            }
            else {
                actualPlayer = this.playerOrder.get(this.playerOrder.size() - 1);
            }
        }
    }

    public void skipPlayer() {
        this.nextPlayer();
        this.nextPlayer();
    }

    public void changeDirection() {
        if (this.direction == UnoDirection.CLOCKWISE) {
            this.direction = UnoDirection.COUNTERCLOCKWISE;
        }
        else {
            this.direction = UnoDirection.CLOCKWISE;
        }
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

    public UnoColor getActualColor() {
        return actualColor;
    }

    public void setActualColor(UnoColor actualColor) {
        this.actualColor = actualColor;
    }
}

