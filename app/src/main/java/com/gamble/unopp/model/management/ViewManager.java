package com.gamble.unopp.model.management;

import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.gamble.unopp.GameScreenActivity;
import com.gamble.unopp.model.cards.Card;
import com.gamble.unopp.model.game.GameRound;
import com.gamble.unopp.model.game.GameSession;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Mario on 19.05.2015.
 */
public class ViewManager {

    private GameScreenActivity activity;
    private List<Card> cards;

    public ViewManager(GameScreenActivity activity) {

        this.activity = activity;
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

    public void updateDrawCounter() {

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
    }

    private GameRound getActualGameRound () {
        return UnoDatabase.getInstance().getCurrentGameSession().getActualGameRound();
    }

    private GameSession getCurrentGameSession() {
        return UnoDatabase.getInstance().getCurrentGameSession();
    }
}
