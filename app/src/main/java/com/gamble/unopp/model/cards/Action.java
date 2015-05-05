package com.gamble.unopp.model.cards;

/**
 * Created by Albert on 02.05.2015.
 */
public class Action {

    ActionType actionType;

    public Action (ActionType type) {
        this.actionType = type;
    }

    public ActionType getActionType() {
        return actionType;
    }
}
