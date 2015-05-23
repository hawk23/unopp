package com.gamble.unopp.helper;

import com.gamble.unopp.model.cards.Card;

import java.util.Comparator;

/**
 * Created by Mario on 23.05.2015.
 */
public class CardComparator implements Comparator<Card> {


    @Override
    public int compare(Card lhs, Card rhs) {

        if (lhs.getID() < rhs.getID()) {
            return -1;
        }
        else if (lhs.getID() > rhs.getID()) {
            return 1;
        }
        else {
            return 0;
        }
    }
}
