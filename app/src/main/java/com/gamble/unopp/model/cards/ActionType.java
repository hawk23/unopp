package com.gamble.unopp.model.cards;

/**
 * Created by Albert on 02.05.2015.
 */
public class ActionType {

    public static enum TYPE {
        ADD_2,
        ADD_4,
        CHANGE_DIRECTION,
        SKIP_TURN,
        CHANGE_COLOR
    }

    private int ID;
    private String name;
    private String description;

}
