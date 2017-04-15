package zhesong.madcourse.neu.edu.charmingu;

import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

public class MainActivity extends AppCompatActivity implements SensorEventListener{


        private ToggleButton tg_btn;
        private Button btn_step;
        private SensorManager mSensorManager;
        private Sensor mStepCounter;
        private Sensor mStepDetector;
        private Float step_number;
        private Button btn_map;
        private Button btn_map2;
        private Button btn_map3;


        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_main);

           // mTextView = (TextView) findViewById(R.id.tv_step_count);

            mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
            mStepCounter = mSensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);
            mStepDetector = mSensorManager.getDefaultSensor(Sensor.TYPE_STEP_DETECTOR);

            if(mStepDetector!=null){
                System.out.println("Success! There is step_counter sensor in this device!!!!");
            }
            else{
                System.out.println("Old Phone!!!");
            }


            tg_btn= (ToggleButton) findViewById(R.id.button_toggle);

            tg_btn.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener(){

                @Override
                public void onCheckedChanged(CompoundButton buttonView,
                                             boolean isChecked) {
                    // TODO Auto-generated method stub
//
                    if(isChecked){
                           registerSensorListener();
                        System.out.println("sensor on!!!!!!");
                    }else{
                        unregisterSensorListener();
                    }
                }

            });

          btn_step = (Button) findViewById(R.id.button_step);
          btn_step.setOnClickListener(new View.OnClickListener() {
              @Override
              public void onClick(View v) {
                  Intent intent = new Intent();
                  intent.setClass(MainActivity.this, StepCountActivity.class);
                  System.out.println(step_number);
                  intent.putExtra("steps",step_number);
                  startActivity(intent);

              }

          });

         btn_map =(Button) findViewById(R.id.button_map);
            btn_map.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    Intent intent=new Intent();
                    intent.setClass(MainActivity.this, MapsActivity.class);
                    startActivity(intent);
                }
            });

            btn_map2 =(Button) findViewById(R.id.button_map2);
            btn_map2.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    Intent intent=new Intent();
                    intent.setClass(MainActivity.this, MyLocationDemoActivity.class);
                    startActivity(intent);
                }
            });

            btn_map3 =(Button) findViewById(R.id.button_map3);
            btn_map3.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    Intent intent=new Intent();
                    intent.setClass(MainActivity.this, ShowLocationActivity.class);
                    startActivity(intent);
                }
            });


        }

        @Override
        protected void onResume() {
            super.onResume();

        }




    //private SensorEventListener mSensorEventListener = new SensorEventListener() {
        @Override
        public void onSensorChanged(SensorEvent sensorEvent) {
            System.out.println(sensorEvent.sensor);
            if (sensorEvent.sensor == mStepCounter) {
                step_number = sensorEvent.values[0];
            } else if (sensorEvent.sensor == mStepDetector) {
                // one more step when the foot hit the ground.
                Toast.makeText(MainActivity.this, "one more step", Toast.LENGTH_SHORT).show();
            } else {
                System.out.println("fail!");
            }
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int i) {

        }
    //};


    public void registerSensorListener() {
        mSensorManager.registerListener(this, mStepCounter, SensorManager.SENSOR_DELAY_NORMAL);
        mSensorManager.registerListener(this, mStepDetector, SensorManager.SENSOR_DELAY_NORMAL);
    }

    public void unregisterSensorListener() {
        mSensorManager.unregisterListener(this);
    }

}
