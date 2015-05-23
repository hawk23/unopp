package com.gamble.unopp.model.management;

import android.graphics.Path;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.shapes.Shape;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.gamble.unopp.GameScreenActivity;
import com.gamble.unopp.R;
import com.gamble.unopp.adapter.GameScreenPlayerListAdapter;
import com.gamble.unopp.model.cards.Card;
import com.gamble.unopp.model.cards.UnoColor;
import com.gamble.unopp.model.game.GameRound;
import com.gamble.unopp.model.game.GameSession;
import com.gamble.unopp.model.game.Player;
import com.gamble.unopp.model.game.UnoDirection;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Mario on 19.05.2015.
 */
public class ViewManager {

    private GameScreenActivity          activity;
    private List<Card>                  cards;
    private ArrayList<Player>           players;
    private ArrayAdapter                arrayAdapter;

    public ViewManager(GameScreenActivity activity) {

        this.activity = activity;
    }

    public void init () {

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

                this.activity.getLlHand().addView(imageView);
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

        UnoColor color = this.getActualGameRound().getGamestate().getActualColor();
        GradientDrawable shape = (GradientDrawable) this.activity.getColorIndicator().getBackground();

        switch (color) {
            case BLACK:
                shape.setColor(this.activity.getResources().getColor(R.color.uno_black));
                break;
            case BLUE:
                shape.setColor(this.activity.getResources().getColor(R.color.uno_blue));
                break;
            case GREEN:
                shape.setColor(this.activity.getResources().getColor(R.color.uno_green));
                break;
            case RED:
                shape.setColor(this.activity.getResources().getColor(R.color.uno_red));
                break;
            case YELLOW:
                shape.setColor(this.activity.getResources().getColor(R.color.uno_yellow));
                break;
        }
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
