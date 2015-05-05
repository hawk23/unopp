package com.gamble.unopp.model.cards;

import android.graphics.Bitmap;

import java.util.ArrayList;

/**
 * Created by Albert on 02.05.2015.
 */
public class ActionCard extends Card {

    private ArrayList<Action> actions;

    public ActionCard(int ID, Bitmap image, UnoColor color) {
        super(ID, image, color);
        this.actions = new ArrayList<Action>();
    }

    public int getActionCount() {
        return this.actions.size();
    }

    public boolean addAction(Action action) {
        if (!(this.actions.contains(action))) {
            this.actions.add(action);
            return true;
        }
        else {
            return false;
        }
    }

    public void setActions(ArrayList<Action> actions) {
        this.actions = actions;
    }
}
