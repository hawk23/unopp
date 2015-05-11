package com.gamble.unopp.model.game;

import com.gamble.unopp.model.Player;
import com.gamble.unopp.model.cards.Card;
import com.gamble.unopp.model.cards.UnoColor;

/**
 * Created by Albert on 08.05.2015.
 *
 * ToDo
 * generate ID
 */
public class Turn {

    public static final int DRAW = 0;
    public static final int PLAY_CARD = 1;
    public static final int NEXT = 2;
    public static final int CHOOSE_COLOR = 3;
    public static final int CALL_UNO = 4;

    private int ID;

    private Player player;
    private int type;
    private Card card;
    private UnoColor color;

    /**
     * constructor
     * @param type
     * @param player
     */
    public Turn (int type, Player player) {
        this.type = type;
        this.player = player;
        this.color = null;
        this.card = null;
    }

    /**
     *
     * @param type
     * @param player
     * @param card if type is PLAY_CARD
     */
    public Turn (int type, Player player, Card card) {
        this.type = type;
        this.player = player;
        this.card = card;
        this.color = null;
    }

    /**
     *
     * @param type
     * @param player
     * @param color if type is CHOOSE_COLOR
     */
    public Turn (int type, Player player, UnoColor color) {
        this.type = type;
        this.player = player;
        this.color = color;
        this.card = null;
    }

    public UnoColor getColor() {
        return color;
    }

    public void setColor(UnoColor color) {
        this.color = color;
    }

    public Player getPlayer() {
        return player;
    }

    public int getType() {
        return type;
    }

    public Card getCard() {
        return card;
    }

    public void setCard(Card card) {
        this.card = card;
    }
}
