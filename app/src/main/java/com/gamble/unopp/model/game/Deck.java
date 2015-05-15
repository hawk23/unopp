package com.gamble.unopp.model.game;

import com.gamble.unopp.model.cards.Card;

import java.util.ArrayList;

/**
 * Created by Albert on 15.05.2015.
 */
public class Deck {

    private ArrayList<Card> stack;
    private ArrayList<Card> playedStack;
    private ArrayList<Card> deck;

    public Deck() {
        this.stack = new ArrayList<Card>();
        this.playedStack = new ArrayList<Card>();
        this.deck = DeckGenerator.createDeck();
    }

    public int sizeOfStack() {
        return this.stack.size();
    }

    public int sizeOfPlayedStack() {
        return this.playedStack.size();
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

    public Card getTopCard() {
        return this.playedStack.get(sizeOfPlayedStack() - 1);
    }

}
