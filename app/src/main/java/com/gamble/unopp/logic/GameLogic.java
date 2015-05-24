package com.gamble.unopp.logic;

import com.gamble.unopp.model.cards.Action;
import com.gamble.unopp.model.cards.ActionCard;
import com.gamble.unopp.model.cards.ActionType;
import com.gamble.unopp.model.cards.Card;
import com.gamble.unopp.model.cards.NumberCard;
import com.gamble.unopp.model.game.GameState;
import com.gamble.unopp.model.game.Player;
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

        if (turn.getPlayer() == this.state.getActualPlayer()) {

            switch (turn.getType()) {

                case DRAW:
                    if (!turn.getPlayer().hasToChooseColor()) {
                        valid = true;
                    }
                    break;

                case PLAY_CARD:
                    if (turn.getPlayer().getHand().size() == 1 && !turn.getPlayer().isUno()) {
                        // Maybe set punishment draw 2 cards
                    }
                    else if (checkCard(turn.getCard())) {
                        valid = true;
                    }
                    break;

                case CHOOSE_COLOR:
                    if (turn.getPlayer().hasToChooseColor()) {
                        valid = true;
                    }
                    break;
                case CALL_UNO:
                    if (turn.getPlayer().getHand().size() == 1) {
                        valid = true;
                    }
                    break;
            }
        }
        return valid;
    }

    public void doTurn(Turn turn) {

        Player nextPlayer = null;

        switch (turn.getType()) {

            case DRAW:
                if (this.state.getDrawCounter() == 0) {
                    turn.getPlayer().addCardsToHand(this.state.popFromStack(1));
                }
                else {
                    turn.getPlayer().addCardsToHand(this.state.popFromStack(this.state.getDrawCounter()));
                    this.state.setDrawCounter(0);
                }

                if (turn.getPlayer().isUno() && turn.getPlayer().getHand().size() > 1) {
                    turn.getPlayer().setUno(false);
                }
                nextPlayer = this.state.nextPlayer();

                if (this.state.getNextActualPlayer() != null && turn.getPlayer().isHasToCallUno()) {
                    turn.getPlayer().setHasToCallUno(false);
                    this.state.setActualPlayer(this.state.getNextActualPlayer());
                    this.state.setNextActualPlayer(null);
                }
                else {
                    this.state.setActualPlayer(nextPlayer);
                }
                break;

            case PLAY_CARD:
                // push turnCard to playedStack and remove turnCard from turnPlayer's hand
                this.state.pushToPlayedStack(turn.getCard());
                turn.getPlayer().removeCardFromHand(turn.getCard());

                if (turn.getCard() instanceof NumberCard) {
                    NumberCard nCard = (NumberCard) turn.getCard();
                    this.state.setActualColor(nCard.getColor());
                    nextPlayer = this.state.nextPlayer();
                }
                else {

                    ActionCard aCard = (ActionCard) turn.getCard();
                    ArrayList<Action> actions = aCard.getActions();

                    if (actions.size() == 2) {
                        this.state.setDrawCounter(this.state.getDrawCounter() + 4);
                        turn.getPlayer().setHasToChooseColor(true);
                    }
                    else {
                        int actionType = actions.get(0).getActionType().getType();

                        if (actionType == ActionType.CHANGE_COLOR) {
                            turn.getPlayer().setHasToChooseColor(true);
                        }
                        else if (actionType == ActionType.ADD2) {
                            this.state.setDrawCounter(this.state.getDrawCounter() + 2);
                            this.state.setActualColor(aCard.getColor());
                            nextPlayer = this.state.nextPlayer();
                        }
                        else if (actionType == ActionType.CHANGE_DIRECTION) {
                            this.state.changeDirection();
                            this.state.setActualColor(aCard.getColor());
                            if (this.state.getPlayers().size() > 2) {
                                nextPlayer = this.state.nextPlayer();
                            }
                        }
                        else if (actionType == ActionType.SKIP_TURN) {
                            this.state.setActualColor(aCard.getColor());
                            nextPlayer = this.state.skipPlayer();
                        }
                    }
                }

                if (turn.getPlayer().getHand().size() == 0) {
                    this.state.setWinner(turn.getPlayer());
                }
                else if (turn.getPlayer().getHand().size() == 1) {
                    this.state.setNextActualPlayer(nextPlayer);
                    turn.getPlayer().setHasToCallUno(true);
                }
                else if (nextPlayer != null) {
                    this.state.setActualPlayer(nextPlayer);
                }
                break;

            case CHOOSE_COLOR:
                this.state.setActualColor(turn.getColor());
                turn.getPlayer().setHasToChooseColor(false);

                nextPlayer = this.state.nextPlayer();

                if (!turn.getPlayer().isHasToCallUno()) {

                    // only switch to next player if player don't has to call uno
                    this.state.setActualPlayer(nextPlayer);
                }
                else {
                    // remember the next player and give current player chance to call uno
                    this.state.setNextActualPlayer(nextPlayer);
                }
                break;

            case CALL_UNO:
                turn.getPlayer().setHasToCallUno(false);
                turn.getPlayer().setUno(true);

                this.state.setActualPlayer(this.state.getNextActualPlayer());
                this.state.setNextActualPlayer(null);
                break;
        }

        this.state.setLocalUpdateID(turn.getID());
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
