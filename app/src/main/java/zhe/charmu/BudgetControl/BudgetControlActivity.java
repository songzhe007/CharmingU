package zhe.charmu.budgetcontrol;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import zhe.charmu.model.NotificationUtil;
import zhe.charmu.model.Globals;
import zhe.charmu.metricstendency.RecordReceiptActivity;
import zhe.charmu.database.DatabaseHelper;
import zhe.charmu.R;

public class BudgetControlActivity extends AppCompatActivity {

    DatabaseHelper dbHelper;
    Globals g;
    //AlertDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_budget_control);
        init();
    }

    private void init(){
        dbHelper = new DatabaseHelper(this);
        g = Globals.getInstance();
        if(g.getMonthMetrics().budget < 0){
            String title = "Budget is not set yet!";
            String body = "Please set budget at first!";
            NotificationUtil.showDialog(title,body,BudgetControlActivity.this,false,SetBudgetActivity.class);
            //showDialog(title,body,);
        }
    }

    public void setBudgetActivity(View view) {
        Intent intent = new Intent(this, SetBudgetActivity.class);
        startActivity(intent);
    }

    public void recordReceiptActivity(View view) {
        Intent intent = new Intent(this, RecordReceiptActivity.class);
        startActivity(intent);
    }

    public void reviewBudgetDetail(View view){
        Intent intent = new Intent(this, ReviewBudgetDetail.class);
        startActivity(intent);

    }

//    private void showDialog(String title, String body){
//        generateDialog(title, body);
//        dialog.show();
//    }
//
//    private void generateDialog(String title, String body){
//
//        dialog = new AlertDialog.Builder(BudgetControlActivity.this)
//                .setTitle(title).setPositiveButton("Got it!", new DialogInterface.OnClickListener() {
//
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        dialog.dismiss();
//                        Intent intent = new Intent(BudgetControlActivity.this, SetBudgetActivity.class);
//                        startActivity(intent);
//                    }
//                }).setMessage(body).create();
//
//    }
}
