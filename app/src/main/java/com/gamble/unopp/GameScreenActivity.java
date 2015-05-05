package com.gamble.unopp;

import android.content.ClipData;
import android.graphics.Color;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.DragEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.MimeTypeMap;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.gamble.unopp.listener.CardLongClickListener;
import com.gamble.unopp.listener.PlayedStackOnDragListener;
import com.gamble.unopp.model.cards.Card;
import com.gamble.unopp.model.game.CardDeck;


public class GameScreenActivity extends ActionBarActivity {

    private LinearLayout llHand;
    private FrameLayout flPlayedCards;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        requestWindowFeature(Window.FEATURE_NO_TITLE);

        super.onCreate(savedInstanceState);

        // remove title
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_game_screen);

        // get views
        this.llHand             = (LinearLayout) findViewById(R.id.llHand);
        this.flPlayedCards      = (FrameLayout) findViewById(R.id.flPlayedCards);

        CardDeck deck = new CardDeck();

        for (final Card card : deck.getDeck()) {

            final ImageView   imageView = new ImageView(getBaseContext());
            imageView.setImageBitmap(card.getImage());
            imageView.setTag(card);
            imageView.setOnLongClickListener(new CardLongClickListener(imageView));

            this.llHand.addView(imageView);
        }

        flPlayedCards.setOnDragListener(new PlayedStackOnDragListener(getResources().getColor(R.color.drag_over)));
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
