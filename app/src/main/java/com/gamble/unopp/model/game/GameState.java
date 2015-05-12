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
    private ArrayList<Card> stack;
    private ArrayList<Card> playedStack;
    private Card topCard;
    private UnoDirection direction;
    private int drawCounter;
    private Player actualPlayer;
    private UnoColor actualColor;

    public GameState(GameRound gameRound) {
        this.gameRound      = gameRound;
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
        return stack;
    }

    public void setStack(ArrayList<Card> stack) {
        this.stack = stack;
    }

    public ArrayList<Card> getPlayedStack() {
        return playedStack;
    }

    public void setPlayedStack(ArrayList<Card> playedStack) {
        this.playedStack = playedStack;
    }

    public void setPlayerHand(int PlayerID, ArrayList<Card> hand) {

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
        // ToDo: check sizeofstack and if not enough cards available - generate new deck or take playedStack - shuffle and push to stack;

        ArrayList<Card> drawnCards = new ArrayList<Card>();

        for (int n = amount; n > 0; n--) {
            drawnCards.add(stack.get(sizeOfStack() - 1));
            stack.remove(sizeOfStack() - 1);
        }

        return drawnCards;
    }

    public void pushToPlayedStack(Card card) {
        this.playedStack.add(card);
    }

    public int sizeOfStack() {
        return this.stack.size();
    }

    public int sizeOfPlayedStack() {
        return this.playedStack.size();
    }

    public UnoDirection getDirection() {
        return direction;
    }

    public void setDirection(UnoDirection direction) {
        this.direction = direction;
    }

    public Card getTopCard() {
        return this.playedStack.get(sizeOfPlayedStack() - 1);
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

