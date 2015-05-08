package com.gamble.unopp.model;

import com.gamble.unopp.model.cards.Action;
import com.gamble.unopp.model.cards.ActionCard;
import com.gamble.unopp.model.cards.ActionType;
import com.gamble.unopp.model.cards.Card;
import com.gamble.unopp.model.cards.NumberCard;
import com.gamble.unopp.model.game.CardDeck;
import com.gamble.unopp.model.game.GameState;
import com.gamble.unopp.model.game.Turn;

import java.util.ArrayList;

/**
 * Created by Albert on 08.05.2015.
 */
public class GameLogic {

    private CardDeck deck;
    private ArrayList<Card> stack;
    private ArrayList<Card> playedStack;
    private GameState state;

    public GameLogic (GameState state, int[] shuffledCardIDs) {
        this.state = state;
        this.stack = new ArrayList<Card>();
        this.playedStack = new ArrayList<Card>();
        this.deck = new CardDeck();
        initDeck(shuffledCardIDs);
    }

    private void initDeck(int[] shuffledCardIDs) {
        ArrayList<Card> cards = this.deck.getDeck();

        // build stack
        for (int i = 0; i < shuffledCardIDs.length; i++) {
            int id = shuffledCardIDs[i];
            this.stack.add(cards.get(id));
        }

        Card card = popFromStack(1).get(0);
        pushToPlayedStack(card);
        initInitialState();
    }

    private void initInitialState() {
        // ToDo: check card, direction etc to init the beginning
    }

