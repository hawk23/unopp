package com.gamble.unopp;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.gamble.unopp.model.cards.Card;
import com.gamble.unopp.model.game.CardDeck;


public class GameScreenActivity extends ActionBarActivity {

    private LinearLayout llHand;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_screen);


        CardDeck deck = new CardDeck();

        for (Card card : deck.getDeck()) {

            this.llHand = (LinearLayout) findViewById(R.id.llHand);

            ImageView   imageView = new ImageView(getBaseContext());
            imageView.setImageBitmap(card.getImage());

            this.llHand.addView(imageView);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_game_screen, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
