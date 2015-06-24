package com.gamble.unopp;

import android.content.ClipData;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
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
import com.gamble.unopp.connection.requests.GetUpdateRequest;
import com.gamble.unopp.connection.requests.SetUpdateRequest;
import com.gamble.unopp.connection.response.DestroyGameResponse;
import com.gamble.unopp.connection.response.GetUpdateResponse;
import com.gamble.unopp.connection.response.Response;
import com.gamble.unopp.connection.response.SetUpdateResponse;
import com.gamble.unopp.fragments.CheatDialogFragment;
import com.gamble.unopp.fragments.ChooseColorDialogFragment;
import com.gamble.unopp.model.game.GameRound;
import com.gamble.unopp.model.game.GameSession;
import com.gamble.unopp.model.game.GameState;
import com.gamble.unopp.model.game.GameUpdate;
import com.gamble.unopp.model.game.Player;
import com.gamble.unopp.model.cards.Card;
import com.gamble.unopp.model.game.Turn;
import com.gamble.unopp.model.management.ICheatDialogListener;
import com.gamble.unopp.model.management.IChooseColorDialogListener;
import com.gamble.unopp.model.management.UnoDatabase;
import com.gamble.unopp.model.management.ViewManager;

import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;


public class GameScreenActivity extends ActionBarActivity implements View.OnDragListener, View.OnLongClickListener, View.OnClickListener, View.OnLayoutChangeListener, SensorEventListener, RequestProcessorCallback, IChooseColorDialogListener, ICheatDialogListener, DialogInterface.OnDismissListener {

    private RelativeLayout              rlScreen;
    private ImageView                   ivCheatFigure;
    private HorizontalScrollView        hswHand;
    private LinearLayout                llHand;
    private TextView                    tvDrawCounter;
    private RelativeLayout              flUnplayedCards;
    private RelativeLayout              flPlayedCards;
    private ChooseColorDialogFragment   chooseColorDialogFragment;
    private CheatDialogFragment         cheatDialogFragment;
    private ImageView                   ivDirection;
    private ListView                    lvPlayers;
    private ImageView                   ivCardFromOtherPlayer;

    private ViewManager                 viewManager;
    private Timer                       updateTimer;
    private Timer                       cheatFigureTimer;
    private Timer                       cheatFigureShownTimer;
    private boolean                     cheatFigureClicked;
    private boolean                     timerStopped;
    private Timer                       unoTimer;
    private boolean                     unoDoneInTime;

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
        this.rlScreen           = (RelativeLayout) findViewById(R.id.rlScreen);
        this.hswHand            = (HorizontalScrollView) findViewById(R.id.hswHand);
        this.llHand             = (LinearLayout) findViewById(R.id.llHand);
        this.tvDrawCounter      = (TextView) findViewById(R.id.tvDrawCounter);
        this.flUnplayedCards    = (RelativeLayout) findViewById(R.id.flUnplayedCards);
        this.flPlayedCards      = (RelativeLayout) findViewById(R.id.flPlayedCards);
        this.lvPlayers          = (ListView) findViewById(R.id.lvPlayers);
        this.ivDirection        = (ImageView) findViewById(R.id.ivDirection);

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

        // init cheatDialogFragment
        cheatDialogFragment = new CheatDialogFragment();

        // init dragging
        flPlayedCards.setOnDragListener(this);

        // start receiving game updates
        this.startUpdateTimer();

