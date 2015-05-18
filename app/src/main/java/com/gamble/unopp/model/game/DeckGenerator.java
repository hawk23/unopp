package com.gamble.unopp.model.game;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.gamble.unopp.GameSettings;
import com.gamble.unopp.model.cards.Action;
import com.gamble.unopp.model.cards.ActionCard;
import com.gamble.unopp.model.cards.ActionType;
import com.gamble.unopp.model.cards.Card;
import com.gamble.unopp.model.cards.NumberCard;
import com.gamble.unopp.model.cards.UnoColor;

import java.io.InputStream;
import java.util.ArrayList;

/**
 * Created by Albert on 15.05.2015.
 */
public class DeckGenerator {

    private static final String DECK_SPRITESHEET_PATH = "cards.png";
    private static final int CARD_WIDTH = 192;
    private static final int CARD_HEIGHT = 288;
    private static final int HORIZONTAL_COUNT = 14;
    private static final int VERTICAL_COUNT = 8;
    private static final int DECK_WIDTH = CARD_WIDTH * HORIZONTAL_COUNT;
    private static final int DECK_HEIGHT = CARD_HEIGHT * VERTICAL_COUNT;

    private static int id;

    public static synchronized ArrayList<Card> createDeck(int startingID) {

        id = startingID;

        ArrayList<Card> deck = new ArrayList<Card>();
        Bitmap deckBitmap = null;

        try {
            InputStream is  = GameSettings.ASSET_MANAGER.open (DECK_SPRITESHEET_PATH);
            deckBitmap      = BitmapFactory.decodeStream(is);
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }

        // init red, yellow, green and blue cards
        for (int i = 0; i < 4; i++) {
            UnoColor color = null;
            if (i == 0) {
                color = UnoColor.RED;
            }
            else if (i == 1) {
                color = UnoColor.YELLOW;
            }
            else if (i == 2) {
                color = UnoColor.GREEN;
            }
            else if (i == 3) {
                color = UnoColor.BLUE;
            }

            for (int j = 0; j < HORIZONTAL_COUNT - 1; j++) {
                Bitmap image = Bitmap.createBitmap(deckBitmap, j * CARD_WIDTH, i * CARD_HEIGHT, CARD_WIDTH, CARD_HEIGHT);

                if (j == 0) {
                    Card card = new NumberCard(getID(), j, image, color);
                    deck.add(card);
                }
                else if (j == 10) {
                    Card card1 = new ActionCard(getID(), image, color);
                    ((ActionCard)card1).addAction(new Action(new ActionType(ActionType.SKIP_TURN)));
                    Card card2 = new ActionCard(getID(), image, color);
                    ((ActionCard)card2).addAction(new Action(new ActionType(ActionType.SKIP_TURN)));
                    deck.add(card1);
                    deck.add(card2);
                }
                else if (j == 11) {
                    Card card1 = new ActionCard(getID(), image, color);
                    ((ActionCard)card1).addAction(new Action(new ActionType(ActionType.CHANGE_DIRECTION)));
                    Card card2 = new ActionCard(getID(), image, color);
                    ((ActionCard)card2).addAction(new Action(new ActionType(ActionType.CHANGE_DIRECTION)));
                    deck.add(card1);
                    deck.add(card2);
                }
                else if (j == 12) {
                    Card card1 = new ActionCard(getID(), image, color);
                    ((ActionCard)card1).addAction(new Action(new ActionType(ActionType.ADD2)));
                    Card card2 = new ActionCard(getID(), image, color);
                    ((ActionCard)card2).addAction(new Action(new ActionType(ActionType.ADD2)));
                    deck.add(card1);
                    deck.add(card2);
                }
                else {
                    Card card1 = new NumberCard(getID(), j, image, color);
                    Card card2 = new NumberCard(getID(), j, image, color);
                    deck.add(card1);
                    deck.add(card2);
                }
            }
        }

        // init black cards
        UnoColor color = UnoColor.BLACK;

        Bitmap imageColorChange = Bitmap.createBitmap(deckBitmap, 13 * CARD_WIDTH, 0, CARD_WIDTH, CARD_HEIGHT);

        Card card1 = new ActionCard(getID(), imageColorChange, color);
        ((ActionCard)card1).addAction(new Action(new ActionType(ActionType.CHANGE_COLOR)));
        Card card2 = new ActionCard(getID(), imageColorChange, color);
        ((ActionCard)card2).addAction(new Action(new ActionType(ActionType.CHANGE_COLOR)));
        Card card3 = new ActionCard(getID(), imageColorChange, color);
        ((ActionCard)card3).addAction(new Action(new ActionType(ActionType.CHANGE_COLOR)));
        Card card4 = new ActionCard(getID(), imageColorChange, color);
        ((ActionCard)card4).addAction(new Action(new ActionType(ActionType.CHANGE_COLOR)));
        deck.add(card1);
        deck.add(card2);
        deck.add(card3);
        deck.add(card4);

        Bitmap imageADD4 = Bitmap.createBitmap(deckBitmap, 13 * CARD_WIDTH, 4 * CARD_HEIGHT, CARD_WIDTH, CARD_HEIGHT);

        Card card5 = new ActionCard(getID(), imageADD4, color);
        ((ActionCard)card5).addAction(new Action(new ActionType(ActionType.CHANGE_COLOR)));
        ((ActionCard)card5).addAction(new Action(new ActionType(ActionType.ADD4)));
        Card card6 = new ActionCard(getID(), imageADD4, color);
        ((ActionCard)card6).addAction(new Action(new ActionType(ActionType.CHANGE_COLOR)));
        ((ActionCard)card6).addAction(new Action(new ActionType(ActionType.ADD4)));
        Card card7 = new ActionCard(getID(), imageADD4, color);
        ((ActionCard)card7).addAction(new Action(new ActionType(ActionType.CHANGE_COLOR)));
        ((ActionCard)card7).addAction(new Action(new ActionType(ActionType.ADD4)));
        Card card8 = new ActionCard(getID(), imageADD4, color);
        ((ActionCard)card8).addAction(new Action(new ActionType(ActionType.CHANGE_COLOR)));
        ((ActionCard)card8).addAction(new Action(new ActionType(ActionType.ADD4)));
        deck.add(card5);
        deck.add(card6);
        deck.add(card7);
        deck.add(card8);

        return deck;
    }

    private static int getID() {
        int cardId = id;
        id++;
        return cardId;
    }

}
