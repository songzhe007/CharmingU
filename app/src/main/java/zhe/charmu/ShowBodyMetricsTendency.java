package zhe.charmu;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

import java.util.ArrayList;
import java.util.List;

import model.DailyMetrics;
import model.NotificationUtil;
import zhe.charmu.database.DatabaseHelper;

import static android.graphics.Color.rgb;

public class ShowBodyMetricsTendency extends AppCompatActivity {

    DatabaseHelper dbHelper;
    String from_date;
    String to_date;
    String type;
    ArrayList<DailyMetrics> metricList;

    TextView tv_title;
    LineChart mLineChart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_body_metrics_tendency);
        init();
        if(metricList.size() == 0){
            String title = "No data has been collected between "+from_date+" and "+to_date+" !";
            String body = "Please check the input range of dates !";
            NotificationUtil.showDialog(title,body,ShowBodyMetricsTendency.this,true,null);
        }else{
            initTitle();
            initLineChart();
        }
    }

    private void init(){
        Bundle b = getIntent().getExtras();
        from_date = b.getString("from_date");
        to_date = b.getString("to_date");
        type = b.getString("type");
        dbHelper = new DatabaseHelper(this);
        SQLiteDatabase dbRead = dbHelper.getReadableDatabase();
        metricList = dbHelper.getDailyMetrics(from_date,to_date,dbRead);
        dbRead.close();
        tv_title = (TextView) findViewById(R.id.tv_tendency_title);
        mLineChart = (LineChart) findViewById(R.id.lineChart);
    }

    private void initTitle(){
        String title = "See your "+type+" tendency between "+from_date+" and "+to_date+" below:";
        tv_title.setText(title);
        tv_title.setTypeface(Typeface.DEFAULT_BOLD);
        tv_title.setTextColor(rgb(50,205,50));
    }

    private void initLineChart(){
        List<Entry> yList = new ArrayList<>();
        final List<String> dateList = new ArrayList<>();
        for(int i = 0; i < metricList.size(); i++){
            if(type.equals("weight") && metricList.get(i).weight > 0){
                yList.add(new Entry(metricList.get(i).weight,i));
            }else if(type.equals("BMI") && metricList.get(i).bmi > 0){
                yList.add(new Entry(metricList.get(i).bmi,i));
            }else if(type.equals("step") && metricList.get(i).step > 0){
                yList.add(new Entry(metricList.get(i).step,i));
            }
            dateList.add(metricList.get(i).date);
        }
        if(yList.isEmpty()){
            String title = "No related data has been collected between "+from_date+" and "+to_date+" !";
            String body = "Please check the input range of dates !";
            NotificationUtil.showDialog(title,body,ShowBodyMetricsTendency.this,true,null);
        }else{
            XAxis xAxis = mLineChart.getXAxis();
            xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
            xAxis.setTextSize(12f);
            xAxis.setDrawAxisLine(true);
            xAxis.setDrawGridLines(false);
            mLineChart.setDescription(type+" tendency");
            mLineChart.setDescriptionTextSize(25f);
            mLineChart.setDescriptionTypeface(Typeface.DEFAULT_BOLD);
            mLineChart.setDescriptionColor(rgb(138,43,226));
            mLineChart.setDescriptionPosition(860,100);
            mLineChart.getAxisLeft().setTextSize(12f);
            mLineChart.getAxisRight().setTextSize(12f);
            mLineChart.getLegend().setTextSize(15f);
            mLineChart.getLegend().setTypeface(Typeface.DEFAULT_BOLD);
            mLineChart.getLegend().setPosition(Legend.LegendPosition.BELOW_CHART_CENTER);
            LineDataSet ySet = new LineDataSet(yList, type);
            ySet.setLineWidth(2f);
            ySet.setColor(rgb(0,191,255));
            ySet.setCircleRadius(5f);
            ySet.setCircleHoleRadius(2.5f);
            ySet.setCircleColor(rgb(138,43,226));
            ySet.setValueTextSize(12f);
            LineData data = new LineData(dateList,ySet);
            mLineChart.setData(data);
            mLineChart.setBackgroundColor(rgb(230,230,250));
        }

    }

}
