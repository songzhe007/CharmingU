package zhe.charmu;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import zhe.charmu.database.DatabaseHelper;

public class StepCountActivity extends AppCompatActivity{

    private DatabaseHelper dbHelper;
    SQLiteDatabase writedb;
    private TextView Step_Textview;
    private float step_number = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_step_count);
        init();
    }

    private void init(){
        dbHelper = new DatabaseHelper(this);
        Step_Textview = (TextView) findViewById(R.id.text_step);
        Bundle b = getIntent().getExtras();
        step_number = b.getFloat("steps");
        Step_Textview.setText(""+ step_number);
        writedb = dbHelper.getWritableDatabase();
    }

}
