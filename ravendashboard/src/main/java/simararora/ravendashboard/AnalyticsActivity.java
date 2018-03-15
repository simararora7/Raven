package simararora.ravendashboard;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import simararora.ravendashboard.details.AnalyticsDataActivity;

/**
 * Created by nateshrelhan on 3/16/18.
 */

public class AnalyticsActivity extends BaseAppCompatActivity implements View.OnClickListener {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_analytics);
        setUpActionBar("Analytics", true);
        findViewById(R.id.tv_influencer).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_influencer:
                Intent intentRealInfluencer = new Intent(this, AnalyticsDataActivity.class);
                intentRealInfluencer.putExtra(AnalyticsDataActivity.KEY_ANALYTICS_TYPE, AnalyticsDataActivity.KEY_REAL_INFLUENCER);
                startActivity(intentRealInfluencer);
                break;
        }
    }
}
