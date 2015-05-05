package com.gamble.unopp.model.cards;

/**
 * Created by Albert on 02.05.2015.
 */
public class ActionType {

    public static final int ADD2 = 0;
    public static final int ADD4 = 1;
    public static final int CHANGE_DIRECTION = 2;
    public static final int SKIP_TURN = 3;
    public static final int CHANGE_COLOR = 4;

    private int ID;
    private int type;

    // needed?
    private String name;
    private String description;

    public ActionType (int type) {
        this.type = type;
    }

    public int getType() {
        return type;
    }
}
