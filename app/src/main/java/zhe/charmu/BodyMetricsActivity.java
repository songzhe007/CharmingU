package zhe.charmu;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class BodyMetricsActivity extends AppCompatActivity {

    Globals g;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_body_metrics);
        g = Globals.getInstance();
    }

    public void startUpdateBodyMetricsActivity(View view){
        Intent intent = new Intent(this, UpdateBodyMetricsActivity.class);
        startActivity(intent);
    }

    public void startReviewBodyMetricsActivity(View view){
        Intent intent = new Intent(this, ReviewBodyMetricsActivity.class);
        startActivity(intent);
    }
}