package com.gamble.unopp.fragments;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;

import com.gamble.unopp.R;
import com.gamble.unopp.model.game.Player;
import com.gamble.unopp.model.management.ICheatDialogListener;
import com.gamble.unopp.model.management.UnoDatabase;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Verena on 09.06.2015.
 */
public class CheatDialogFragment extends DialogFragment implements DialogInterface.OnClickListener, DialogInterface.OnDismissListener {

    private List<Player> players;
    private String[] playerNames;
    private Player selectedPlayer;
    private Map<Player, String> otherPlayers = new HashMap<>();

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        players = UnoDatabase.getInstance().getCurrentGameSession().getPlayers();

        for (int i = 0; i < players.size(); i++) {

            if (!players.get(i).equals(UnoDatabase.getInstance().getLocalPlayer())) {
                otherPlayers.put(players.get(i), players.get(i).getName());
            }
        }

        playerNames = otherPlayers.values().toArray(new String[players.size() - 1]);

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(R.string.cheatDialogMessage);
        builder.setItems(playerNames, this);
        builder.setNegativeButton(R.string.cancel, this);
        return builder.create();
    }

    @Override
    public void onClick(DialogInterface dialogInterface, int which) {

        if (which >= 0 && which < players.size() - 1) {

            String selectedPlayerName = playerNames[which];

            for (Map.Entry<Player, String> otherPlayerEntry : otherPlayers.entrySet()) {
                if (otherPlayerEntry.getValue().equals(selectedPlayerName)) {
                    selectedPlayer = otherPlayerEntry.getKey();
                }
            }

            if (getActivity() != null && getActivity() instanceof ICheatDialogListener) {

                ICheatDialogListener listener = (ICheatDialogListener) getActivity();
                listener.onDialogClosed(this);
            }
        }

        dialogInterface.cancel();
    }

    public Player getSelectedPlayer() {
        return selectedPlayer;
    }

    @Override
    public void onDismiss(final DialogInterface dialog) {
        super.onDismiss(dialog);
        final Activity activity = getActivity();

        if (activity instanceof DialogInterface.OnDismissListener) {
            ((DialogInterface.OnDismissListener) activity).onDismiss(dialog);
        }

        if (dialog != null) {
            dialog.cancel();
        }
    }
}