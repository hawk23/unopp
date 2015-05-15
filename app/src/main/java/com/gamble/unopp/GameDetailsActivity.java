package com.gamble.unopp;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.gamble.unopp.connection.RequestProcessor;
import com.gamble.unopp.connection.RequestProcessorCallback;
import com.gamble.unopp.connection.requests.GetGameRequest;
import com.gamble.unopp.connection.response.GetGameResponse;
import com.gamble.unopp.connection.response.Response;
import com.gamble.unopp.model.game.GameSession;
import com.gamble.unopp.model.game.Player;
import com.gamble.unopp.model.management.UnoDatabase;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class GameDetailsActivity extends ActionBarActivity implements RequestProcessorCallback {

    private Player player;
    private GameSession gameSession;
    private TextView txtGameDetailsName;
    private ListView listCurrentPlayers;
    private Timer updateTimer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        requestWindowFeature(Window.FEATURE_NO_TITLE);

        super.onCreate(savedInstanceState);

        // remove title
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_game_details);

        this.txtGameDetailsName = (TextView) findViewById(R.id.txtGameDetailsName);
        this.listCurrentPlayers = (ListView) findViewById(R.id.listCurrentPlayers);

        this.displayDetails();

        this.updateTimer = new Timer();
        this.updateTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                updateTimerTick();
            }
        }, 0, 1000);
    }

    private void updateTimerTick () {

        GetGameRequest getGameRequest = new GetGameRequest();
        getGameRequest.setGameID(UnoDatabase.getInstance().getCurrentGameSession().getID());

        RequestProcessor processor = new RequestProcessor(this);
        processor.execute(getGameRequest);
    }

    @Override
    public void requestFinished(Response response) {
        if (response != null) {
            if (response instanceof GetGameResponse) {
                GetGameResponse getGameResponse = (GetGameResponse) response;

                UnoDatabase.getInstance().setCurrentGameSession(getGameResponse.getGameSession());

                this.displayDetails();
            }
        }
    }

    private void displayDetails () {

        // load objects
        this.player         = UnoDatabase.getInstance().getLocalPlayer();
        this.gameSession    = UnoDatabase.getInstance().getCurrentGameSession();

        setTitle(getString(R.string.title_activity_game_details) +": "+ this.gameSession.getName());

        this.txtGameDetailsName.setText("Beigetretene Spieler: " +
                Integer.toString(this.gameSession.getCurrentPlayerCount()) + "/"+
                Integer.toString(this.gameSession.getMaxPlayers()));

        ArrayList players = new ArrayList();

        for (Player player : this.gameSession.getPlayers()) {
            players.add(player);
        }

        ArrayAdapter arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, players);
        this.listCurrentPlayers.setAdapter(arrayAdapter);
    }

    private void leaveGame () {
        // TODO
    }

    private void deleteGame () {
        // TODO
    }

    private void startGame () {
        // TODO

        // HACK
        // create an Intent to take you over to the Lobby
        Intent intent = new Intent(this, GameScreenActivity.class);

        // pack away the name into the lobbyIntent
        startActivity(intent);
        // END HACK
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        // Inflate the menu; this adds items to the action bar if it is present.
        if (this.gameSession.getHost().getID() == this.player.getID()) {
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
}
