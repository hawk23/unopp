package com.gamble.unopp;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.transition.Transition;
import android.transition.TransitionValues;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.gamble.unopp.connection.RequestProcessor;
import com.gamble.unopp.connection.RequestProcessorCallback;
import com.gamble.unopp.connection.requests.DestroyGameRequest;
import com.gamble.unopp.connection.requests.GetGameRequest;
import com.gamble.unopp.connection.requests.LeaveGameRequest;
import com.gamble.unopp.connection.requests.StartGameRequest;
import com.gamble.unopp.connection.response.DestroyGameResponse;
import com.gamble.unopp.connection.response.GetGameResponse;
import com.gamble.unopp.connection.response.LeaveGameResponse;
import com.gamble.unopp.connection.response.Response;
import com.gamble.unopp.connection.response.StartGameResponse;
import com.gamble.unopp.fragments.ErrorDialogFragment;
import com.gamble.unopp.helper.ErrorDialogTypes;
import com.gamble.unopp.model.game.GameSession;
import com.gamble.unopp.model.game.Player;
import com.gamble.unopp.model.management.IErrorDialogListener;
import com.gamble.unopp.model.management.UnoDatabase;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class GameDetailsActivity extends ActionBarActivity implements RequestProcessorCallback, IErrorDialogListener {

    private Player player;
    private GameSession gameSession;
    private TextView txtGameDetailsName;
    private ListView listCurrentPlayers;
    private Timer updateTimer;
    private boolean timerStopped = false;
    private boolean gameStarted = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        requestWindowFeature(Window.FEATURE_NO_TITLE);

        super.onCreate(savedInstanceState);

        // remove title
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_game_details);

        this.txtGameDetailsName = (TextView) findViewById(R.id.txtGameDetailsName);
        this.listCurrentPlayers = (ListView) findViewById(R.id.listCurrentPlayers);

        this.gameStarted = false;

        this.displayDetails();

        this.timerStopped = false;
        this.updateTimer = new Timer();
        this.updateTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                updateTimerTick();
            }
        }, 0, 5000);
    }

    private void updateTimerTick () {

        GameSession currentGameSession = UnoDatabase.getInstance().getCurrentGameSession();

        if (currentGameSession != null) {
            GetGameRequest getGameRequest = new GetGameRequest();
            getGameRequest.setGameID(UnoDatabase.getInstance().getCurrentGameSession().getID());

            RequestProcessor processor = new RequestProcessor(this);
            processor.execute(getGameRequest);
        }
    }

    @Override
    public void onDialogClosed(ErrorDialogFragment dialog) {

        switch (dialog.getType()) {
            case ErrorDialogTypes.GET_GAME_FAILED:
                // create an Intent to take you back to the LobbyActivity
                Intent intent = new Intent(this, LobbyActivity.class);
                startActivity(intent);
                break;
        }
    }

    @Override
    public void requestFinished(Response response) {
        if (response != null) {
            if (response instanceof GetGameResponse && !this.gameStarted) {

                GetGameResponse getGameResponse = (GetGameResponse) response;

                if (getGameResponse.getResponseResult().isStatus()) {
                    // update game session
                    UnoDatabase.getInstance().setCurrentGameSession(getGameResponse.getGameSession());

                    // update local player
                    Player localPlayer          = UnoDatabase.getInstance().getLocalPlayer();
                    Player updatedLocalPlayer   = getGameResponse.getGameSession().getPlayerById(localPlayer.getID());
                    UnoDatabase.getInstance().setLocalPlayer(updatedLocalPlayer);

                    if (getGameResponse.getGameSession() != null && getGameResponse.getGameSession().isStarted()) {
                        this.stopUpdateTimer ();
                        this.gameStarted = true;

                        // create an Intent to take you over to the GameScreenActivity
                        Intent intent = new Intent(this, GameScreenActivity.class);
                        startActivity(intent);
                    } else {
                        this.displayDetails();
                    }
                }
                else {
                    this.stopUpdateTimer ();
                    this.displayError(ErrorDialogTypes.GET_GAME_FAILED, response.getResponseResult().getMessage());
                }

            } else if (response instanceof StartGameResponse) {
                StartGameResponse startGameResponse = (StartGameResponse) response;

                if (startGameResponse.getResponseResult() != null) {
                    if (startGameResponse.getResponseResult() != null && startGameResponse.getResponseResult().isStatus()) {
                        this.stopUpdateTimer ();

                        // send get game to get initial status
                        GetGameRequest getGameRequest = new GetGameRequest();
                        getGameRequest.setGameID(UnoDatabase.getInstance().getCurrentGameSession().getID());

                        RequestProcessor processor = new RequestProcessor(this);
                        processor.execute(getGameRequest);
                    }
                    else {
                        this.displayError(ErrorDialogTypes.START_GAME_FAILED, response.getResponseResult().getMessage());
                    }
                }
            } else if (response instanceof LeaveGameResponse) {
                LeaveGameResponse leaveGameResponse = (LeaveGameResponse) response;

                if (leaveGameResponse.getResponseResult() != null && leaveGameResponse.getResponseResult().isStatus()) {
                    this.stopUpdateTimer ();

                    // delete current gameSession
                    UnoDatabase.getInstance().setCurrentGameSession(null);

                    // create an Intent to take you back to the LobbyActivity
                    Intent intent = new Intent(this, LobbyActivity.class);
                    startActivity(intent);
                }
                else {
                    this.displayError(ErrorDialogTypes.LEAVE_GAME_FAILED, response.getResponseResult().getMessage());
                }
            }
            else if (response instanceof DestroyGameResponse) {
                DestroyGameResponse destroyGameResponse = (DestroyGameResponse) response;

                if (destroyGameResponse.getResponseResult() != null && destroyGameResponse.getResponseResult().isStatus()) {
                    this.stopUpdateTimer ();

                    // delete current gameSession
                    UnoDatabase.getInstance().setCurrentGameSession(null);

                    // create an Intent to take you back to the LobbyActivity
                    Intent intent = new Intent(this, LobbyActivity.class);
                    startActivity(intent);
                }
                else {
                    this.displayError(ErrorDialogTypes.DESTROY_GAME_FAILED, response.getResponseResult().getMessage());
                }
            }
        }
        else {
            // TODO
        }
    }

    private void displayError (String type, String message) {

        // display error message
        ErrorDialogFragment errorDialogFragment = new ErrorDialogFragment();
        Bundle args = new Bundle();
        args.putString("errorMessage", message);
        args.putString(ErrorDialogFragment.TYPE_IDENTIFIER, type);
        errorDialogFragment.setArguments(args);
        errorDialogFragment.show(getFragmentManager(), "error");
    }

    private void displayDetails () {

        // load objects
        this.player         = UnoDatabase.getInstance().getLocalPlayer();
        this.gameSession    = UnoDatabase.getInstance().getCurrentGameSession();

        setTitle(getString(R.string.title_activity_game_details) +": "+ this.gameSession.getName());

        this.txtGameDetailsName.setText("Beigetretene Spieler: (" +
                Integer.toString(this.gameSession.getCurrentPlayerCount()) + "/"+
                Integer.toString(this.gameSession.getMaxPlayers()) + ")");

        ArrayList players = new ArrayList();

        for (Player player : this.gameSession.getPlayers()) {
            players.add(player);
        }

        ArrayAdapter arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, players);
        this.listCurrentPlayers.setAdapter(arrayAdapter);
        arrayAdapter.notifyDataSetChanged();
    }

    private void leaveGame () {

        this.stopUpdateTimer ();

        LeaveGameRequest leaveGameRequest = new LeaveGameRequest();
        leaveGameRequest.setGameId(this.gameSession.getID());
        leaveGameRequest.setPlayerId(this.player.getID());

        RequestProcessor rp = new RequestProcessor(this);
        rp.execute(leaveGameRequest);
    }

    private void deleteGame () {

        this.stopUpdateTimer ();

        DestroyGameRequest destroyGameRequest = new DestroyGameRequest();
        destroyGameRequest.setGameId(this.gameSession.getID());
        destroyGameRequest.setHostId(this.player.getID());

        RequestProcessor rp = new RequestProcessor(this);
        rp.execute(destroyGameRequest);
    }

    private void startGame () {

        this.stopUpdateTimer ();

        StartGameRequest startGameRequest = new StartGameRequest();
        startGameRequest.setGameId(this.gameSession.getID());
        startGameRequest.setHostId(this.player.getID());

        RequestProcessor rp = new RequestProcessor(this);
        rp.execute(startGameRequest);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        // Inflate the menu; this adds items to the action bar if it is present.
        if (this.player != null && this.gameSession.getHost().getID() == this.player.getID()) {
            getMenuInflater().inflate(R.menu.menu_game_details_host, menu);
        }
        else {
            getMenuInflater().inflate(R.menu.menu_game_details, menu);
        }

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
            case R.id.action_startgame:
                this.startGame();
                return true;
            case R.id.action_leavegame:
                this.leaveGame();
                return true;
            case R.id.action_deletegame:
                this.deleteGame();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /**
     * called when the back button of the phone is pressed
     */
    @Override
    public void onBackPressed()
    {
        if (this.player != null && this.gameSession.getHost().getID() == this.player.getID()) {
            this.deleteGame();
        }
        else {
            this.leaveGame();
        }
    }

    private void stopUpdateTimer () {

        this.timerStopped = true;
        this.updateTimer.cancel();
        this.updateTimer.purge();
    }
}
