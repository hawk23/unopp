package com.gamble.unopp;

import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Vibrator;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.FloatMath;
import android.view.DragEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gamble.unopp.connection.RequestProcessor;
import com.gamble.unopp.connection.RequestProcessorCallback;
import com.gamble.unopp.connection.requests.DestroyGameRequest;
import com.gamble.unopp.connection.response.DestroyGameResponse;
import com.gamble.unopp.connection.response.Response;
import com.gamble.unopp.fragments.ChooseColorDialogFragment;
import com.gamble.unopp.model.game.GameRound;
import com.gamble.unopp.model.game.GameSession;
import com.gamble.unopp.model.game.GameState;
import com.gamble.unopp.model.game.Player;
import com.gamble.unopp.model.cards.Card;
import com.gamble.unopp.model.game.Turn;
import com.gamble.unopp.model.management.IChooseColorDialogListener;
import com.gamble.unopp.model.management.UnoDatabase;
import com.gamble.unopp.model.management.ViewManager;


public class GameScreenActivity extends ActionBarActivity implements View.OnDragListener, View.OnLongClickListener, View.OnClickListener, View.OnLayoutChangeListener, SensorEventListener, RequestProcessorCallback, IChooseColorDialogListener {

    private HorizontalScrollView        hswHand;
    private LinearLayout                llHand;
    private TextView                    tvDrawCounter;
    private RelativeLayout              flUnplayedCards;
    private RelativeLayout              flPlayedCards;
    private ChooseColorDialogFragment   chooseColorDialogFragment;
    private ImageView                   ivDirection;
    private ListView                    lvPlayers;
    private RelativeLayout              colorIndicator;

    private ViewManager                 viewManager;

    private SensorManager               sensorMan;
    private Sensor                      accelerometer;
    private float[]                     mGravity;
    private float                       mAccel;
    private float                       mAccelCurrent;
    private float                       mAccelLast;
    private Vibrator                    vibrator;

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
        this.colorIndicator     = (RelativeLayout) findViewById(R.id.colorIndicator);

        // init sensors
        sensorMan               = (SensorManager)getSystemService(SENSOR_SERVICE);
        accelerometer           = sensorMan.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        mAccel                  = 0.00f;
        mAccelCurrent           = SensorManager.GRAVITY_EARTH;
        mAccelLast              = SensorManager.GRAVITY_EARTH;

