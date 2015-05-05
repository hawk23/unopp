package com.gamble.unopp.model.game;

import com.gamble.unopp.model.Player;
import com.gamble.unopp.model.cards.Card;
import com.gamble.unopp.model.cards.UnoColor;

/**
 * Created by Albert on 05.05.2015.
 */
public class GameState {

    private UnoDirection direction;
    private Card topCard;
    private int drawCounter;
    private Player actualPlayer;
    private Player nextPlayer;
    private UnoColor actualColor;

    public GameState() {
        // ToDo
    }

    /**
     * initialize GameLogic and Deck
     */
    public void init() {

    }
}
