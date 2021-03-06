package com.gamble.unopp.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
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

        // Get the text and image view from the rowView
        TextView txtPlayerName  = (TextView) rowView.findViewById(R.id.txtPlayerName);
        ImageView imageView = (ImageView) rowView.findViewById(R.id.ivPlayerBubble);

        // Set the text for textView
        txtPlayerName.setText(player.getName() +" ("+ player.getHand().size() +")");

        // Set image and only show if player has called uno
        imageView.setImageResource(R.mipmap.uno_player_bubble);
        if (player.isUno()) {
            imageView.setVisibility(View.VISIBLE);
        } else {
            imageView.setVisibility(View.INVISIBLE);
        }

        // highlight current player
        Player currentPlayer = player.getGameSession().getActualGameRound().getGamestate().getActualPlayer();

        if (currentPlayer.getID() == player.getID()) {
            rowView.setBackgroundColor(Color.WHITE);
        }

        // retrn rowView
        return rowView;
    }
}
