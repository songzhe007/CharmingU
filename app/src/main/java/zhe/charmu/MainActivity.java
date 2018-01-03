package zhe.charmu;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ToggleButton;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import zhe.charmu.model.DailyMetrics;
import zhe.charmu.model.NotificationUtil;
import zhe.charmu.budgetcontrol.BudgetControlActivity;
import zhe.charmu.bodymetrics.BodyMetricsActivity;
import zhe.charmu.database.DatabaseHelper;
import zhe.charmu.model.Globals;

public class MainActivity extends AppCompatActivity implements SensorEventListener {

    DatabaseHelper dbHelper;
    SQLiteDatabase dbRead;
    Globals g;

    private ToggleButton tg_btn;
    private SensorManager mSensorManager;
    private Sensor mStepDetector;
    private float step_number;
    private Button step_cnt_btn;

    private Thread thread;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
    }

    protected void onResume(){
        super.onResume();
    }

    private void init(){
        dbHelper = new DatabaseHelper(this);
        step_cnt_btn = (Button) findViewById(R.id.button_stepcount);
        tg_btn = (ToggleButton) findViewById(R.id.button_toggle);
        step_number = 0;
        initGlobals();
        stepCount();
    }

    private void initSaveStepCountThread(){
        thread=new Thread(new Runnable() {
            @Override
            public void run() {
                while(true){
                    try {
                        Thread.sleep(24*60*60*1000); //save steps into database once each day
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    System.out.println("Thread is called here !!!");
                    System.out.println(step_number);
                    dbHelper.setSteps(step_number,g.getIfTodaySet());
                    g.setToday();
                }
            }
        });

        thread.start();
    }

    private void initGlobals(){
        dbRead = dbHelper.getReadableDatabase();
        g = Globals.getInstance();
        g.setMonthMetrics(dbHelper.getMonthMetrics(dbRead));
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
        String today = sdf.format(new Date());
        ArrayList<DailyMetrics> list = dbHelper.getDailyMetrics(today,today,dbRead);
        if(!list.isEmpty()){
            g.setToday();
        }
        dbRead.close();
    }

    public void bodyMetricsActivity(View view) {
        Intent intent = new Intent(this, BodyMetricsActivity.class);
        startActivity(intent);
    }

    public void budgetControlActivity(View view) {
        Intent intent = new Intent(this, BudgetControlActivity.class);
        startActivity(intent);
    }

    public void stepCountActivity(View view) {
        Intent intent = new Intent(this, StepCountActivity.class);
        Bundle b = new Bundle();
        b.putFloat("steps", step_number);
        intent.putExtras(b);
        startActivity(intent);
    }

    public void googleMapActivity(View view) {
        Intent intent = new Intent(this, GoogleMapActivity.class);
        startActivity(intent);
    }


    private void stepCount() {
        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mStepDetector = mSensorManager.getDefaultSensor(Sensor.TYPE_STEP_DETECTOR);

        if (mStepDetector != null) {
            System.out.println("Success! There is step_counter sensor in this device!!!!");
            tg_btn.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

                @Override
                public void onCheckedChanged(CompoundButton buttonView,
                                             boolean isChecked) {

                    if (isChecked) {
                        registerSensorListener();
                        System.out.println("sensor on!!!!!!");
                    } else {
                        unregisterSensorListener();
                    }
                }

            });
            initSaveStepCountThread();
        } else {
            String title = "Step count function is not available!";
            String body = "Sorry! We failed to detect hardwares for step counting on your phone." +
                    "The related function is not available!";
            NotificationUtil.showDialog(title,body,MainActivity.this,false,null);
            tg_btn.setVisibility(View.GONE);
            tg_btn.setEnabled(false);
            step_cnt_btn.setVisibility(View.GONE);
            step_cnt_btn.setEnabled(false);
            System.out.println("Old Phone!!!");
        }
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        System.out.println(sensorEvent.sensor);
        if (sensorEvent.sensor == mStepDetector) {
            step_number = step_number+sensorEvent.values[0];
        } else {
            System.out.println("fail!");
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }

    public void registerSensorListener() {
        mSensorManager.registerListener(this, mStepDetector, SensorManager.SENSOR_DELAY_NORMAL);
    }

    public void unregisterSensorListener() {
        mSensorManager.unregisterListener(this);
    }

}
