package zhe.charmu;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import java.util.LinkedList;

import model.CheckInputFormatUtil;
import model.Item;
import model.NotificationUtil;
import zhe.charmu.BudgetControl.ReviewBudgetDetail;
import zhe.charmu.database.DatabaseHelper;

public class RecordReceiptActivity extends AppCompatActivity {

    EditText et_item_name;
    EditText et_item_amount;
    EditText et_item_price;
    RadioGroup radio_group;
    RadioButton radio_healthy;
    RadioButton radio_unhealthy;
    Button bt_submit_item;
    Button bt_finish;
    LinkedList<Item> itemList;
    DatabaseHelper dbHelper;
    Globals g;
    SQLiteDatabase writedb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record_receipt);
        init();
        init_bt_submit_item();
        init_bt_finish();
    }

    protected void onStop(){
        super.onStop();
        writedb.close();
    }

    private void init(){
        et_item_name = (EditText) findViewById(R.id.et_item_name);
        et_item_amount = (EditText) findViewById(R.id.et_item_amount);
        et_item_price = (EditText) findViewById(R.id.et_item_price);
        radio_healthy = (RadioButton) findViewById(R.id.radio_healthy);
        radio_unhealthy = (RadioButton) findViewById(R.id.radio_unhealthy);
        radio_group = (RadioGroup) findViewById(R.id.radio_group);
        bt_submit_item = (Button) findViewById(R.id.bt_submit_item);
        bt_finish = (Button) findViewById(R.id.bt_finish);
        itemList = new LinkedList<> ();
        dbHelper = new DatabaseHelper(this);
        g = Globals.getInstance();
        writedb = dbHelper.getWritableDatabase();
    }

    private void init_bt_submit_item(){
        bt_submit_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = et_item_name.getText().toString();
                String amountStr = et_item_amount.getText().toString();
                String priceStr = et_item_price.getText().toString();
                float amount = CheckInputFormatUtil.getInputFloatValue(amountStr, "amount", RecordReceiptActivity.this);
                float price = CheckInputFormatUtil.getInputFloatValue(priceStr, "price", RecordReceiptActivity.this);
                boolean ifHealthy = radio_healthy.isChecked();
                boolean ifunhealthy = radio_unhealthy.isChecked();
                if(!(ifHealthy || ifunhealthy)){
                    String title = "Need more information!";
                    String body = "Please indicate if this item is healthy or junk food!";
                    NotificationUtil.showDialog(title,body,RecordReceiptActivity.this,false,null);
                }else if(amount > 0 && price > 0){
                    Item item = new Item(name, amount, ifHealthy, price);
                    itemList.addFirst(item);
                    et_item_name.setText("");
                    et_item_amount.setText("");
                    et_item_price.setText("");
                    radio_group.clearCheck();
                }
            }
        });
    }

    private void init_bt_finish(){
        bt_finish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                float[] newMetrics = calculateRemain();
                float newRemain = newMetrics[0];
                float newHealthy = newMetrics[1];
                float newUnhealthy = newMetrics[2];
                dbHelper.setRemain(newRemain, writedb);
                g.setRemain(newRemain);
                dbHelper.setHealthy(newHealthy, writedb);
                g.setHealthy(newHealthy);
                dbHelper.setUnhealthy(newUnhealthy, writedb);
                g.setUnhealthy(newUnhealthy);
                Intent intent = new Intent(RecordReceiptActivity.this, ReviewBudgetDetail.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private float[] calculateRemain(){
        float[] metrics = new float[3];
        float remain = g.getMonthMetrics().remain;
        float healthy = g.getMonthMetrics().healthy;
        float unhealthy = g.getMonthMetrics().unhealthy;
        while(!itemList.isEmpty()){
            Item curr = itemList.removeFirst();
            if(curr.ifHealthy){
                remain = remain-(curr.price*curr.amount)/3;
                healthy = healthy+(curr.price*curr.amount);
            }else{
                remain = remain-(curr.price*curr.amount)*3;
                unhealthy = unhealthy+(curr.price*curr.amount);
            }
        }
        metrics[0] = remain;
        metrics[1] = healthy;
        metrics[2] = unhealthy;
        return metrics;
    }
}
