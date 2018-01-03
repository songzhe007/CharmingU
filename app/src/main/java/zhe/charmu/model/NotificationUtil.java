package zhe.charmu.model;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.app.AlertDialog;
import android.view.Gravity;
import android.widget.Button;
import android.widget.TextView;

import static android.graphics.Color.rgb;
import zhe.charmu.R;

/**
 * Created by songz on 1/2/2018.
 */

public class NotificationUtil {
    public static void showDialog(String title, String body, final Activity act, boolean ifFinishAct,
                                  final Class to_act){
        final AlertDialog dialog = generateDialog(title,body,act,ifFinishAct,to_act);
        dialog.setOnShowListener( new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface arg0) {
                Button positiveButton = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
                positiveButton.setGravity(Gravity.CENTER_HORIZONTAL);
                positiveButton.setTextColor(rgb(30,144,255));
                positiveButton.setTextSize(18);
                positiveButton.setTypeface(positiveButton.getTypeface(), Typeface.BOLD_ITALIC);
            }
        });

        dialog.show();
    }


    private static AlertDialog generateDialog(String title, String body, final Activity act,
                                              final boolean ifFinishAct, final Class to_act){

        TextView titleView = new TextView(act);
        titleView.setText('\n'+title);
        titleView.setTextSize(18);
        titleView.setTextColor(rgb(75,0,130));
        titleView.setGravity(Gravity.CENTER_HORIZONTAL);
        titleView.setTypeface(Typeface.DEFAULT_BOLD);

        TextView messageView = new TextView(act);
        messageView.setText('\n'+body);
        messageView.setTextSize(18);
        messageView.setTextColor(rgb(75,0,130));
        messageView.setGravity(Gravity.CENTER_HORIZONTAL);

        AlertDialog.Builder alert = new AlertDialog.Builder(act, R.style.DialogStyle);
        alert.setCustomTitle(titleView);
        alert.setPositiveButton("Got it!", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                if(ifFinishAct){
                    act.finish();
                }else if(to_act != null){
                    Intent intent = new Intent(act, to_act);
                    act.startActivity(intent);
                }
            }
        });
        alert.setView(messageView);
        AlertDialog dialog = alert.create();
        return dialog;
    }

}
