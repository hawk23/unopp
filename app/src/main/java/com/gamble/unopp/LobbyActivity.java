package com.gamble.unopp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.gamble.unopp.connection.RequestProcessor;
import com.gamble.unopp.connection.RequestProcessorCallback;
import com.gamble.unopp.connection.requests.ListGamesRequest;
import com.gamble.unopp.connection.response.ListGamesResponse;
import com.gamble.unopp.connection.response.Response;

import java.util.ArrayList;

/**
 * Created by Verena on 25.04.2015.
 */
public class LobbyActivity extends ActionBarActivity {

    private ListView            existingGamesListView;
    private ArrayAdapter        mArrayAdapter;
    private String              existingGames;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        requestWindowFeature(Window.FEATURE_NO_TITLE);

        super.onCreate(savedInstanceState);

        // remove title
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.actitvity_lobby);

        ArrayList existingGames = new ArrayList();

        existingGamesListView = (ListView) findViewById(R.id.existingGamesListView);

        this.getAvailableGameSessions();
    }

    private void getAvailableGameSessions() {

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

        //HACK: hardcoded result
        ArrayList games = new ArrayList();

        games.add("Albert's Game");
        games.add("Roland's Game");

        displayAvailableGameSessions(games);
    }

    private void displayAvailableGameSessions(ArrayList games) {

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
        switch (id) {
            case R.id.action_settings:
                return true;
            case R.id.action_newGame:
                this.newGame();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
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
}