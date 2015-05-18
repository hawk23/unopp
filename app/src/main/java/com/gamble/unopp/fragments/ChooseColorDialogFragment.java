package com.gamble.unopp.fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;

import com.gamble.unopp.GameScreenActivity;
import com.gamble.unopp.R;
import com.gamble.unopp.model.cards.UnoColor;
import com.gamble.unopp.model.game.Player;
import com.gamble.unopp.model.game.Turn;
import com.gamble.unopp.model.management.UnoDatabase;

import java.util.logging.Logger;

/**
 * Created by Mario on 05.05.2015.
 */
public class ChooseColorDialogFragment extends DialogFragment {

    private String[] colors = new String[]{"Rot", "Grün", "Blau", "Gelb"};

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(R.string.pick_color)
                .setItems(colors, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // The 'which' argument contains the index position
                        // of the selected item

                        if (which >= 0 && which < colors.length) {
                            /*
                            Turn turn = new Turn(Turn.TurnType.CHOOSE_COLOR);
                            turn.setColor(UnoColor.valueOf(colors[which]));
                            UnoDatabase.getInstance().getCurrentGameSession().getActualGameRound().doTurn(turn);
                            */
                        } else {
                            System.out.println("Unopp: ChooseColorDialogFragment has invalid index: " + which);
                        }
                    }
                });
        return builder.create();
    }
}
