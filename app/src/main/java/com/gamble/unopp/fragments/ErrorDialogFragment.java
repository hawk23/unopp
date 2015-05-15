package com.gamble.unopp.fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;

import com.gamble.unopp.R;
import com.gamble.unopp.connection.response.ResponseResult;

/**
 * Created by Verena on 15.05.2015.
 */
public class ErrorDialogFragment extends DialogFragment implements DialogInterface.OnClickListener {

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        String errorMessage = "error";
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            errorMessage = getArguments().getString("errorMessage");
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(R.string.error);
        builder.setMessage(errorMessage);
        builder.setPositiveButton("Ok", this);
        return builder.create();
    }

    @Override
    public void onClick(DialogInterface dialogInterface, int i) {
        dialogInterface.cancel();
    }
}
