package com.gamble.unopp;

import android.content.Intent;
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
import com.gamble.unopp.connection.requests.CreateGameRequest;
import com.gamble.unopp.connection.response.CreateGameResponse;
import com.gamble.unopp.connection.response.Response;
import com.gamble.unopp.fragments.ErrorDialogFragment;
import com.gamble.unopp.helper.SharedPreferencesKeys;
import com.gamble.unopp.model.game.GameRound;
import com.gamble.unopp.model.game.GameSession;
import com.gamble.unopp.model.game.Player;
import com.gamble.unopp.model.management.UnoDatabase;


public class NewGameActivity extends ActionBarActivity {

    private EditText            txtGameName;
    private Player              player;
    private SharedPreferences   sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        requestWindowFeature(Window.FEATURE_NO_TITLE);

        super.onCreate(savedInstanceState);

        // remove title
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_new_game);

        // get views
        txtGameName         = (EditText) findViewById(R.id.txtGameName);

        // get player
        this.player         = UnoDatabase.getInstance().getLocalPlayer();

        sharedPreferences   = getSharedPreferences(SharedPreferencesKeys.PREFS, MODE_PRIVATE);

        // check if player has enterd a name previously
        setNameFromPrefs ();
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
            this.txtGameName.setError(null);

            // save name into shared preferences
            SharedPreferences.Editor e  = sharedPreferences.edit();
            e.putString(SharedPreferencesKeys.GAMENAME, this.txtGameName.getText().toString());
            e.commit();

            CreateGameRequest request = new CreateGameRequest();
            request.setLatitude(0);
            request.setLongitude(0);
            request.setPlayerID(UnoDatabase.getInstance().getLocalPlayer().getID());
            request.setMaxPlayers(GameSettings.MAX_PLAYERS);
            request.setGameName(this.txtGameName.getText().toString());

            RequestProcessor rp = new RequestProcessor(new RequestProcessorCallback() {
                @Override
                public void requestFinished(Response response) {
                    createGameFinished((CreateGameResponse) response);
                }
            });
            rp.execute(request);
        }
        else {
            this.txtGameName.setError("Der Spielname muss mind. 3 Zeichen lang sein.");
        }
    }

    private void createGameFinished (CreateGameResponse response) {

        if (response != null) {
            GameSession session = response.getGameSession();
            UnoDatabase.getInstance().setCurrentGameSession(session);

            // update local player
            this.player.setGameSession(session);

            // create an Intent to take you over to the GameDetailsActivity
            Intent intent = new Intent(this, GameDetailsActivity.class);

            startActivity(intent);
        }
        else {
            String errorMessage = "Neues Spiel konnte nicht erstellt werden.\n";
            if (response.getResponseResult() != null && !response.getResponseResult().isStatus()) {
                errorMessage += response.getResponseResult().getMessage();
            }

            // display error message
            ErrorDialogFragment errorDialogFragment = new ErrorDialogFragment();
            Bundle args = new Bundle();
            args.putString("errorMessage", errorMessage);
            errorDialogFragment.setArguments(args);
            errorDialogFragment.show(getFragmentManager(), "error");
        }
    }

    public void setNameFromPrefs() {

        String name = sharedPreferences.getString(SharedPreferencesKeys.GAMENAME, "");

        if (name.length() > 0) {

            this.txtGameName.setText(name);
        }
    }
}
