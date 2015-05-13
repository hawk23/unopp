package com.gamble.unopp.model.game;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.gamble.unopp.GameSettings;
import com.gamble.unopp.MainActivity;
import com.gamble.unopp.model.cards.Action;
import com.gamble.unopp.model.cards.ActionCard;
import com.gamble.unopp.model.cards.ActionType;
import com.gamble.unopp.model.cards.Card;
import com.gamble.unopp.model.cards.NumberCard;
import com.gamble.unopp.model.cards.UnoColor;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

/**
 * Created by Albert on 05.05.2015.
 */
public class CardDeck {

    private static final String DECK_SPRITESHEET_PATH = "cards.png";
    private static final int CARD_WIDTH = 192;
    private static final int CARD_HEIGHT = 288;
    private static final int HORIZONTAL_COUNT = 14;
    private static final int VERTICAL_COUNT = 8;
    private static final int DECK_WIDTH = CARD_WIDTH * HORIZONTAL_COUNT;
    private static final int DECK_HEIGHT = CARD_HEIGHT * VERTICAL_COUNT;

    private int id = 0;

    private ArrayList<Card> deck;

    public CardDeck() {
        this.deck = new ArrayList<Card>();
        initDeck();

        // console output just for testing
        for (Card c : deck) {
            System.out.print(c.getID() + "/");
            if (c instanceof NumberCard) {
                System.out.print(((NumberCard) c).getValue() + "/");
                System.out.println(c.getColor());
                System.out.println("---------------------");
            }
            else {
                if (c instanceof ActionCard) {
                    for (Action a : ((ActionCard) c).getActions()) {
                        System.out.print(a.getActionType() + "/");
                    }
                    System.out.println(c.getColor());
                    System.out.println("---------------------");
                }

            }
        }
    }

    private void initDeck() {
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
                    this.deck.add(card);
                }
                else if (j == 10) {
                    Card card1 = new ActionCard(getID(), image, color);
                    ((ActionCard)card1).addAction(new Action(new ActionType(ActionType.SKIP_TURN)));
                    Card card2 = new ActionCard(getID(), image, color);
                    ((ActionCard)card2).addAction(new Action(new ActionType(ActionType.SKIP_TURN)));
                    this.deck.add(card1);
                    this.deck.add(card2);
                }
                else if (j == 11) {
                    Card card1 = new ActionCard(getID(), image, color);
                    ((ActionCard)card1).addAction(new Action(new ActionType(ActionType.CHANGE_DIRECTION)));
                    Card card2 = new ActionCard(getID(), image, color);
                    ((ActionCard)card2).addAction(new Action(new ActionType(ActionType.CHANGE_DIRECTION)));
                    this.deck.add(card1);
                    this.deck.add(card2);
                }
                else if (j == 12) {
                    Card card1 = new ActionCard(getID(), image, color);
                    ((ActionCard)card1).addAction(new Action(new ActionType(ActionType.ADD2)));
                    Card card2 = new ActionCard(getID(), image, color);
                    ((ActionCard)card2).addAction(new Action(new ActionType(ActionType.ADD2)));
                    this.deck.add(card1);
                    this.deck.add(card2);
                }
                else {
                    Card card1 = new NumberCard(getID(), j, image, color);
                    Card card2 = new NumberCard(getID(), j, image, color);
                    this.deck.add(card1);
                    this.deck.add(card2);
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
        this.deck.add(card1);
        this.deck.add(card2);
        this.deck.add(card3);
        this.deck.add(card4);

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
        this.deck.add(card5);
        this.deck.add(card6);
        this.deck.add(card7);
        this.deck.add(card8);
    }

    private int getID() {
        int id = this.id;
        this.id++;
        return id;
    }

    public ArrayList<Card> getDeck() {
        return deck;
    }
}
