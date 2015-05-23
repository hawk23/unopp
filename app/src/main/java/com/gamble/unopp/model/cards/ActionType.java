package com.gamble.unopp.model.cards;

import java.io.Serializable;

/**
 * Created by Albert on 02.05.2015.
 */
public class ActionType implements Serializable {

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

    public String toString() {
        String s = "";
        switch (this.type) {
            case ADD2: s = "ADD2"; break;
            case ADD4: s = "ADD4"; break;
            case CHANGE_DIRECTION: s = "CHANGE_DIRECTION"; break;
            case SKIP_TURN: s = "SKIP_TURN"; break;
            case CHANGE_COLOR: s = "CHANGE_COLOR"; break;
        }

        return s;
    }
}
