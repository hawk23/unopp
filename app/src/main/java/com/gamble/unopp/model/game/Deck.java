package com.gamble.unopp.model.game;

import com.gamble.unopp.GameSettings;
import com.gamble.unopp.model.cards.Card;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by Albert on 15.05.2015.
 */
public class Deck {

    private ArrayList<Card> stack;
    private ArrayList<Card> playedStack;
    private int deckCount;
    private static final int SHUFFLE_SEED = 99;

    public Deck() {
        this.stack = new ArrayList<Card>();
        this.playedStack = new ArrayList<Card>();
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

        if (amount > sizeOfStack()) {

            ArrayList<Card> cardsTooShuffle = new ArrayList<Card>();
            for (int i = this.playedStack.size() - 1; i >= 0 ; i--) {
                cardsTooShuffle.add(this.playedStack.get(i));
                this.playedStack.remove(i);
            }

            this.playedStack.add(cardsTooShuffle.get(0));
            cardsTooShuffle.remove(0);

            cardsTooShuffle = shuffle(cardsTooShuffle);
            for (Card c : this.stack) {
                cardsTooShuffle.add(c);
            }
            this.stack = cardsTooShuffle;
            this.deckCount++;
        }

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
        if (!playedStack.isEmpty()) {
            return this.playedStack.get(sizeOfPlayedStack() - 1);
        } else {
            return null;
        }
    }

    // ToDo TESTING
    private ArrayList<Card> shuffle(ArrayList<Card> cards) {

        Random rand = new Random(Deck.SHUFFLE_SEED);
        for (int i = 0; i < cards.size(); i++) {
            int a = rand.nextInt(cards.size() - 1);
            int b = rand.nextInt(cards.size() - 1);

            Card temp = cards.get(a);
            cards.add(a, cards.get(b));
            cards.add(b, temp);
        }

        return cards;
    }



}
