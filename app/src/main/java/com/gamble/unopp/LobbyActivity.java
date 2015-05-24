package com.gamble.unopp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.gamble.unopp.connection.RequestProcessor;
import com.gamble.unopp.connection.RequestProcessorCallback;
import com.gamble.unopp.connection.requests.DestroyGameRequest;
import com.gamble.unopp.connection.requests.DestroyPlayerRequest;
import com.gamble.unopp.connection.requests.JoinGameRequest;
import com.gamble.unopp.connection.requests.ListGamesRequest;
import com.gamble.unopp.connection.response.DestroyGameResponse;
import com.gamble.unopp.connection.response.DestroyPlayerResponse;
import com.gamble.unopp.connection.response.JoinGameResponse;
import com.gamble.unopp.connection.response.ListGamesResponse;
import com.gamble.unopp.connection.response.Response;
import com.gamble.unopp.fragments.ErrorDialogFragment;
import com.gamble.unopp.model.game.GameSession;
import com.gamble.unopp.model.game.Player;
import com.gamble.unopp.model.management.UnoDatabase;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Verena on 25.04.2015.
 */
public class LobbyActivity extends ActionBarActivity implements AdapterView.OnItemClickListener, RequestProcessorCallback {

    private ListView            existingGamesListView;
    private ArrayAdapter        mArrayAdapter;
    private List<GameSession>   games;
    private Player              player;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        // load from database
        this.player = UnoDatabase.getInstance().getLocalPlayer();

        requestWindowFeature(Window.FEATURE_NO_TITLE);

        super.onCreate(savedInstanceState);

        // remove title
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.actitvity_lobby);

        ArrayList existingGames = new ArrayList();

        existingGamesListView = (ListView) findViewById(R.id.existingGamesListView);
        existingGamesListView.setOnItemClickListener(this);

        games = new ArrayList<>();
        this.getAvailableGameSessions();
    }

    private void refresh () {
        games = new ArrayList<>();
        this.getAvailableGameSessions();
    }

    private void getAvailableGameSessions()
    {
        ListGamesRequest gameSessionsRequest = new ListGamesRequest();
        gameSessionsRequest.setLatitude(0.0);
        gameSessionsRequest.setLongitude(0.0);
        gameSessionsRequest.setMaxdistance(10);

        RequestProcessor rp = new RequestProcessor(new RequestProcessorCallback() {
            @Override
            public void requestFinished(Response response) {

            listGamesFinished((ListGamesResponse) response);
            }
        });
        rp.execute(gameSessionsRequest);
    }

    private void listGamesFinished (ListGamesResponse response) {

        if (response != null) {

            for (GameSession gs : response.getGameSessions()) {
                games.add(gs);
            }

            displayAvailableGameSessions(games);
        }
        else {
            ErrorDialogFragment errorDialogFragment = new ErrorDialogFragment();
            Bundle args = new Bundle();
            args.putString("errorMessage", "Keine Spiele gefunden!");
            errorDialogFragment.setArguments(args);
            errorDialogFragment.show(getFragmentManager(), "error");
        }
    }

    private void displayAvailableGameSessions(List games) {

        mArrayAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, games);
        existingGamesListView.setAdapter(mArrayAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_lobby, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        // Handle item selection
        switch (id)
        {
            case R.id.action_settings:
                return true;
            case R.id.action_newGame:
                this.newGame();
                return true;
            case R.id.action_refresh:
                this.refresh();
                return true;
            case R.id.action_deleteAllGames:
                this.deleteAllGames();
                return true;
            case 16908332: // back button
                this.deletePlayer();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void deleteAllGames () {

        for (GameSession gameSession : this.games) {

            DestroyGameRequest request = new DestroyGameRequest();
            request.setGameId(gameSession.getID());
            request.setHostId(gameSession.getHost().getID());

            RequestProcessor rp = new RequestProcessor(this);
            rp.execute(request);
        }
    }

    private void deletePlayer ()
    {
        DestroyPlayerRequest destroyPlayerRequest = new DestroyPlayerRequest();
        destroyPlayerRequest.setPlayerId(this.player.getID());

        // the player is not in a game session yet
        destroyPlayerRequest.setGameId(-1);

        RequestProcessor rp = new RequestProcessor(this);
        rp.execute(destroyPlayerRequest);

        // create an Intent to take you back to the LobbyActivity
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    private void newGame () {

        // create an Intent to take you over to the Lobby
        Intent newGameIntent = new Intent(this, NewGameActivity.class);

        // pack away the name into the lobbyIntent
        startActivity(newGameIntent);
    }

    /**
     * Listener for Menu in action bar
     */

    /**
     * Eventlisteners for Views
     */
    @Override
    public void onItemClick(AdapterView<?> adapter, View v, int position, long id) {

        this.player = UnoDatabase.getInstance().getLocalPlayer();
        if (player != null) {

            final GameSession game = (GameSession) adapter.getItemAtPosition(position);

            JoinGameRequest joinGameRequest = new JoinGameRequest();
            joinGameRequest.setGameId(game.getID());
            joinGameRequest.setPlayerId(player.getID());

            RequestProcessor rp = new RequestProcessor(new RequestProcessorCallback() {
                @Override
                public void requestFinished(Response response) {
                    joinGameFinished((JoinGameResponse) response, game);
                }
            });
            rp.execute(joinGameRequest);
        }
        else {

            // goto main activity to create new player
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        }
    }

    private void joinGameFinished(JoinGameResponse response, GameSession game) {
        if (response != null && response.getResponseResult().isStatus()) {
            UnoDatabase.getInstance().setCurrentGameSession(game);

            // create an Intent to take you over to the GameDetailsActivity
            Intent intent = new Intent(this, GameDetailsActivity.class);
            startActivity(intent);
        }
    }

    @Override
    public void requestFinished(Response response)
    {
        if (response instanceof DestroyGameResponse) {
            this.refresh();
        }
    }
}