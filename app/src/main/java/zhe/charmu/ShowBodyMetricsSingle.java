package zhe.charmu;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.util.ArrayList;

import model.DailyMetrics;
import model.NotificationUtil;
import zhe.charmu.database.DatabaseHelper;

public class ShowBodyMetricsSingle extends AppCompatActivity {

    String type;
    String date;
    DatabaseHelper dbHelper;
    ArrayList<DailyMetrics> metricList;
    TextView tv_show;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_body_metrics_single);
        init();
        if(metricList.size() == 0){
            String title = "No data has been collected on "+date+" !";
            String body = "Please check the input date !";
            NotificationUtil.showDialog(title,body,ShowBodyMetricsSingle.this,true,null);
        }else{
            set();
        }
    }

    private void init(){
        Bundle b = getIntent().getExtras();
        type = b.getString("type");
        date = b.getString("date");
        dbHelper = new DatabaseHelper(this);
        SQLiteDatabase dbRead = dbHelper.getReadableDatabase();
        metricList = dbHelper.getDailyMetrics(date,date,dbRead);
        dbRead.close();
        tv_show = (TextView) findViewById(R.id.tv_show);
    }

    private void set(){
        String value = "";
        String unit = "";
        if(type.equals("weight")){
            value = String.valueOf(metricList.get(0).weight);
            unit = "kg";
        }else if(type.equals("BMI")){
            DecimalFormat formatter = new DecimalFormat("#0.00");
            value = formatter.format(metricList.get(0).bmi);
            //value = String.valueOf(metricList.get(0).bmi);
            unit = "";
        }else if(type.equals("step")){
            value = String.valueOf(metricList.get(0).step);
        }
        if(Float.parseFloat(value) < 0){
            String title = "No related data has been collected on "+date+" !";
            String body = "Please check the input date !";
            NotificationUtil.showDialog(title,body,ShowBodyMetricsSingle.this,true,null);
        }else{
            String text = "Your "+type+" on "+date+" is "+value+unit+"!";
            tv_show.setText(text);
        }
    }
}