    public ArrayList<Card> popFromStack(int amount) {
        // ToDo: check sizeofstack and if not enough cards available - generate new deck

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

    public int sizeOfStack() {
        return this.stack.size();
    }

    public int sizeOfPlayedStack() {
        return this.playedStack.size();
    }

    public boolean checkTurn(Turn turn) {
        boolean valid = false;

        // player states have to be set correctly in doTurn etc.
        // e.g. only an active player can draw
        // if one has drawn he/she cannot draw again
        // one cannot 'call' next without drawing a card
        // after playing a card next will be called automatically
        // ...
        switch (turn.getType()) {

            case Turn.DRAW:
                if (!turn.getPlayer().hasDrawn()) {
                    valid = true;
                    doTurn(turn);
                }
                break;

            case Turn.PLAY_CARD:
                if (checkCard(turn.getCard())) {
                    valid = true;
                    doTurn(turn);
                }
                break;

            case Turn.NEXT:
                if (turn.getPlayer().hasDrawn()) {
                    valid = true;
                    doTurn(turn);
                }
                break;

            case Turn.CHOOSE_COLOR:
                if (turn.getPlayer().hasToChooseColor()) {
                    valid = true;
                    doTurn(turn);
                }
                break;
        }


        return valid;
    }

    private void doTurn(Turn turn) {
        // ToDo: update GameState and Player State

        switch (turn.getType()) {
            case Turn.DRAW: break;
            case Turn.PLAY_CARD: break;
            case Turn.NEXT: break;
            case Turn.CHOOSE_COLOR: break;
        }
    }

    /**
     * NOT TESTED
     * @param card
     * @return true if the play-card-turn is valid, else false
     */
    private boolean checkCard(Card card) {
        boolean valid = false;

        Card topCard = getTopCard();

        // if the TopCard is a NumberCard
        if (topCard instanceof NumberCard) {

            if (card instanceof NumberCard) {
                if (((NumberCard) card).getValue() == ((NumberCard) topCard).getValue()
                        || card.getColor() == this.state.getActualColor()) {
                    valid = true;
                }
            }
            else if (card instanceof ActionCard) {
                ArrayList<Action> actions = ((ActionCard) card).getActions();

                if (actions.size() == 2) {
                    // card is ADD4
                    valid = true;
                }
                else {
                    int actionType = actions.get(0).getActionType().getType();

                    if (actionType == ActionType.CHANGE_COLOR) {
                        valid = true;
                    }
                    else if(actionType == ActionType.ADD2) {
                        if (card.getColor() == this.state.getActualColor()) {
                            valid = true;
                        }
                    }
                    else if (actionType == ActionType.CHANGE_DIRECTION) {
                        if (card.getColor() == this.state.getActualColor()) {
                            valid = true;
                        }
                    }
                    else if (actionType == ActionType.SKIP_TURN) {
                        if (card.getColor() == this.state.getActualColor()) {
                            valid = true;
                        }
                    }
                }
            }
        }
        // else TopCard is an ActionCard
        else if (topCard instanceof ActionCard) {
            ArrayList<Action> actions = ((ActionCard) topCard).getActions();

            if (actions.size() == 2) {
                // TopCard is ADD4

                if (card instanceof NumberCard) {
                    if (this.state.getDrawCounter() == 0 && this.state.getActualColor() == card.getColor()) {
                        valid = true;
                    }
                }
                else if (card instanceof ActionCard) {
                    ArrayList<Action> actions2 = ((ActionCard) card).getActions();

                    if (actions2.size() == 2) {
                        // card is ADD4
                        valid = true;
                    }
                    else {
                        int actionType2 = actions2.get(0).getActionType().getType();

                        if (actionType2 == ActionType.CHANGE_COLOR) {
                            if (this.state.getDrawCounter() == 0) {
                                valid = true;
                            }
                        }
                        else if (actionType2 == ActionType.ADD2) {
                            if (this.state.getActualColor() == card.getColor()) {
                                valid = true;
                            }
                        }
                        else if (actionType2 == ActionType.CHANGE_DIRECTION) {
                            if (this.state.getDrawCounter() == 0 && this.state.getActualColor() == card.getColor()) {
                                valid = true;
                            }
                        }
                        else if (actionType2 == ActionType.SKIP_TURN) {
                            if (this.state.getDrawCounter() == 0 && this.state.getActualColor() == card.getColor()) {
                                valid = true;
                            }
                        }
                    }
                }
            }
            else {
                int actionType = actions.get(0).getActionType().getType();

                if (actionType == ActionType.CHANGE_COLOR) {

                    if (card instanceof NumberCard) {
                        if (this.state.getActualColor() == card.getColor()) {
                            valid = true;
                        }
                    }
                    else if (card instanceof ActionCard) {
                        ArrayList<Action> actions2 = ((ActionCard) card).getActions();

                        if (actions2.size() == 2) {
                            valid = true;
                        }
                        else {
                            int actionType2 = actions2.get(0).getActionType().getType();

                            if (actionType2 == ActionType.CHANGE_COLOR) {
                                valid = true;
                            }
                            else if (actionType2 == ActionType.ADD2) {
                                if (this.state.getActualColor() == card.getColor()) {
                                    valid = true;
                                }
                            }
                            else if (actionType2 == ActionType.CHANGE_DIRECTION) {
                                if (this.state.getActualColor() == card.getColor()) {
                                    valid = true;
                                }
                            }
                            else if (actionType2 == ActionType.SKIP_TURN) {
                                if (this.state.getActualColor() == card.getColor()) {
                                    valid = true;
                                }
                            }
                        }
                    }
                }
                else if(actionType == ActionType.ADD2) {

                    if (card instanceof NumberCard) {
                        if (this.state.getDrawCounter() == 0 && this.state.getActualColor() == card.getColor()) {
                            valid = true;
                        }
                    }
                    else if (card instanceof ActionCard) {
                        ArrayList<Action> actions2 = ((ActionCard) card).getActions();

                        if (actions2.size() == 2) {
                            if (this.state.getDrawCounter() == 0) {
                                valid = true;
                            }
                        }
                        else {
                            int actionType2 = actions2.get(0).getActionType().getType();

                            if (actionType2 == ActionType.CHANGE_COLOR) {
                                if (this.state.getDrawCounter() == 0) {
                                    valid = true;
                                }
                            }
                            else if (actionType2 == ActionType.ADD2) {
                                    valid = true;
                            }
                            else if (actionType2 == ActionType.CHANGE_DIRECTION) {
                                if (this.state.getDrawCounter() == 0 && this.state.getActualColor() == card.getColor()) {
                                    valid = true;
                                }
                            }
                            else if (actionType2 == ActionType.SKIP_TURN) {
                                if (this.state.getDrawCounter() == 0 && this.state.getActualColor() == card.getColor()) {
                                    valid = true;
                                }
                            }
                        }
                    }

                }
                else if (actionType == ActionType.CHANGE_DIRECTION) {

                    if (card instanceof NumberCard) {
                        if (this.state.getActualColor() == card.getColor()) {
                            valid = true;
                        }
                    }
                    else if (card instanceof ActionCard) {
                        ArrayList<Action> actions2 = ((ActionCard) card).getActions();

                        if (actions2.size() == 2) {
                            // card is ADD4
                            valid = true;
                        }
                        else {
                            int actionType2 = actions2.get(0).getActionType().getType();

                            if (actionType2 == ActionType.CHANGE_COLOR) {
                                valid = true;
                            }
                            else if (actionType2 == ActionType.ADD2) {
                                if (this.state.getActualColor() == card.getColor()) {
                                    valid = true;
                                }
                            }
                            else if (actionType2 == ActionType.CHANGE_DIRECTION) {
                                valid = true;
                            }
                            else if (actionType2 == ActionType.SKIP_TURN) {
                                if (this.state.getActualColor() == card.getColor()) {
                                    valid = true;
                                }
                            }
                        }
                    }
                }
                else if (actionType == ActionType.SKIP_TURN) {

                    if (card instanceof NumberCard) {
                        if (this.state.getActualColor() == card.getColor()) {
                            valid = true;
                        }
                    }
                    else if (card instanceof ActionCard) {
                        ArrayList<Action> actions2 = ((ActionCard) card).getActions();

                        if (actions2.size() == 2) {
                            // card is ADD4
                            valid = true;
                        }
                        else {
                            int actionType2 = actions2.get(0).getActionType().getType();

                            if (actionType2 == ActionType.CHANGE_COLOR) {
                                valid = true;
                            }
                            else if (actionType2 == ActionType.ADD2) {
                                if (this.state.getActualColor() == card.getColor()) {
                                    valid = true;
                                }
                            }
                            else if (actionType2 == ActionType.CHANGE_DIRECTION) {
                                if (this.state.getActualColor() == card.getColor()) {
                                    valid = true;
                                }
                            }
                            else if (actionType2 == ActionType.SKIP_TURN) {
                                valid = true;
                            }
                        }
                    }
                }
            }
        }

        return valid;
    }
}
