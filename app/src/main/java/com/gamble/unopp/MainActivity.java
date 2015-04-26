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
import android.widget.Button;
import android.widget.EditText;


public class MainActivity extends ActionBarActivity implements View.OnClickListener {

    private static final String PREFS = "prefs";
    private static final String PREF_NAME = "name";

    private EditText inputName;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        super.onCreate(savedInstanceState);

        // remove title
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_main);

        Button mainButton = (Button) findViewById(R.id.btnStart);
        mainButton.setOnClickListener(this);

        inputName = (EditText) findViewById(R.id.txtUsername);

        sharedPreferences = getSharedPreferences(PREFS, MODE_PRIVATE);
        setNameFromPrefs();
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

    @Override
    public void onClick(View v) {
        String inputName = this.inputName.getText().toString();

        SharedPreferences.Editor e = sharedPreferences.edit();
        e.putString(PREF_NAME, inputName);
        e.commit();

        // create an Intent to take you over to the Lobby
        Intent lobbyIntent = new Intent(this, LobbyActivity.class);

        // pack away the name into the lobbyIntent
        lobbyIntent.putExtra("username", inputName);
        startActivity(lobbyIntent);
    }

    public void setNameFromPrefs() {
        String name = sharedPreferences.getString(PREF_NAME, "");

        if (name.length() > 0) {
            inputName.setText(name);
        }
    }
}