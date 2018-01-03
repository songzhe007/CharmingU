package zhe.charmu.model;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by songz on 1/2/2018.
 */

public class DateUtil {

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
