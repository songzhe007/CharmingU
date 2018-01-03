package zhe.charmu.bodymetrics;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import zhe.charmu.model.CheckInputFormatUtil;
import zhe.charmu.metricstendency.ShowBodyMetricsTendency;
import zhe.charmu.database.DatabaseHelper;
import zhe.charmu.R;

public class ReviewBodyMetricsActivity extends AppCompatActivity {

    private final static String[] button_type = {"weight","BMI","step"};

    EditText et_from;
    EditText et_to;
    Button bt_review_weight;
    Button bt_review_bmi;
    Button bt_review_steps;
    DatabaseHelper dbHelper;
    String from_date;
    String to_date;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review_body_metrics);
        init();
    }

    private void init(){
        et_from = (EditText) findViewById(R.id.et_from);
        et_to = (EditText) findViewById(R.id.et_to);
        bt_review_weight = (Button) findViewById(R.id.bt_review_weight);
        bt_review_bmi = (Button) findViewById(R.id.bt_review_bmi);
        bt_review_steps = (Button) findViewById(R.id.bt_review_steps);
        dbHelper = new DatabaseHelper(this);
        from_date = "";
        to_date = "";
        init_review_button(bt_review_weight,button_type[0]);
        init_review_button(bt_review_bmi,button_type[1]);
        init_review_button(bt_review_steps,button_type[2]);

    }

    private void init_review_button(Button button, final String type){
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                from_date = et_from.getText().toString();
                to_date = et_to.getText().toString();
                Intent intent;
                switch(CheckInputFormatUtil.check_date_input(from_date,to_date,ReviewBodyMetricsActivity.this)){
                    case 0:
                        break;
                    case 1:
                        break;
                    case 2:
                        break;
                    case 3:
                        intent = new Intent(ReviewBodyMetricsActivity.this, ShowBodyMetricsSingle.class);
                        intent.putExtra("date", from_date);
                        intent.putExtra("type", type);
                        startActivity(intent);
                        break;
                    case 4:
                        intent = new Intent(ReviewBodyMetricsActivity.this, ShowBodyMetricsTendency.class);
                        intent.putExtra("from_date", from_date);
                        intent.putExtra("to_date", to_date);
                        intent.putExtra("type", type);
                        startActivity(intent);
                        break;
                }
            }
        });
    }

}
