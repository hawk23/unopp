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
    private Deck deck;
    private UnoDirection direction;
    private int drawCounter;
    private Player actualPlayer;
    private UnoColor actualColor;

    public GameState(GameRound gameRound) {
        this.gameRound      = gameRound;
        this.deck           = new Deck();
        this.logic          = new GameLogic(this);
    }

    public boolean doTurn(Turn turn) {
        boolean valid = this.logic.checkTurn(turn);

        if (valid) {
            this.logic.doTurn(turn);
        }

        return valid;
    }

    public ArrayList<Card> getStack() {
        return this.deck.getStack();
    }

    public void setStack(ArrayList<Card> stack) {
        this.deck.setStack(stack);
    }

    public ArrayList<Card> getPlayedStack() {
        return this.deck.getPlayedStack();
    }

    public void setPlayedStack(ArrayList<Card> playedStack) {
        this.deck.setPlayedStack(playedStack);
    }

    public void setPlayerHand(int PlayerID, ArrayList<Card> hand) {

        for (Player p : this.getPlayers()) {
            if (p.getID() == PlayerID) {
                p.setHand(hand);
                break;
            }
        }
    }

    public ArrayList<Player> getPlayers() {
        return this.gameRound.getPlayers();
    }

    public void nextPlayer() {

        if (this.direction == UnoDirection.CLOCKWISE) {

            if (this.getPlayers().indexOf(this.actualPlayer) + 1 < this.getPlayers().size()) {
                actualPlayer = this.getPlayers().get(this.getPlayers().indexOf(this.actualPlayer) + 1);
            }
            else {
                actualPlayer = this.getPlayers().get(0);
            }
        }
        else {

            if (this.getPlayers().indexOf(this.actualPlayer) - 1 >= 0) {
                actualPlayer = this.getPlayers().get(this.getPlayers().indexOf(this.actualPlayer) - 1);
            }
            else {
                actualPlayer = this.getPlayers().get(this.getPlayers().size() - 1);
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

    public ArrayList<Card> popFromStack(int amount) {
       return this.deck.popFromStack(amount);
    }

    public void pushToPlayedStack(Card card) {
        this.deck.pushToPlayedStack(card);
    }

    public int sizeOfStack() {
        return this.deck.sizeOfStack();
    }

    public int sizeOfPlayedStack() {
        return this.deck.sizeOfPlayedStack();
    }

    public UnoDirection getDirection() {
        return direction;
    }

    public void setDirection(UnoDirection direction) {
        this.direction = direction;
    }

    public Card getTopCard() {
        return this.deck.getTopCard();
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