        // start timer for the appearance of the cheat figure
        this.startCheatFigureTimer();
    }

    private void sendGetUpdateRequest () {

        // get udates from server
        GetUpdateRequest request = new GetUpdateRequest();
        request.setGameID(this.getCurrentGameSession().getID());
        request.setLastKnownUpdateID(this.getActualGameRound().getLocalUpdateID());

        RequestProcessor processor = new RequestProcessor(this);
        processor.execute(request);
    }

    @Override
    public void onResume() {
        super.onResume();
        sensorMan.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_UI);
        this.startUpdateTimer();
    }

    @Override
    protected void onPause() {
        super.onPause();
        sensorMan.unregisterListener(this);
        this.stopUpdateTimer();
    }

    @Override
    protected void onStop() {
        super.onStop();
        this.stopUpdateTimer();
    }

    @Override
    public boolean onLongClick(View v) {

        if (this.isMyTurn()) {

            ImageView card = (ImageView) v;

            ClipData data = ClipData.newPlainText("card", Integer.toString(((Card)card.getTag()).getID()));
            View.DragShadowBuilder shadowBuilder = new View.DragShadowBuilder(card);
            card.startDrag(data, shadowBuilder, card, 0);
            card.setVisibility(View.INVISIBLE);
            return true;
        }
        else {
            return false;
        }

    }

    private Turn createPlayCardTurn (Card card) {

        Turn turn = new Turn(Turn.TurnType.PLAY_CARD);
        turn.setCard(card);

        return turn;
    }

    private void playCard (Turn turn) {

        this.getActualGameRound().doTurn(turn);
        this.viewManager.updateView();
        this.broadcastTurn(turn);

        // check if color has to be chosen
        if (this.getLocalPlayer().hasToChooseColor()) {
            chooseColorDialogFragment.show(getFragmentManager(), "chooseColor");
        }
        else {
            // check if player has to say uno
            if (this.getLocalPlayer().isHasToCallUno()) {
                this.startUnoTimer();
            }
        }

        this.checkWinner();
    }

    private void checkWinner () {

        // check if someone has one
        if (getActualGameRound().getWinner() != null) {

            Intent intent = new Intent(this, GameRoundSummaryActivity.class);
            startActivity(intent);
        }
    }

    @Override
    public boolean onDrag (View v, DragEvent event) {

        if (this.isMyTurn()) {

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
                        v.setBackgroundColor(Color.TRANSPARENT);
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
            case R.id.action_sortHand:
                this.getLocalPlayer().sortHand();
                this.viewManager.updateView();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void callUno(View view) {
        callUno();
    }

    public void callUno () {

        if (this.getCurrentGameSession() != null && this.getActualGameRound() != null) {

            this.unoDoneInTime = true;
        }
    }

    private void broadcastTurn (Turn turn) {

        SetUpdateRequest    request = new SetUpdateRequest();
        request.setGameID(this.getCurrentGameSession().getID());
        request.setUpdateID(turn.getID());
        request.setUpdate(turn.serializeUpdate());

        RequestProcessor processor = new RequestProcessor(this);
        processor.execute(request);
    }

    @Override
    public void onClick(View view) {

        // player wants to draw from stack
        if (view == this.flUnplayedCards) {
            if (this.isMyTurn()) {

                Turn drawTurn = new Turn(Turn.TurnType.DRAW);

                if (this.getActualGameRound().checkTurn(drawTurn)) {

                    this.getActualGameRound().doTurn(drawTurn);
                    this.viewManager.updateView();
                    this.broadcastTurn(drawTurn);
                }
                else {

                    // TODO update view to show that drawing is not possible
                }
            }

        // cheating figure was touched
        } else if (view == this.ivCheatFigure) {
            cheatFigureClicked = true;

            cheatDialogFragment.show(getFragmentManager(), "cheatDialog");
        } else if (view == this.ivCardFromOtherPlayer) {
            this.rlScreen.removeView(ivCardFromOtherPlayer);
        }
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
        else if (response instanceof SetUpdateResponse) {

            if (response.getResponseResult() != null && response.getResponseResult().isStatus()) {
                // TODO
            }
            else {
                // TODO
            }
        }
        else if (response instanceof GetUpdateResponse && !this.timerStopped) {

            GetUpdateResponse getUpdateResponse = (GetUpdateResponse) response;

            if (response.getResponseResult() != null && response.getResponseResult().isStatus() && getUpdateResponse.getGameUpdates() != null) {

                for (GameUpdate gameUpdate : getUpdateResponse.getGameUpdates()) {
                    if (gameUpdate instanceof Turn && this.getActualGameRound().getLocalUpdateID() < ((Turn) gameUpdate).getID()) {
                        Turn turn = (Turn) gameUpdate;

                        this.getActualGameRound().doTurn(turn);
                        this.checkWinner();
                    }
                }

                if (getUpdateResponse.getGameUpdates().size() > 0) {
                    this.viewManager.updateView();
                }
            }
            else if (response.getResponseResult() != null && !response.getResponseResult().isStatus()) {

                // error when getting update. leave game to lobby
                Intent intent = new Intent(this, LobbyActivity.class);
                startActivity(intent);
            }
        }
    }

    @Override
    public void onDialogClosed(ChooseColorDialogFragment dialog) {

        Turn turn = new Turn(Turn.TurnType.CHOOSE_COLOR);
        turn.setColor(dialog.getColor());

        if (this.getActualGameRound().checkTurn(turn)) {

            this.getActualGameRound().doTurn(turn);
            this.viewManager.updateView();
            this.broadcastTurn(turn);

            // check if player has to say uno
            if (this.getLocalPlayer().isHasToCallUno()) {
                this.startUnoTimer();
            }
        }
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

        this.stopUpdateTimer();

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

    public ImageView getIvCheatFigure() {
        return ivCheatFigure;
    }

    public void setIvCheatFigure(ImageView ivCheatFigure) {
        this.ivCheatFigure = ivCheatFigure;
    }

    public void setCheatFigureClicked(boolean cheatFigureClicked) {
        this.cheatFigureClicked = cheatFigureClicked;
    }

    public RelativeLayout getRlScreen() {
        return rlScreen;
    }

    private void unoTimerExpired () {

        this.unoTimer.cancel();
        this.unoTimer.purge();

        if (this.unoDoneInTime) {

            // call uno
            Turn turn = new Turn(Turn.TurnType.CALL_UNO);

            this.getActualGameRound().doTurn(turn);
            this.viewManager.updateView();
            this.broadcastTurn(turn);
        }
        else {

            // player has to draw a card for punishment
            Turn turn = new Turn(Turn.TurnType.DRAW);

            this.getActualGameRound().doTurn(turn);
            this.viewManager.updateView();
            this.broadcastTurn(turn);
        }
    }

    @Override
    public void onBackPressed()
    {
        this.stopUpdateTimer();
        this.stopCheatFigureTimer();
        super.onBackPressed();
    }

    private void stopUpdateTimer () {

        this.timerStopped = true;

        if (this.updateTimer != null) {

            this.updateTimer.cancel();
            this.updateTimer.purge();
        }
    }

    private void startUpdateTimer () {

        this.stopUpdateTimer();

        this.timerStopped = false;

        this.updateTimer = new Timer();
        this.updateTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                sendGetUpdateRequest();
            }
        }, 5000, 2000);
    }

    private void startUnoTimer () {

        this.unoDoneInTime = false;

        this.unoTimer = new Timer();
        this.unoTimer.schedule(new TimerTask() {
            @Override
            public void run() {

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        // This code will always run on the UI thread, therefore is safe to modify UI elements.
                        unoTimerExpired();
                    }
                });
            }
        }, 4000);
    }

    private void startCheatFigureTimer () {
        this.stopCheatFigureTimer();
        Random r = new Random();

        // wait randomly between 20 and 120 seconds
        int randomDelay = r.nextInt(120000 - 20000 + 1) + 20000;

        this.cheatFigureTimer = new Timer();
        this.cheatFigureTimer.schedule(new TimerTask() {
            @Override
            public void run() {

                runOnUiThread(new Runnable() {

                    @Override
                    public void run() {
                        viewManager.drawCheatFigure();
                    }
                });
            }
        }, randomDelay);
    }

    private void stopCheatFigureTimer() {
        if (this.cheatFigureTimer != null) {

            this.cheatFigureTimer.cancel();
            this.cheatFigureTimer.purge();
        }
    }

    public void startCheatFigureShownTimer() {

        // show cheatFigure for 3 seconds
        this.cheatFigureShownTimer = new Timer();
        cheatFigureShownTimer.schedule(new TimerTask() {
            @Override
            public void run() {

                runOnUiThread(new Runnable() {

                    @Override
                    public void run() {
                        // if it was not clicked remove cheatFigure and restart cheatFigureTimer
                        if (!cheatFigureClicked) {
                            rlScreen.removeView(ivCheatFigure);

                            startCheatFigureTimer();
                        }
                    }
                });
            }
        }, 3000);
    }

    @Override
    public void onDialogClosed(CheatDialogFragment dialog) {

        // remove cheating figure from view
        this.rlScreen.removeView(this.ivCheatFigure);

        // display random card from the selected player
        Player selectedPlayer = cheatDialogFragment.getSelectedPlayer();

        // display a random card from the selected player in the middle of the screen
        if (selectedPlayer != null) {
            this.displayRandomCard(selectedPlayer);
        }

        // start cheatFigureTimer again
        startCheatFigureTimer();
    }

    private void displayRandomCard(Player selectedPlayer) {

        if (this.ivCardFromOtherPlayer != null && this.rlScreen.indexOfChild(this.ivCardFromOtherPlayer) >= 0) {
            this.rlScreen.removeView(this.ivCardFromOtherPlayer);
        }

        // get random card
        Random r = new Random();
        int randomIndex = r.nextInt(selectedPlayer.getHand().size());
        Card card = selectedPlayer.getHand().get(randomIndex);

        // create imageView and upscale the image
        ivCardFromOtherPlayer = new ImageView(this.getBaseContext());
        Bitmap bitmapImage = card.getImage();
        int nh = (int) (bitmapImage.getHeight() * (300d / bitmapImage.getWidth()));
        Bitmap scaled = Bitmap.createScaledBitmap(bitmapImage, 300, nh, true);
        ivCardFromOtherPlayer.setImageBitmap(scaled);
        ivCardFromOtherPlayer.setTag(card);

        // set card half transparent
        ivCardFromOtherPlayer.setAlpha(210);
        ivCardFromOtherPlayer.setOnClickListener(this);
        ivCardFromOtherPlayer.setAdjustViewBounds(true);

        // center the Image View
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams
                (RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        layoutParams.addRule(RelativeLayout.CENTER_IN_PARENT);
        layoutParams.addRule(RelativeLayout.CENTER_HORIZONTAL);
        layoutParams.addRule(RelativeLayout.CENTER_VERTICAL);
        ivCardFromOtherPlayer.setLayoutParams(layoutParams);

        // display card
        this.rlScreen.addView(ivCardFromOtherPlayer);
    }

    @Override
    public void onDismiss(DialogInterface dialog) {

        // remove cheating figure from view
        this.rlScreen.removeView(this.ivCheatFigure);

        // start cheatFigureTimer again
        startCheatFigureTimer();
    }
}