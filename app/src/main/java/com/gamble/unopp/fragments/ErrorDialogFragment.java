package com.gamble.unopp.fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;

import com.gamble.unopp.R;
import com.gamble.unopp.connection.response.ResponseResult;
import com.gamble.unopp.helper.ErrorDialogTypes;
import com.gamble.unopp.model.management.IErrorDialogListener;

/**
 * Created by Verena on 15.05.2015.
 */
public class ErrorDialogFragment extends DialogFragment implements DialogInterface.OnClickListener {

    public static final String TYPE_IDENTIFIER      = "errorDialogType";

    private String type;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        String errorMessage = "error";
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            errorMessage = getArguments().getString("errorMessage");

            this.type   = getArguments().getString(TYPE_IDENTIFIER);
            if (this.type == null) {
                this.type = ErrorDialogTypes.DEFAULT;
            }
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

        if (getActivity() != null && getActivity() instanceof IErrorDialogListener) {

            IErrorDialogListener listener = (IErrorDialogListener) getActivity();
            listener.onDialogClosed(this);
        }
    }

    public String getType() {
        return type;
    }
}
