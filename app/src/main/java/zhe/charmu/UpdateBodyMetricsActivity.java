package zhe.charmu;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import model.CheckInputFormatUtil;
import model.NotificationUtil;
import zhe.charmu.database.DatabaseHelper;

public class UpdateBodyMetricsActivity extends AppCompatActivity {

    DatabaseHelper dbHelper;
    Globals g;
    SQLiteDatabase writedb;
    EditText et_height;
    EditText et_weight;
    Button bt_submit_height;
    Button bt_submit_weight;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_body_metrics);
        init();
    }

    protected void onStop(){
        super.onStop();
        writedb.close();
    }

    private void init(){

        dbHelper = new DatabaseHelper(this);
        g = Globals.getInstance();
        writedb = dbHelper.getWritableDatabase();

        et_height = (EditText) findViewById(R.id.et_height);
        et_weight = (EditText) findViewById(R.id.et_weight);
        bt_submit_height = (Button) findViewById(R.id.bt_submit_height);
        bt_submit_weight = (Button) findViewById(R.id.bt_submit_weight);
        init_bt_height();
        init_bt_weight();
    }

    private void init_bt_height() {
        bt_submit_height.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String heightStr = et_height.getText().toString();
                float height = CheckInputFormatUtil.getInputFloatValue(heightStr,"height",UpdateBodyMetricsActivity.this);
                if(height > 0){
                    g.setHeight(height);
                    dbHelper.setHeight(height, writedb);
                    et_height.setText("");
                }
            }
        });
    }

    private void init_bt_weight() {
        bt_submit_weight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String weightStr = et_weight.getText().toString();
                float weight = CheckInputFormatUtil.getInputFloatValue(weightStr,"weight",UpdateBodyMetricsActivity.this);
                float height = g.getMonthMetrics().height;
                if(height <= 0){
                    String title = "Height has not been set yet!";
                    String body = "Please set your height at first!";
                    NotificationUtil.showDialog(title,body,UpdateBodyMetricsActivity.this,false,null);
                    et_weight.setText("");
                }else if(weight > 0){
                    dbHelper.setWeight(g.getMonthMetrics().height, weight, writedb, g.getIfTodaySet());
                    g.setToday();
                    et_weight.setText("");
                }
            }
        });
    }
}
