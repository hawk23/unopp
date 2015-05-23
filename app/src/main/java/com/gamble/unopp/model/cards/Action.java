package com.gamble.unopp.model.cards;

import java.io.Serializable;

/**
 * Created by Albert on 02.05.2015.
 */
public class Action implements Serializable {

    ActionType actionType;

    public Action (ActionType type) {
        this.actionType = type;
    }

    public ActionType getActionType() {
        return actionType;
    }
}
