package com.gamble.unopp.logic;

import com.gamble.unopp.model.cards.Action;
import com.gamble.unopp.model.cards.ActionCard;
import com.gamble.unopp.model.cards.ActionType;
import com.gamble.unopp.model.cards.Card;
import com.gamble.unopp.model.cards.NumberCard;
import com.gamble.unopp.model.game.GameState;
import com.gamble.unopp.model.game.Turn;

import java.util.ArrayList;

/**
 * Created by Albert on 08.05.2015.
 */
public class GameLogic {

    private GameState state;

    public GameLogic (GameState state) {
        this.state = state;
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

            case DRAW:
                if (!turn.getPlayer().hasDrawn()) {
                    valid = true;
                }
                break;

            case PLAY_CARD:
                if (checkCard(turn.getCard())) {
                    valid = true;
                }
                break;

            case NEXT:
                if (turn.getPlayer().hasDrawn()) {
                    valid = true;
                }
                break;

            case CHOOSE_COLOR:
                if (turn.getPlayer().hasToChooseColor()) {
                    valid = true;
                }
                break;
            case CALL_UNO: break;
        }


        return valid;
    }

    public void doTurn(Turn turn) {
        // ToDo: update GameState and Player State

        switch (turn.getType()) {
            case DRAW:
                if (this.state.getDrawCounter() == 0) {
                    turn.getPlayer().addCardsToHand(this.state.popFromStack(1));
                }
                else {
                    turn.getPlayer().addCardsToHand(this.state.popFromStack(this.state.getDrawCounter()));
                }
                turn.getPlayer().setHasDrawn(true);
                break;
            case PLAY_CARD:

                break;
            case NEXT:

                break;
            case CHOOSE_COLOR: break;
            case CALL_UNO: break;
        }
    }

    /**
     * NOT TESTED
     * @param card
     * @return true if the play-card-turn is valid, else false
     */
    private boolean checkCard(Card card) {
        boolean valid = false;

        Card topCard = state.getTopCard();

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
