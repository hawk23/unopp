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
import com.gamble.unopp.model.management.IChooseColorDialogListener;
import com.gamble.unopp.model.management.UnoDatabase;

import java.util.logging.Logger;

/**
 * Created by Mario on 05.05.2015.
 */
public class ChooseColorDialogFragment extends DialogFragment implements DialogInterface.OnClickListener {

    private String[] colors = new String[]{"Rot", "Grün", "Blau", "Gelb"};
    private int selectedIndex;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(R.string.pick_color).setItems(colors, this);
        return builder.create();
    }

    public UnoColor getColor () {

        switch (selectedIndex) {
            case 0:
                return UnoColor.RED;
            case 1:
                return UnoColor.GREEN;
            case 2:
                return UnoColor.BLUE;
            case 3:
                return UnoColor.YELLOW;
        }

        return null;
    }

    @Override
    public void onClick(DialogInterface dialogInterface, int which) {
        dialogInterface.cancel();

        if (which >= 0 && which < colors.length) {

            selectedIndex = which;

            if (getActivity() != null && getActivity() instanceof IChooseColorDialogListener) {

                IChooseColorDialogListener listener = (IChooseColorDialogListener) getActivity();
                listener.onDialogClosed(this);
            }
        } else {
            System.out.println("Unopp: ChooseColorDialogFragment has invalid index: " + which);
        }
    }
}
