package com.example.admin.labs.models;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

/**
 * Created by admin on 18.09.2014.
 */
public class AlertDialogHelper {
    public static void showAlertView(Context context,String error) {

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Сообщение")
                .setMessage(error)
                .setNegativeButton("Ок",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });
        AlertDialog alert = builder.create();
        alert.show();
    }
}
