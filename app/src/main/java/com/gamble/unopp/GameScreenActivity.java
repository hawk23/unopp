package com.gamble.unopp;

import android.content.ClipData;
import android.graphics.Color;
import android.graphics.Path;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.DragEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gamble.unopp.adapter.GameScreenPlayerListAdapter;
import com.gamble.unopp.fragments.ChooseColorDialogFragment;
import com.gamble.unopp.model.game.DeckGenerator;;
import com.gamble.unopp.model.game.Player;
import com.gamble.unopp.model.cards.Card;
import com.gamble.unopp.model.cards.UnoColor;
import com.gamble.unopp.model.management.UnoDatabase;

import java.util.ArrayList;
import java.util.List;


public class GameScreenActivity extends ActionBarActivity implements View.OnDragListener, View.OnLongClickListener, View.OnClickListener, View.OnLayoutChangeListener {

    private HorizontalScrollView hswHand;
    private LinearLayout llHand;
    private TextView tvDrawCounter;
    private RelativeLayout flUnplayedCards;
    private RelativeLayout flPlayedCards;
    private ChooseColorDialogFragment chooseColorDialogFragment = new ChooseColorDialogFragment();
    private ImageView ivDirection;
    private ListView lvPlayers;
    private ArrayList<Player> players;
    private ArrayAdapter arrayAdapter;
    private List<Card> cards;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        requestWindowFeature(Window.FEATURE_NO_TITLE);

        super.onCreate(savedInstanceState);

