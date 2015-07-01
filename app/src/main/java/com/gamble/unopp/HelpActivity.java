package com.gamble.unopp;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.Html;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;


public class HelpActivity extends ActionBarActivity {

    private TextView inputHelp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        super.onCreate(savedInstanceState);

        // remove title
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_help);

        // get controls
        inputHelp           = (TextView) findViewById(R.id.txtHelp);

        // display help text
        inputHelp.setText(Html.fromHtml(this.textContent));
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_help, menu);
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

    private String textContent = "<h1>Uno++ User Manuel</h1>\n" +
            "<h2>UNO++ Start</h2>\n" +
            "<p>Ein Spieler erstellt über die Lobby ein neues Spiel. Ab nun können alle Spieler die sich im erlaubten Umkreis befinden das Spiel finden und diesem beitreten. Maximale Spieleranzahl pro Spiel ist 6. Nachdem alle Spieler über die Lobby dem Spiel beigetreten sind, kann der Spielhost (Spieler der Spiel erstellt hat) das Spiel starten.</p>\n" +
            "<h2>Uno++ Spielverlauf</h2>\n" +
            "<p>Jeder Spieler bekommt zu Beginn genau sieben zufällig gewählte Karten vom Spielstapel der 108 Karten. Bevor das Spiel beginnt wird automatisch die erste Karte des Spielstapels gezogen und offene aufgelegt. Ist die Startkarte eine +4, so wird erneut eine Karte gezogen und aufgelegt. Ist die Startkarte eine Farbwahl, so wird zufällig eine Startfarbe ausgewählt und angezeigt. Ist die Startkarte eine +2, so muss der aktuelle Spieler 2 Karten abheben, außer dieser hat selbst eine +2 Karte auf seiner Spielhand. Bei allen anderen Karten ist nichts weiters zu beachten. Standardmäßig beginnt nun der Spielhost mit seinem Spielzug. Hat dieser Spieler eine passende Karte, so kann er diese legen. Eine passende Karte ist durch die folgenden Eigenschaften definiert:</p>\n" +
            "<p>Die Farbe ist ident zur Farbe der derzeit offen liegenden Karte.</p>\n" +
            "<p>Die Zahl ist ident zur Zahl der derzeit offen liegenden Karte, hierbei ist die Farbe egal.</p>\n" +
            "<p>Eine Richtungswechselkarte darf nur gelegt werden, wenn bereits eine derartige Karte liegt (hierbei ist die Farbe egal) oder die Farbe der offen liegenden Karte ident zur zuspielenden Karte ist. Der blaue Pfeil der die Spielrichtung vorgibt wird daraufhin angepasst.</p>\n" +
            "<p>Eine +4 darf immer gelegt werden. Wird diese gespielt, so öffnet sich ein Popup, indem eine Farbe ausgewählt werden muss.</p>\n" +
            "<p>Eine Farbwahl darf immer gelegt werden, sofern keine +2 offen liegt. Wird diese gespielt, so öffnet sich ein Popup, indem eine Farbe ausgewählt werden muss.</p>\n" +
            "<p>Eine +2 darf nur gelegt werden, wenn bereits eine +2 einer beliebigen Farbe offen liegt, oder die Farbe der offenen Karte zur Farbe der +2 passt. Liegt bereits eine +2 so wird der Zähler der zu hebenden Karten um 2 erhöht.</p>\n" +
            "<p>Hat der aktuelle Spieler keine passende Karte, so muss dieser eine Karte bzw. die vom Zähler berechnete Anzahl an Karten vom Stapel heben. Nach Legen oder Heben ist nun der nächste Spieler (siehe Reihenfolge, wird vom blauen Pfeil angezeigt) an der Reihe. Für diesen gilt derselbe Ablauf wie soeben für den Spielhost, jedoch kann nun auch eine +4 Karte gelegt worden sein. Liegt bereits eine +4 so wird der Zähler der zu hebenden Karten um 4 erhöht. Diese Schritte wiederholen sich nun solange bis ein Spieler keine Karten mehr hat.</p>\n" +
            "<h2>Uno++ Spielende</h2>\n" +
            "<p>Wichtig! Sobald ein Spieler nur mehr eine Karte übrig hat, so muss dieser sein Handy schütteln oder den UNO Button drücken. Für diese Ausführung hat der Spieler nur wenige Sekunden Zeit. Vergisst der Spieler UNO auszulösen, so muss dieser zwei Karten vom Stapel ziehen. Hat ein Spieler gewonnen, so werden alle Spieler automatisch in die Lobby zurückgeleitet und ein neues Spiel kann begonnen werden.</p>\n" +
            "<h2>Uno++ Schummeln:</h2>\n" +
            "<p>Während dem Spielablauf taucht immer wieder einmal zufällig irgendwo auf dem Bildschirm ein Avatar auf. Berühren Sie diesen schnell genug, so wir ein Popup angezeigt mit den Namen der Spieler im Spiel. Nun muss ein Spielername ausgewählt werden. Nach Auswahl wird dem Spieler eine beliebige Karte des ausgewählten Spielers angezeigt.</p>\n";
}
