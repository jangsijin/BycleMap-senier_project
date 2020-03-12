package com.example.coha.google.login;


import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

import com.example.coha.google.R;

/**
 * Created by Samchan on 2016-12-05.
 */

public class AlertDialogManager {
    /**
     * 다이얼로그 부분
     * */
    public void showAlertDialog(Context context, String title, String message,
                                Boolean status) {
        AlertDialog alertDialog = new AlertDialog.Builder(context).create();
        alertDialog.setTitle(title);
        alertDialog.setMessage(message);

        if(status != null)
            alertDialog.setIcon((status) ? R.mipmap.ic_launcher : R.mipmap.ic_launcher);


        alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
            }
        });

        // Showing Alert Message
        alertDialog.show();
    }

}