        // remove title
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_game_screen);

        // get views
        this.hswHand            = (HorizontalScrollView) findViewById(R.id.hswHand);
        this.llHand             = (LinearLayout) findViewById(R.id.llHand);
        this.tvDrawCounter      = (TextView) findViewById(R.id.tvDrawCounter);
        this.flUnplayedCards    = (RelativeLayout) findViewById(R.id.flUnplayedCards);
        this.flPlayedCards      = (RelativeLayout) findViewById(R.id.flPlayedCards);
        this.lvPlayers          = (ListView) findViewById(R.id.lvPlayers);
        this.ivDirection        = (ImageView) findViewById(R.id.ivDirection);

        // initialize direction
        setDirection(Path.Direction.CW);

        // set on click linstener on unplayed cards for drawing cards
        flUnplayedCards.setOnClickListener(this);

        // initialize draw counter
        tvDrawCounter.setText("");

        // disable the back button for the color popup
        chooseColorDialogFragment.setCancelable(false);

        // HACK: set cards of player here
        // cards = UnoDatabase.getInstance().getLocalPlayer().getHand();
        cards = DeckGenerator.createDeck();

        for (final Card card : cards) {

            final ImageView   imageView = new ImageView(getBaseContext());
            imageView.setImageBitmap(card.getImage());
            imageView.setTag(card);
            imageView.setOnLongClickListener(this);

            this.llHand.addView(imageView);
        }

        flPlayedCards.setOnDragListener(this);

        // HACK display hardcoded players
        players = new ArrayList<>();
        players.add(new Player(1, "Peter"));
        players.add(new Player(2, "Verena"));
        players.add(new Player(3, "Albert"));
        players.add(new Player(4, "Julius"));
        players.add(new Player(5, "Roland"));
        players.add(new Player(UnoDatabase.getInstance().getLocalPlayer().getID(), "Mario"));

        arrayAdapter = new GameScreenPlayerListAdapter(this.getBaseContext(), players);
        this.lvPlayers.setAdapter(arrayAdapter);

    }

    private void setDirection(Path.Direction direction) {
        if (direction.equals(Path.Direction.CW)) {
            ivDirection.setImageResource(R.mipmap.direction_down);
        } else if (direction.equals(Path.Direction.CCW)) {
            ivDirection.setImageResource(R.mipmap.direction_up);
        }
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

        // HACK : check if card can be played here
        boolean cardPlayable = true;

        switch (event.getAction()) {
            case DragEvent.ACTION_DRAG_STARTED:
                // do nothing
                break;
            case DragEvent.ACTION_DRAG_ENTERED:
                if (cardPlayable) {
                    v.setBackgroundColor(getResources().getColor(R.color.green));
                } else {
                    v.setBackgroundColor(getResources().getColor(R.color.red));
                }
                break;
            case DragEvent.ACTION_DRAG_EXITED:
                v.setBackgroundColor(Color.TRANSPARENT);
                break;
            case DragEvent.ACTION_DROP:
                // Dropped, reassign View to ViewGroup

                if (cardPlayable) {
                    LinearLayout owner = (LinearLayout) view.getParent();
                    owner.removeView(view);

                    RelativeLayout container = (RelativeLayout) v;
                    view.setLongClickable(false);
                    container.addView(view);
                    view.setVisibility(View.VISIBLE);
                    RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) view.getLayoutParams();
                    params.addRule(RelativeLayout.CENTER_HORIZONTAL);
                    params.addRule(RelativeLayout.CENTER_VERTICAL);

                    Card draggedCard = (Card) view.getTag();
                    if (draggedCard.getColor() == UnoColor.BLACK) {
                        chooseColorDialogFragment.show(getFragmentManager(), "chooseColor");
                    }
                    //TODO: update game state
                    //TODO: update draw counter
                    //TODO: update color chosen

                    //HACK
                    setDrawCounter(4);
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

    public void callUno(View view) {
        /*
        Turn turn = new Turn(Turn.TurnType.CALL_UNO);
        UnoDatabase.getInstance().getCurrentGameSession().getActualGameRound().doTurn(turn);
        */
    }

    public void showUnoCall() {
        // notify listAdapter that players changed
        arrayAdapter.notifyDataSetChanged();
    }

    public void showColorCall(UnoColor color, Player player) {
        // notify listAdapter that players changed
        arrayAdapter.notifyDataSetChanged();
    }

    @Override
    public void onClick(View view) {
        Player self = UnoDatabase.getInstance().getLocalPlayer();

        // HACK draw card - create Turn Object
        List<Card> cardsDrawn = new ArrayList<>();
        cardsDrawn.add(cards.get(0));
        cardsDrawn.add(cards.get(1));
        cardsDrawn.add(cards.get(2));
        cardsDrawn.add(cards.get(3));
        // END HACK
        /*
        GameRound gameRound = self.getGameSession().getActualGameRound();
        int updateId = gameRound.getLocalUpdateID();
        int numberCardsOld = self.getHandCount();

        Turn turn = new Turn(updateId, Turn.TurnType.DRAW, self);
        if (gameRound.doTurn() == true) {
            cardsDrawn =
        } else {
            // TODO update view to show that drawing is not possible
        }
        int numberCardsNew = self.getHandCount();
        int drawnCards = numberCardsNew - numberCardsOld;

        for (int i = 0; i < drawnCards; i++) {
            cardsDrawn.add(self.getHand().get(numberCardsNew - drawnCards + i));
        }
        */

        for (Card card : cardsDrawn) {
            addCardToHand(card);
        }

        // add listener in order to scroll the hand to last card
        hswHand.addOnLayoutChangeListener(this);
    }

    private void addCardToHand(Card card) {
        final ImageView   imageView = new ImageView(getBaseContext());
        imageView.setImageBitmap(card.getImage());
        imageView.setTag(card);
        imageView.setOnLongClickListener(this);
        this.llHand.addView(imageView);
    }

    @Override
    public void onLayoutChange(View view, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
        hswHand.removeOnLayoutChangeListener(this);
        hswHand.fullScroll(View.FOCUS_RIGHT);
    }

    public void setDrawCounter(int drawCounter) {
        if (drawCounter == 0 ) {
            tvDrawCounter.setText("");
        } else if (drawCounter > 0) {
            tvDrawCounter.setText("+" + drawCounter);
        }
    }
}
