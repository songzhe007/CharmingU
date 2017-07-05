package zhe.charmu;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final int DB_VERSION = 1;
    private static final String DB_NAME = "charmudb";
    public static final String MONTHLY_TABLE = "monthly_metrics";
    public static final String DAILY_TABLE = "daily_metrics";
    public static final String HEIGHT_COL = "height";
    public static final String BUDGET_COL = "budget";
    public static final String REMAIN_COL = "remain";
    public static final String HEALTHY_COL = "healthy";
    public static final String UNHEALTHY_COL = "unhealthy";
    public static final String BUDGET_SET_DATE_COL = "budget_set_date";
    public static final String DATE_COL = "date";
    public static final String WEIGHT_COL = "weight";
    public static final String BMI_COL = "BMI";
    public static final String STEP_COL = "step";

    public DatabaseHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        
        String sql_create_monthly_table =
                "CREATE TABLE "+ MONTHLY_TABLE + "(" +
                "_id INTEGER PRIMARY KEY AUTOINCREMENT,  " +
                        HEIGHT_COL + " REAL, " +
                        BUDGET_COL + " REAL, " +
                        REMAIN_COL + " REAL, " +
                        HEALTHY_COL + " REAL, " +
                        UNHEALTHY_COL + " REAL, " +
                        BUDGET_SET_DATE_COL + " REAL);";

        String sql_create_daily_table =
                "CREATE TABLE "+ DAILY_TABLE + "(" +
                "_id INTEGER PRIMARY KEY AUTOINCREMENT,  " +
                        DATE_COL + " TEXT, " +
                        WEIGHT_COL + " REAL, " +
                        BMI_COL + " REAL, " +
                        STEP_COL + " REAL);";

        db.execSQL(sql_create_monthly_table);
        db.execSQL(sql_create_daily_table);

        initMonthTable(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + MONTHLY_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + DAILY_TABLE);
        onCreate(db);
    }

    private void initMonthTable(SQLiteDatabase db){
        ContentValues cv = new ContentValues();
        cv.put(HEIGHT_COL, -1);
        cv.put(BUDGET_COL, -1);
        cv.put(REMAIN_COL, -1);
        cv.put(HEALTHY_COL, -1);
        cv.put(UNHEALTHY_COL, -1);
        cv.put(BUDGET_SET_DATE_COL, "1900.01.01.00.00.00");
        db.insertOrThrow(MONTHLY_TABLE, null, cv);
    }

    void setHeight(float newHeight, SQLiteDatabase db){
        ContentValues cv = new ContentValues();
        cv.put(HEIGHT_COL, newHeight);
        db.update(MONTHLY_TABLE, cv, "_id = 1", null);
    }

    void setRemain(float newRemain, SQLiteDatabase db){
        ContentValues cv = new ContentValues();
        cv.put(REMAIN_COL, newRemain);
        db.update(MONTHLY_TABLE, cv, "_id = 1", null);
    }

    void setHealthy(float newHealthy, SQLiteDatabase db){
        ContentValues cv = new ContentValues();
        cv.put(HEALTHY_COL, newHealthy);
        db.update(MONTHLY_TABLE, cv, "_id = 1", null);
    }

    void setUnhealthy(float newUnealthy, SQLiteDatabase db){
        ContentValues cv = new ContentValues();
        cv.put(UNHEALTHY_COL, newUnealthy);
        db.update(MONTHLY_TABLE, cv, "_id = 1", null);
    }

    void setBudget(float newBudget, SQLiteDatabase db){
        ContentValues cv = new ContentValues();
        cv.put(BUDGET_COL, newBudget);
        db.update(MONTHLY_TABLE, cv, "_id = 1", null);
    }

    void setDate(SQLiteDatabase db){
        ContentValues cv = new ContentValues();
        String todayStr = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new Date());
        cv.put(BUDGET_SET_DATE_COL, todayStr);
        db.update(MONTHLY_TABLE, cv, "_id = 1", null);
    }

    void setWeight(float height, float newWeight, SQLiteDatabase db, boolean ifTodaySet){
        ContentValues cv = new ContentValues();
        cv.put(WEIGHT_COL, newWeight);
        double newBMI =  newWeight/Math.pow(height,2);
        cv.put(BMI_COL, newBMI);
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
        String currDate = sdf.format(new Date());
        if(ifTodaySet){
            db.update(DAILY_TABLE, cv, "date = ?", new String[]{currDate});
        }else{
            cv.put(DATE_COL, currDate);
            db.insertOrThrow(DAILY_TABLE, null, cv);
        }
    }

    void setSteps(float newStep, boolean ifTodaySet){
        System.out.println("Set step is called here!!");
        SQLiteDatabase db = this.getWritableDatabase();
        System.out.println(newStep);
        ContentValues cv = new ContentValues();
        cv.put(STEP_COL, newStep);
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
        String currDate = sdf.format(new Date());
        if(ifTodaySet){
            System.out.println("Update is called here!!");
            int i = db.update(DAILY_TABLE, cv, "date = ?", new String[]{currDate});
            System.out.println(i);
        }else{
            System.out.println("Insert is called here!!");
            cv.put(DATE_COL, currDate);
            db.insertOrThrow(DAILY_TABLE, null, cv);
        }
        db.close();
    }

    MonthlyMetrics getMonthMetrics(SQLiteDatabase db){
        MonthlyMetrics monthMetrics = null;
        Cursor cursor = db.query(MONTHLY_TABLE,new String[] { "*" },
                null, null, null, null, null, null);
        if(cursor != null){
            if(cursor.moveToFirst()){
                float height = Float.parseFloat(cursor.getString(cursor.getColumnIndex(HEIGHT_COL)));
                float budget = Float.parseFloat(cursor.getString(cursor.getColumnIndex(BUDGET_COL)));
                float remain = Float.parseFloat(cursor.getString(cursor.getColumnIndex(REMAIN_COL)));
                float healthy = Float.parseFloat(cursor.getString(cursor.getColumnIndex(HEALTHY_COL)));
                float unhealthy = Float.parseFloat(cursor.getString(cursor.getColumnIndex(UNHEALTHY_COL)));
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss");
                Date budget_set_date = null;
                try{
                    budget_set_date = sdf.parse(cursor.getString(cursor.getColumnIndex(BUDGET_SET_DATE_COL)));
                }catch(ParseException e){
                    e.getStackTrace();
                }
                monthMetrics = new MonthlyMetrics(height,budget,remain,healthy,unhealthy, budget_set_date);
            }
            cursor.close();
        }
        return monthMetrics;
    }

    ArrayList<DailyMetrics> getDailyMetrics(String from_date, String to_date, SQLiteDatabase db){
        ArrayList<DailyMetrics> list = new ArrayList<>();
        String query = "SELECT * FROM daily_metrics WHERE date BETWEEN '"
                + from_date+"' AND '" + to_date+"' ";
        Cursor cursor=db.rawQuery(query, null);
        try{
            while(cursor.moveToNext()){
                int _id = 0;
                float weight = -1;
                float bmi = -1;
                float step = -1;
                String date = from_date;
                if(cursor.getString(cursor.getColumnIndex("_id")) != null){
                    _id = Integer.parseInt(cursor.getString(cursor.getColumnIndex("_id")));
                }
                if(cursor.getString(cursor.getColumnIndex(WEIGHT_COL)) != null){
                    weight = Float.parseFloat(cursor.getString(cursor.getColumnIndex(WEIGHT_COL)));
                }
                if(cursor.getString(cursor.getColumnIndex(BMI_COL)) != null){
                    bmi = Float.parseFloat(cursor.getString(cursor.getColumnIndex(BMI_COL)));
                }
                if(cursor.getString(cursor.getColumnIndex(STEP_COL)) != null){
                    step = Float.parseFloat(cursor.getString(cursor.getColumnIndex(STEP_COL)));
                }
                if(cursor.getString(cursor.getColumnIndex(DATE_COL)) != null){
                    date = cursor.getString(cursor.getColumnIndex(DATE_COL));
                }
                DailyMetrics tmp = new DailyMetrics(_id, date, weight,bmi,step);
                list.add(tmp);
            }
        }finally{
            cursor.close();
        }
        return list;
    }

}
