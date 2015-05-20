package com.gamble.unopp.model.game;

import com.gamble.unopp.model.cards.Card;
import com.gamble.unopp.model.cards.UnoColor;
import com.gamble.unopp.model.management.UnoDatabase;

import java.io.Serializable;

/**
 * Created by Albert on 08.05.2015.
 *
 */
public class Turn extends GameUpdate implements Serializable {

    public static enum TurnType implements Serializable {
        DRAW, PLAY_CARD, CHOOSE_COLOR, CALL_UNO
    }

    private Player player;
    private TurnType type;
    private Card card;
    private UnoColor color;

    /**
     *
     * @param type
     * creates a new Turn object and sets the id to localUpdateId and the player to the current local player
     */
    public Turn(TurnType type) {
        super(UnoDatabase.getInstance().getCurrentGameSession().getActualGameRound().getLocalUpdateID()+1);
        this.type = type;
        this.player = UnoDatabase.getInstance().getLocalPlayer();
        this.card = null;
        this.color = null;
    }

    /**
     *
     * @param ID
     * @param type: DRAW, PLAY_CARD, CHOOSE_COLOR, CALL_UNO
     * @param player
     * if TurnType is CHOOSE_COLOR - Color must be set with setColor
     * if TurnType is PLAY_CARD - Card must be set with setCard
     */
    public Turn(int ID, TurnType type, Player player) {
        super(ID);
        this.type = type;
        this.player = player;
        this.card = null;
        this.color = null;
    }

    public int getID() {
        return ID;
    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public TurnType getType() {
        return type;
    }

    public void setType(TurnType type) {
        this.type = type;
    }

    public Card getCard() {
        return card;
    }

    public void setCard(Card card) {
        this.card = card;
    }

    public UnoColor getColor() {
        return color;
    }

    public void setColor(UnoColor color) {
        this.color = color;
    }
}
