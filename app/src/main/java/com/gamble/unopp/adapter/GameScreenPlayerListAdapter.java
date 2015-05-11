package com.gamble.unopp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.gamble.unopp.R;
import com.gamble.unopp.model.game.Player;

import java.util.ArrayList;

/**
 * Created by Mario on 08.05.2015.
 */
public class GameScreenPlayerListAdapter extends ArrayAdapter<Player> {

    private final Context context;
    private final ArrayList<Player> itemsArrayList;

    public GameScreenPlayerListAdapter(Context context, ArrayList<Player> itemsArrayList) {

        super(context, R.layout.game_screen_player_row, itemsArrayList);

        this.context = context;
        this.itemsArrayList = itemsArrayList;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        Player player = itemsArrayList.get(position);

        // Create inflater
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        // Get rowView from inflater
        View rowView = inflater.inflate(R.layout.game_screen_player_row, parent, false);

        // Get the text view from the rowView
        TextView txtPlayerName  = (TextView) rowView.findViewById(R.id.txtPlayerName);

        // Set the text for textView
        txtPlayerName.setText(player.getName() +" ("+ player.getHand().size() +")");

        /*
        // highlight current player
        Player currentPlayer = player.getGameSession().getActualGameRound().getGamestate().getActualPlayer();

        if (currentPlayer.getID() == player.getID()) {
            rowView.setBackgroundColor(Color.WHITE);
        }
        */

        // HACK
        if (player.getID() == 3) {
            rowView.setBackgroundColor(context.getResources().getColor(R.color.selected_player_back));
        }
        // END HACK

        // retrn rowView
        return rowView;
    }
}
