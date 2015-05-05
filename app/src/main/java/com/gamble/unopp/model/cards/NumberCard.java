package com.gamble.unopp.model.cards;

import android.graphics.Bitmap;

/**
 * Created by Albert on 02.05.2015.
 */
public class NumberCard extends Card {

    private int value;

    public NumberCard(int ID, int value, Bitmap image, UnoColor color) {
        super(ID, image, color);
        this.value = value;
    }
}
