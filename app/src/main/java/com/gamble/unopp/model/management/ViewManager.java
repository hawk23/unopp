package com.gamble.unopp.model.management;

import android.graphics.Path;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.shapes.Shape;
import android.view.Display;
import android.view.Gravity;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.gamble.unopp.GameScreenActivity;
import com.gamble.unopp.R;
import com.gamble.unopp.adapter.GameScreenPlayerListAdapter;
import com.gamble.unopp.model.cards.Action;
import com.gamble.unopp.model.cards.ActionCard;
import com.gamble.unopp.model.cards.ActionType;
import com.gamble.unopp.model.cards.Card;
import com.gamble.unopp.model.cards.UnoColor;
import com.gamble.unopp.model.game.GameRound;
import com.gamble.unopp.model.game.GameSession;
import com.gamble.unopp.model.game.Player;
import com.gamble.unopp.model.game.UnoDirection;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by Mario on 19.05.2015.
 */
public class ViewManager {

    private GameScreenActivity          activity;
    private List<Card>                  cards;
    private ArrayList<Player>           players;
    private ArrayAdapter                arrayAdapter;

    private float                       screenWidth;
    private float                       screenHeight;

    public ViewManager(GameScreenActivity activity) {

        this.activity = activity;
    }

    public void init () {

        // get screen size
        Display display = this.activity.getWindowManager().getDefaultDisplay();
        Point screenSize = new Point();
        display.getSize(screenSize);
        screenWidth = screenSize.x;
        screenHeight = screenSize.y;

    }

    /**
     * Updates the view based on the state of the given game round
     */
    public void updateView () {

        // Display played stack
        this.updatePlayedStack(this.getActualGameRound());

        // Display hand of player
        this.updateHand();

        // update displayed draw counter
        this.updateDrawCounter();

        // update list of players
        this.updatePlayersList();

        // update direction icon
        this.updateDirection();

        // update display of uno symbol
        this.updateUno();

        // update color shown by color indicator
        this.updateColorIndicator();
    }

    private void updatePlayedStack (GameRound gameRound) {

        ArrayList<Card> playedStack = gameRound.getGamestate().getPlayedStack();
        Card            topCard     = playedStack.get(playedStack.size()-1);

        ImageView imageView = new ImageView(this.activity.getBaseContext());
        imageView.setImageBitmap(topCard.getImage());
        imageView.setTag(topCard);
        imageView.setBackgroundResource(R.drawable.color_indicator_shape);
        imageView.setPadding(10,10,10,10);

        this.activity.getFlPlayedCards().removeAllViews();
        this.activity.getFlPlayedCards().addView(imageView);

        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) imageView.getLayoutParams();
        params.addRule(RelativeLayout.CENTER_HORIZONTAL);
        params.addRule(RelativeLayout.CENTER_VERTICAL);
    }

    private void updateDrawCounter() {

        int drawCounter = this.getActualGameRound().getGamestate().getDrawCounter();

        if (drawCounter == 0 ) {
            this.activity.getTvDrawCounter().setText("");
        } else if (drawCounter > 0) {
            this.activity.getTvDrawCounter().setText("+" + drawCounter);
        }
    }

    private void updateHand () {

        cards = UnoDatabase.getInstance().getLocalPlayer().getHand();

        if (cards != null) {
            this.activity.getLlHand().removeAllViews();

            for (final Card card : cards) {

                final ImageView imageView = new ImageView(this.activity.getBaseContext());
                imageView.setImageBitmap(card.getImage());
                imageView.setTag(card);
                imageView.setOnLongClickListener(this.activity);
                imageView.setAdjustViewBounds(true);

                this.activity.getLlHand().addView(imageView);

                LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) imageView.getLayoutParams();
                params.setMargins(0,0,0,0);

                this.activity.getLlHand().setDividerPadding(0);
                this.activity.getLlHand().setPadding(0,0,0,0);
                this.activity.getLlHand().setShowDividers(LinearLayout.SHOW_DIVIDER_NONE);
            }
        }

        // TODO: animate new cards: scroll to new cards and let them flash/glow for a while or something.
    }

    private void updatePlayersList () {

        // initialize the players list
        this.players = this.getCurrentGameSession().getPlayers();
        arrayAdapter = new GameScreenPlayerListAdapter(this.activity.getBaseContext(), players);
        this.activity.getLvPlayers().setAdapter(arrayAdapter);
    }

    private void updateDirection () {

        UnoDirection direction =  this.getActualGameRound().getGamestate().getDirection();

        switch (direction) {
            case COUNTERCLOCKWISE:
                this.activity.getIvDirection().setImageResource(R.mipmap.direction_up);
                break;
            case CLOCKWISE:
                activity.getIvDirection().setImageResource(R.mipmap.direction_down);
                break;
        }
    }

    private void updateUno () {
        // notify listAdapter that players changed
        arrayAdapter.notifyDataSetChanged();
    }

    private void updateColorIndicator () {

        UnoColor            color       = this.getActualGameRound().getGamestate().getActualColor();
        Card                card        = this.getActualGameRound().getGamestate().getTopCard();
        GradientDrawable    shape       = (GradientDrawable) this.activity.getFlPlayedCards().getChildAt(0).getBackground();
        Boolean             showColor   = false;

        // only show color indicator when black card is on top
        if (card instanceof ActionCard) {
            ActionCard acard = (ActionCard) card;

            for (Action action : acard.getActions()) {
                if (action.getActionType().getType() == ActionType.CHANGE_COLOR) {

                    showColor = true;
                    break;
                }
            }
        }

        if (showColor && !UnoDatabase.getInstance().getLocalPlayer().hasToChooseColor()) {
            switch (color) {
                case BLACK:
                    shape.setStroke(10, this.activity.getResources().getColor(R.color.uno_black));
                    break;
                case BLUE:
                    shape.setStroke(10, this.activity.getResources().getColor(R.color.uno_blue));
                    break;
                case GREEN:
                    shape.setStroke(10, this.activity.getResources().getColor(R.color.uno_green));
                    break;
                case RED:
                    shape.setStroke(10, this.activity.getResources().getColor(R.color.uno_red));
                    break;
                case YELLOW:
                    shape.setStroke(10, this.activity.getResources().getColor(R.color.uno_yellow));
                    break;
            }
        }
        else {
            shape.setStroke(10, this.activity.getResources().getColor(R.color.transparent));
        }
    }

    public void drawCheatFigure() {

        // get random position
        Random random = new Random();
        int randomWidth = (int) (random.nextFloat() * screenWidth);
        int randomHeight = (int) (random.nextFloat() * screenHeight);

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        params.gravity = Gravity.RIGHT | Gravity.TOP;
        ImageView ivCheatFigure = new ImageView(this.activity.getBaseContext());
        ivCheatFigure.setImageResource(R.mipmap.cheat_figure);
        ivCheatFigure.setPadding(randomWidth, 0, 0, randomHeight);
        ivCheatFigure.setOnClickListener(this.activity);
        ivCheatFigure.setLayoutParams(params);
        this.activity.setIvCheatFigure(ivCheatFigure);
        this.activity.getRlScreen().addView(this.activity.getIvCheatFigure());
        this.activity.setCheatFigureClicked(false);

        this.activity.startCheatFigureShownTimer();

    }

    /**
     * Getter and Setter
     */

    private GameRound getActualGameRound () {
        return UnoDatabase.getInstance().getCurrentGameSession().getActualGameRound();
    }

    private GameSession getCurrentGameSession() {
        return UnoDatabase.getInstance().getCurrentGameSession();
    }
}
