package simararora.ravendashboard;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView tvResource, tvSource;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tvResource = findViewById(R.id.tv_resource);
        tvSource = findViewById(R.id.tv_source);
        tvResource.setOnClickListener(this);
        tvSource.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_resource:
                Intent intentResource = new Intent(this,ResourceActivity.class);
                startActivity(intentResource);
                break;
            case R.id.tv_source:
                Intent intentSource = new Intent(this,SourceActivity.class);
                startActivity(intentSource);
                break;
        }
    }
}
