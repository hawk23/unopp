package com.gamble.unopp.model.game;

/**
 * Created by Albert on 05.05.2015.
 */
public enum UnoDirection {

    CLOCKWISE (1),
    COUNTERCLOCKWISE (-1);

    private final int direction;

    private UnoDirection (int id) {
        this.direction = id;
    }

    public int getValue() {
        return direction;
    }

    public static UnoDirection createById (int id) {
        for (UnoDirection value : values() )
        {
            if (value.getValue() == id) {
                return value;
            }
        }

        return null;
    }
}
