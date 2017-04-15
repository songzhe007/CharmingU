package zhesong.madcourse.neu.edu.charmingu;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class StepCountActivity extends AppCompatActivity {
    private float step_number;
    private TextView  Step_Textview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("My Steps");
        Intent intent = getIntent();
        step_number=intent.getFloatExtra("steps",-1);

        Step_Textview.setText(""+ step_number);

        Button button_return = (Button) findViewById(R.id.button_return);
        button_return.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(StepCountActivity.this, MainActivity.class));
            }
        });
    }
}
