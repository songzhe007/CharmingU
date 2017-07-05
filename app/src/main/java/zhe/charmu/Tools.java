package zhe.charmu;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.app.AlertDialog;
import android.view.Gravity;
import android.widget.Button;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static android.graphics.Color.rgb;

class CheckInputFormatUtil{

    public static float getInputFloatValue(String inputStr, String fieldName, Activity act){
        float value = -1;
        if(inputStr.equals("")){
            String title = "Input Field should not be empty!";
            String body = "Please enter the value of "+fieldName;
            NotificationUtil.showDialog(title, body, act, false, null);
        }else{
            try{
                value = Float.parseFloat(inputStr);
                if(value <= 0){
                    String title = "Illegal input field!";
                    String body = "Illegal input value of "+fieldName+" should be non-negative!";
                    NotificationUtil.showDialog(title, body, act, false, null);
                }
            }catch(Exception e){
                String title = "Illegal input field!";
                String body = "Illegal input format of "+fieldName;
                NotificationUtil.showDialog(title, body, act, false, null);
            }
        }
        return value;
    }

    public static int check_date_input(String from_date, String to_date, Activity act){
        if(from_date.equals("") || to_date.equals("")){
            String title = "Please enter start and end dates !";
            String body = "You missed either start or end date, please fill them!";
            NotificationUtil.showDialog(title, body, act, false, null);
            return 0;
        }else if(!from_to_dates_legal(from_date,to_date)){
            String title = "Illegal date format !";
            String body = "Dates should be in format as MM/dd/yyyy! Please check your format and correct!";
            NotificationUtil.showDialog(title, body, act, false, null);
            return 1;
        }else if(DateUtil.compareDateString(from_date,to_date) > 0){
            String title = "End date is in front of start date ?";
            String body = "Please check and correct your input dates, because your end date is in front of your start date!";
            NotificationUtil.showDialog(title, body, act, false, null);
            return 2;
        }else if(DateUtil.compareDateString(from_date,to_date) == 0){
            return 3;
        }else{
            return 4;
        }
    }

    private static boolean from_to_dates_legal(String datestr1, String datestr2){
        boolean ifLengthLegal;
        boolean ifSlashLegal;
        boolean ifDayLegal;
        boolean ifMonthLegal;
        boolean ifYearLegal;

        ifLengthLegal = checkFormatLength(datestr1) && checkFormatLength(datestr2);
        if(!ifLengthLegal){
            return false;
        }
        ifSlashLegal = checkFormatSlash(datestr1) && checkFormatSlash(datestr2);
        if(!ifSlashLegal){
            return false;
        }
        ifMonthLegal = checkFormatMonth(datestr1) && checkFormatMonth(datestr2);
        if(!ifMonthLegal){
            return false;
        }
        ifDayLegal = checkFormatDay(datestr1) && checkFormatDay(datestr2);
        if(!ifDayLegal){
            return false;
        }
        ifYearLegal = checkFormatYear(datestr1) && checkFormatYear(datestr2);
        return ifYearLegal;
    }

    private static boolean checkFormatLength(String datestr){
        System.out.println("length check:");
        System.out.println(datestr.length() == 10);
        return datestr.length() == 10;
    }

    private static boolean checkFormatSlash(String datestr){
        System.out.println("slash check:");
        System.out.println(datestr.charAt(2) == '/' && datestr.charAt(5) == '/');
        return datestr.charAt(2) == '/' && datestr.charAt(5) == '/';
    }

    private static boolean checkFormatMonth(String datestr){
        int day = Integer.parseInt(datestr.substring(0,2));
        System.out.println("month check:");
        System.out.println(day >= 1 && day <= 12);
        return day >= 1 && day <= 12;
    }

    private static boolean checkFormatDay(String datestr){
        int month = Integer.parseInt(datestr.substring(3,5));
        System.out.println("day check:");
        System.out.println(month >= 1 && month <= 31);
        return month >= 1 && month <= 31;
    }

    private static boolean checkFormatYear(String datestr){
        int year = Integer.parseInt(datestr.substring(6,10));
        System.out.println("year check:");
        System.out.println(year >= 2000 && year <= 2100);
        return year >= 2000 && year <= 2100;
    }


}

class NotificationUtil{

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
                positiveButton.setTypeface(positiveButton.getTypeface(),Typeface.BOLD_ITALIC);
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

class DateUtil{

    public static double getDateDiff(Date date1, Date date2){
        long timeDiff = Math.abs(date2.getTime() - date1.getTime());
        return Math.ceil(timeDiff / (1000 * 3600 * 24));
    }

    public static int compareDateString(String datestr1, String datestr2){
        int result = 1;
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
        try{
            Date date1 = sdf.parse(datestr1);
            Date date2 = sdf.parse(datestr2);
            result = date1.compareTo(date2);
        }catch(ParseException e){
            e.getStackTrace();
        }
        return result;
    }

}