        // init vibrator
        this.vibrator           = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);

        // init view manager
        this.viewManager        = new ViewManager(this);
        this.viewManager.init();
        this.viewManager.updateView();

        this.initGameView();
    }

    private void initGameView () {

        // set on click linstener on unplayed cards for drawing cards
        flUnplayedCards.setOnClickListener(this);

        // disable the back button for the color popup
        chooseColorDialogFragment   = new ChooseColorDialogFragment();
        chooseColorDialogFragment.setCancelable(false);

        // init dragging
        flPlayedCards.setOnDragListener(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        sensorMan.registerListener(this, accelerometer,SensorManager.SENSOR_DELAY_UI);
    }

    @Override
    protected void onPause() {
        super.onPause();
        sensorMan.unregisterListener(this);
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

    private Turn createPlayCardTurn (Card card) {

        Turn turn = new Turn(Turn.TurnType.PLAY_CARD);
        turn.setCard(card);

        return turn;
    }

    private void playCard (Card card) {

        Turn turn = createPlayCardTurn(card);

        this.playCard(turn);
    }

    private void playCard (Turn turn) {

        // TODO: send Turn to other players

        this.getActualGameRound().doTurn(turn);
        this.viewManager.updateView();

        // check if color has to be chosen
        if (this.getLocalPlayer().hasToChooseColor()) {
            chooseColorDialogFragment.show(getFragmentManager(), "chooseColor");
        }
    }

    @Override
    public boolean onDrag (View v, DragEvent event) {

        if (this.isMyTurn()) {

            int     action          = event.getAction();
            View    view            = (View) event.getLocalState();
            Card    draggedCard     = (Card) view.getTag();

            Turn playCardTurn       = this.createPlayCardTurn(draggedCard);
            boolean cardPlayable    = this.getActualGameRound().checkTurn(playCardTurn);

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
                    if (cardPlayable) {
                        this.playCard(playCardTurn);
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
        else {
            return false;
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

        // Handle item selection
        switch (id) {
            case R.id.action_endGameRound:
                this.endGameRound();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void callUno(View view) {
        callUno();
    }

    public void callUno () {

        if (this.getCurrentGameSession() != null &&
            this.getActualGameRound() != null) {

            Turn turn = new Turn(Turn.TurnType.CALL_UNO);
            this.getActualGameRound().doTurn(turn);

            // TODO: send to other players if call uno successful.

            this.viewManager.updateView();
        }
    }

    @Override
    public void onClick(View view) {

        // player wants to draw from stack
        if (view == this.flUnplayedCards) {
            if (this.isMyTurn()) {
                // TODO
            }

        }

        /*
        Player self = UnoDatabase.getInstance().getLocalPlayer();

        // HACK draw card - create Turn Object
        List<Card> cardsDrawn = new ArrayList<>();
        cardsDrawn.add(cards.get(0));
        cardsDrawn.add(cards.get(1));
        cardsDrawn.add(cards.get(2));
        cardsDrawn.add(cards.get(3));
        // END HACK
        */

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

        /*
        for (Card card : cardsDrawn) {
            addCardToHand(card);
        }

        // add listener in order to scroll the hand to last card
        hswHand.addOnLayoutChangeListener(this);
        */
    }

    // TODO: obsolete? --> Viewmanager should do that.
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

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER){

            mGravity = event.values.clone();

            // Shake detection
            float x = mGravity[0];
            float y = mGravity[1];
            float z = mGravity[2];

            mAccelLast = mAccelCurrent;
            mAccelCurrent = FloatMath.sqrt(x * x + y * y + z * z);
            float delta = mAccelCurrent - mAccelLast;
            mAccel = mAccel * 0.9f + delta;

            // Make this higher or lower according to how much
            // motion you want to detect
            if(mAccel > 8){
                this.callUno();
                this.vibrator.vibrate(500);
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        // required method
    }

    @Override
    public void requestFinished(Response response) {

        if (response instanceof DestroyGameResponse) {

            DestroyGameResponse destroyGameResponse = (DestroyGameResponse) response;

            // delete current gameSession
            UnoDatabase.getInstance().setCurrentGameSession(null);

            // create an Intent to take you back to the LobbyActivity
            Intent intent = new Intent(this, LobbyActivity.class);
            startActivity(intent);
        }
    }

    @Override
    public void onDialogClosed(ChooseColorDialogFragment dialog) {

        Turn turn = new Turn(Turn.TurnType.CHOOSE_COLOR);
        turn.setColor(dialog.getColor());
        this.getActualGameRound().doTurn(turn);

        // TODO: send turn to other players

        this.viewManager.updateView();
    }

    private GameRound getActualGameRound () {
        return UnoDatabase.getInstance().getCurrentGameSession().getActualGameRound();
    }

    private GameSession getCurrentGameSession() {
        return UnoDatabase.getInstance().getCurrentGameSession();
    }

    private Player getLocalPlayer() {
        return UnoDatabase.getInstance().getLocalPlayer();
    }

    private GameState getGameState() {
        return getActualGameRound().getGamestate();
    }

    private boolean isMyTurn () {
        return this.getGameState().getActualPlayer().getID() == this.getLocalPlayer().getID();
    }

    private void endGameRound () {

        DestroyGameRequest destroyGameRequest = new DestroyGameRequest();
        destroyGameRequest.setGameId(this.getCurrentGameSession().getID());
        destroyGameRequest.setHostId(this.getCurrentGameSession().getHost().getID());

        RequestProcessor rp = new RequestProcessor(this);
        rp.execute(destroyGameRequest);
    }

    public RelativeLayout getFlPlayedCards() {
        return flPlayedCards;
    }

    public TextView getTvDrawCounter() {
        return tvDrawCounter;
    }

    public LinearLayout getLlHand() {
        return llHand;
    }

    public ListView getLvPlayers() {
        return lvPlayers;
    }

    public ImageView getIvDirection() {
        return ivDirection;
    }

    public RelativeLayout getColorIndicator() {
        return colorIndicator;
    }
}