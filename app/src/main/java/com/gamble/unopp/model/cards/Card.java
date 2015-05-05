package com.gamble.unopp.model.cards;

import android.graphics.Bitmap;

/**
 * Created by Albert on 02.05.2015.
 */
public abstract class Card {

    protected int ID;
    protected Bitmap image;
    protected UnoColor color;

    public Card (int ID, Bitmap image, UnoColor color) {
        this.ID = ID;
        this.image = image;
        this.color = color;
    }

    public int getID() {
        return ID;
    }

    public Bitmap getImage() {
        return image;
    }

    public UnoColor getColor() {
        return color;
    }
}
