package com.gamble.unopp;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.gamble.unopp.connection.RequestProcessor;
import com.gamble.unopp.connection.RequestProcessorCallback;
import com.gamble.unopp.connection.requests.GetAvailableGameSessionsRequest;
import com.gamble.unopp.connection.response.Response;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Verena on 25.04.2015.
 */
public class LobbyActivity extends ActionBarActivity {

    private String username;
    private ListView existingGamesListView;
    private ArrayAdapter mArrayAdapter;
    private String existingGames;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        // remove title
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.actitvity_lobby);

        username = this.getIntent().getExtras().getString(IntentValueKeys.USERNAME);

        ArrayList existingGames = new ArrayList();

        existingGamesListView = (ListView) findViewById(R.id.existingGamesListView);

        this.getAvailableGameSessions();
    }

    private void getAvailableGameSessions() {

        GetAvailableGameSessionsRequest gameSessionsRequest = new GetAvailableGameSessionsRequest();
        gameSessionsRequest.setLatitude(0.0);
        gameSessionsRequest.setLongitude(0.0);
        gameSessionsRequest.setMaxdistance(10);

        RequestProcessor rp = new RequestProcessor(new RequestProcessorCallback() {
            @Override
            public void requestFinished(Response response) {

                //HACK: hardcoded result
                ArrayList games = new ArrayList();

                games.add("Albert's Game");
                games.add("Roland's Game");

                displayAvailableGameSessions(games);
            }
        });
        rp.execute(gameSessionsRequest);
    }

    private void displayAvailableGameSessions(ArrayList games) {

        mArrayAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, games);
        existingGamesListView.setAdapter(mArrayAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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

    /**
     * Eventlisteners for Views
     */

    private void newGameSession (View v) {


    }
}