package com.gamble.unopp;

import android.app.ActionBar;
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
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.gamble.unopp.adapter.GameScreenPlayerListAdapter;
import com.gamble.unopp.fragments.ChooseColorDialogFragment;
import com.gamble.unopp.model.Player;
import com.gamble.unopp.model.cards.Card;
import com.gamble.unopp.model.cards.UnoColor;
import com.gamble.unopp.model.game.CardDeck;

import java.util.ArrayList;


public class GameScreenActivity extends ActionBarActivity implements View.OnDragListener, View.OnLongClickListener {

    private LinearLayout llHand;
    private RelativeLayout flPlayedCards;
    private ChooseColorDialogFragment chooseColorDialogFragment = new ChooseColorDialogFragment();
    private ListView lvPlayers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        requestWindowFeature(Window.FEATURE_NO_TITLE);

        super.onCreate(savedInstanceState);

        // remove title
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_game_screen);

        // get views
        this.llHand             = (LinearLayout) findViewById(R.id.llHand);
        this.flPlayedCards      = (RelativeLayout) findViewById(R.id.flPlayedCards);
        this.lvPlayers          = (ListView) findViewById(R.id.lvPlayers);

        CardDeck deck = new CardDeck();

        for (final Card card : deck.getDeck()) {

            final ImageView   imageView = new ImageView(getBaseContext());
            imageView.setImageBitmap(card.getImage());
            imageView.setTag(card);
            imageView.setOnLongClickListener(this);

            this.llHand.addView(imageView);
        }

        flPlayedCards.setOnDragListener(this);

        // HACK display hardcoded players
        ArrayList<Player> players = new ArrayList<Player>();
        players.add(new Player(1, "Peter"));
        players.add(new Player(2, "Verena"));
        players.add(new Player(3, "Albert"));
        players.add(new Player(4, "Julius"));
        players.add(new Player(5, "Roland"));
        players.add(new Player(6, "Mario"));

        ArrayAdapter arrayAdapter = new GameScreenPlayerListAdapter(this.getBaseContext(), players);
        this.lvPlayers.setAdapter(arrayAdapter);
    }

    @Override
    public boolean onLongClick(View v) {
        ImageView card = (ImageView) v;

        ClipData data = ClipData.newPlainText("card", Integer.toString(((Card)card.getTag()).getID()));
        View.DragShadowBuilder shadowBuilder = new View.DragShadowBuilder(card);
        card.startDrag(data, shadowBuilder, card, 0);
        card.setVisibility(View.INVISIBLE);
        return true;
    }

    @Override
    public boolean onDrag(View v, DragEvent event) {
        int action = event.getAction();
        View view = (View) event.getLocalState();
        switch (event.getAction()) {
            case DragEvent.ACTION_DRAG_STARTED:
                // do nothing
                break;
            case DragEvent.ACTION_DRAG_ENTERED:
                v.setBackgroundColor(getResources().getColor(R.color.drag_over));
                break;
            case DragEvent.ACTION_DRAG_EXITED:
                v.setBackgroundColor(Color.TRANSPARENT);
                break;
            case DragEvent.ACTION_DROP:
                // Dropped, reassign View to ViewGroup

                LinearLayout owner = (LinearLayout) view.getParent();
                owner.removeView(view);

                RelativeLayout container = (RelativeLayout) v;
                container.addView(view);
                view.setVisibility(View.VISIBLE);
                RelativeLayout.LayoutParams params =  (RelativeLayout.LayoutParams) view.getLayoutParams();
                params.addRule(RelativeLayout.CENTER_HORIZONTAL);
                params.addRule(RelativeLayout.CENTER_VERTICAL);

                Card draggedCard = (Card) view.getTag();
                if (draggedCard.getColor() == UnoColor.BLACK) {
                    chooseColorDialogFragment.show(getFragmentManager(), "chooseColor");

                    //TODO: update game state
                }
                break;
            case DragEvent.ACTION_DRAG_ENDED:
                v.setBackgroundColor(Color.TRANSPARENT);
                view.setVisibility(View.VISIBLE);
            default:
                break;
        }
        return true;
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
