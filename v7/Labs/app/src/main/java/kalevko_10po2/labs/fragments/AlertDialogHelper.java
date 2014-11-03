package kalevko_10po2.labs.fragments;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

/**
 * Created by admin on 03.11.2014.
 */
public class AlertDialogHelper {
    public static void showAlertView(Context context, String s) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Сообщение")
                .setMessage(s)
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
