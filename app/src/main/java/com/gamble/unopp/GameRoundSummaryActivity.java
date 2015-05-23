package com.gamble.unopp;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.gamble.unopp.connection.RequestProcessor;
import com.gamble.unopp.connection.RequestProcessorCallback;
import com.gamble.unopp.connection.requests.DestroyGameRequest;
import com.gamble.unopp.connection.response.DestroyGameResponse;
import com.gamble.unopp.connection.response.Response;
import com.gamble.unopp.model.game.Player;
import com.gamble.unopp.model.management.UnoDatabase;


public class GameRoundSummaryActivity extends ActionBarActivity implements View.OnClickListener, RequestProcessorCallback {

    private TextView    tvWinners;
    private Button      btnBackToLobby;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        // remove title
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_game_round_summary);

        // get views
        this.tvWinners          = (TextView)    findViewById(R.id.tvWinner);
        this.btnBackToLobby     = (Button)      findViewById(R.id.btnBackToLobby);

        this.init();
        this.displayWinnerText();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_game_round_summary, menu);
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

    private void init () {

        this.btnBackToLobby.setOnClickListener(this);
    }

    private void displayWinnerText () {

        Player winner = UnoDatabase.getInstance().getCurrentGameSession().getActualGameRound().getWinner();

        if (winner != null) {

            this.tvWinners.setText(winner.getName() + " hat gewonnen!!!");
        }
    }

    @Override
    public void onClick(View v) {

        if (v == this.btnBackToLobby) {

            DestroyGameRequest destroyGameRequest = new DestroyGameRequest();
            destroyGameRequest.setGameId(UnoDatabase.getInstance().getCurrentGameSession().getID());
            destroyGameRequest.setHostId(UnoDatabase.getInstance().getCurrentGameSession().getHost().getID());

            RequestProcessor rp = new RequestProcessor(this);
            rp.execute(destroyGameRequest);
        }
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
}
