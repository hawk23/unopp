package com.gamble.unopp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.AssetManager;
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
import com.gamble.unopp.connection.requests.CreatePlayerRequest;
import com.gamble.unopp.connection.response.CreatePlayerResponse;
import com.gamble.unopp.connection.response.Response;
import com.gamble.unopp.model.game.CardDeck;
import com.gamble.unopp.model.management.UnoDatabase;

public class MainActivity extends ActionBarActivity {

    private EditText inputName;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        super.onCreate(savedInstanceState);

        // remove title
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_main);

        inputName           = (EditText) findViewById(R.id.txtUsername);
        sharedPreferences   = getSharedPreferences(SharedPreferencesKeys.PREFS, MODE_PRIVATE);

        // check if player has enterd a name previously
        setNameFromPrefs ();
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

    public void startGame(View v) {

        String inputName            = this.inputName.getText().toString();

        // save name into shared preferences
        SharedPreferences.Editor e  = sharedPreferences.edit();
        e.putString(SharedPreferencesKeys.USERNAME, inputName);
        e.commit();

        // create player on server
        CreatePlayerRequest request = new CreatePlayerRequest();
        request.setName(inputName);

        RequestProcessor  processor = new RequestProcessor(new RequestProcessorCallback() {
            @Override
            public void requestFinished(Response response) {
                createPlayerFinished((CreatePlayerResponse) response);
            }
        });
        processor.execute(request);
    }

    private void createPlayerFinished (CreatePlayerResponse response) {

        if (response != null) {
            // save player in database
            UnoDatabase.getInstance().setLocalPlayer(response.getPlayer());

            // create an Intent to take you over to the Lobby
            Intent lobbyIntent = new Intent(this, LobbyActivity.class);

            // pack away the name into the lobbyIntent
            startActivity(lobbyIntent);
        }
        else {

            // TODO
        }
   }

    public void setNameFromPrefs() {

        String name = sharedPreferences.getString(SharedPreferencesKeys.USERNAME, "");

        if (name.length() > 0) {

            inputName.setText(name);
        }
    }
}
