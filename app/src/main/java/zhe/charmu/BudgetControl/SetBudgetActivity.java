package zhe.charmu.BudgetControl;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.util.Date;

import model.CheckInputFormatUtil;
import model.DateUtil;
import model.NotificationUtil;
import zhe.charmu.Globals;
import zhe.charmu.database.DatabaseHelper;

public class SetBudgetActivity extends AppCompatActivity {

    EditText et_budget;
    Button bt_submit_budget;
    DatabaseHelper dbHelper;
    Globals g;
    SQLiteDatabase writedb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_budget);
        init();
    }

    protected void onStop(){
        super.onStop();
        writedb.close();
    }

    private void init(){
        dbHelper = new DatabaseHelper(this);
        writedb = dbHelper.getWritableDatabase();
        g = Globals.getInstance();
        et_budget = (EditText) findViewById(R.id.et_budget);
        bt_submit_budget = (Button) findViewById(R.id.bt_submit_budget);
        init_bt_submit_budget();
    }

    private void init_bt_submit_budget(){
        bt_submit_budget.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(DateUtil.getDateDiff(g.getMonthMetrics().budget_set_date,new Date()) < 30){
                    String title = "Unable to set budget today!";
                    String body = "Sorry! You are only allowed to set your budget once per month";
                    NotificationUtil.showDialog(title, body, SetBudgetActivity.this, true, null);
                }else{
                    String budgetstr = et_budget.getText().toString();
                    float newBudget = CheckInputFormatUtil.getInputFloatValue(budgetstr,"budget",SetBudgetActivity.this);
                    if(newBudget > 0){
                        dbHelper.setBudget(newBudget, writedb);
                        dbHelper.setRemain(newBudget, writedb);
                        dbHelper.setHealthy(0, writedb);
                        dbHelper.setUnhealthy(0, writedb);
                        dbHelper.setDate(writedb);
                        g.setBudget(newBudget);
                        g.setRemain(newBudget);
                        g.setHealthy(0);
                        g.setUnhealthy(0);
                        g.setDate();
                        et_budget.setText("");
                    }
                }
            }
        });
    }
}
