package com.gamble.unopp.model.cards;

/**
 * Created by Albert on 05.05.2015.
 */
public enum UnoColor {

    RED     (1),
    YELLOW  (2),
    GREEN   (3),
    BLUE    (4),
    BLACK   (5);

    private final int value;

    private UnoColor(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public static UnoColor createById (int id) {
        for (UnoColor value : values() )
        {
            if (value.getValue() == id) {
                return value;
            }
        }

        return null;
    }
}
