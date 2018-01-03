package zhe.charmu.budgetcontrol;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.Legend.LegendPosition;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.formatter.PercentFormatter;

import java.util.ArrayList;

import zhe.charmu.model.NotificationUtil;
import zhe.charmu.model.Globals;
import zhe.charmu.R;

import static android.graphics.Color.rgb;

public class ReviewBudgetDetail extends AppCompatActivity {

    Globals g;

    TextView tv_budget_title;
    TextView tv_remain_title;

    PieChart piechart;
    PieData pieData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review_budget_detail);
        init();
        set();
        drawPieChart(piechart, pieData);
        if(g.getMonthMetrics().budget == g.getMonthMetrics().remain){
            String title = "No food cost yet!";
            String body = "You haven't buy any food yet this month!";
            NotificationUtil.showDialog(title,body,ReviewBudgetDetail.this,true,null);
        }
    }

    private void init() {
        g = Globals.getInstance();
        tv_budget_title = (TextView) findViewById(R.id.tv_budget_title);
        tv_remain_title = (TextView) findViewById(R.id.tv_remain_title);
        piechart = (PieChart) findViewById(R.id.pie_chart);
        pieData = getPieData();
    }

    private void set() {
        tv_budget_title.setText("Your budget for this month: " + String.valueOf(g.getMonthMetrics().budget));
        tv_budget_title.setTextSize(18);
        tv_budget_title.setTypeface(tv_budget_title.getTypeface(), Typeface.BOLD_ITALIC);
        tv_budget_title.setTextColor(rgb(135,206,235));
        tv_remain_title.setText("Your remain for this month: " + String.valueOf(g.getMonthMetrics().remain));
        tv_remain_title.setTextSize(18);
        tv_remain_title.setTextColor(rgb(135,206,235));
        tv_remain_title.setTypeface(tv_remain_title.getTypeface(), Typeface.BOLD_ITALIC);
    }

    private void drawPieChart(PieChart pieChart, PieData pieData) {
        pieChart.setHoleRadius(30f);
        pieChart.setDescription("Food Cost Distribution");
        pieChart.setDescriptionTextSize(20f);
        pieChart.setDescriptionTypeface(Typeface.DEFAULT_BOLD);
        pieChart.setUsePercentValues(true);
        pieChart.setCenterTextTypeface(Typeface.DEFAULT_BOLD);
        pieChart.setCenterTextColor(Color.rgb(255, 165, 0));
        pieChart.setCenterTextSize(15f);
        pieChart.setData(pieData);
        Legend mLegend = pieChart.getLegend();
        mLegend.setPosition(LegendPosition.RIGHT_OF_CHART);
        mLegend.setXEntrySpace(7f);
        mLegend.setYEntrySpace(5f);
        mLegend.setTextSize(15f);
        mLegend.setTypeface(Typeface.DEFAULT_BOLD);
    }

    private PieData getPieData() {
        ArrayList<String> xValues = new ArrayList<>();
        xValues.add("Healthy Food");
        xValues.add("Junk Food");
        ArrayList<Entry> yValues = new ArrayList<>();
        yValues.add(new Entry(g.getMonthMetrics().healthy, 0));
        yValues.add(new Entry(g.getMonthMetrics().unhealthy, 1));
        PieDataSet pieDataSet = new PieDataSet(yValues, "");
        ArrayList<Integer> colors = new ArrayList<>();
        colors.add(rgb(94, 198, 57));
        colors.add(rgb(91, 57, 198));
        pieDataSet.setColors(colors);
        PieData pieData = new PieData(xValues, pieDataSet);
        pieData.setValueTypeface(Typeface.DEFAULT_BOLD);
        pieData.setValueFormatter(new PercentFormatter());
        pieData.setValueTextSize(18f);
        pieData.setValueTextColor(Color.rgb(255, 165, 0));
        return pieData;
    }
}
