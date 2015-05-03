package com.gamble.unopp;

import android.content.SharedPreferences;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;

import com.gamble.unopp.connection.RequestProcessor;
import com.gamble.unopp.connection.RequestProcessorCallback;
import com.gamble.unopp.connection.requests.CreateGameSessionRequest;
import com.gamble.unopp.connection.response.Response;

import java.util.ArrayList;


public class NewGameActivity extends ActionBarActivity {

    private EditText            txtGameName;
    private String              username;
    private SharedPreferences   sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        requestWindowFeature(Window.FEATURE_NO_TITLE);

        super.onCreate(savedInstanceState);

        // remove title
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_new_game);

        // get username
        sharedPreferences   = getSharedPreferences(SharedPreferencesKeys.PREFS, MODE_PRIVATE);
        username            = sharedPreferences.getString(SharedPreferencesKeys.USERNAME, "");

        // get views
        txtGameName         = (EditText) findViewById(R.id.txtGameName);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_new_game, menu);
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
     * Eventliesteners for Views
     */

    public void createGame (View v) {

        if (this.txtGameName.getText().length() > 2) {

            CreateGameSessionRequest    request = new CreateGameSessionRequest();
            request.setLatitude(0);
            request.setLongitude(0);
            request.setCreatorName(username);
            request.setGameName(this.txtGameName.getText().toString());

            RequestProcessor rp = new RequestProcessor(new RequestProcessorCallback() {
                @Override
                public void requestFinished(Response response) {
                    createGameFinished();
                }
            });
            rp.execute(request);
        }
        else {
            // TODO: show validation error.
        }
    }

    private void createGameFinished () {

        // TODO: show game details activity


    }
}
