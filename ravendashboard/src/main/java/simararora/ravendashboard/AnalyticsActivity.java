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
        findViewById(R.id.tv_popular_platform).setOnClickListener(this);
        findViewById(R.id.tv_connections_directed).setOnClickListener(this);
        findViewById(R.id.tv_connections_undirected).setOnClickListener(this);
        findViewById(R.id.tv_os).setOnClickListener(this);
        findViewById(R.id.tv_platform).setOnClickListener(this);
        findViewById(R.id.tv_broswer).setOnClickListener(this);
        findViewById(R.id.tv_user_agent).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()) {
            case R.id.tv_influencer:
                intent = new Intent(this, AnalyticsDataActivity.class);
                intent.putExtra(AnalyticsDataActivity.KEY_ANALYTICS_TYPE, AnalyticsDataActivity.KEY_REAL_INFLUENCER);
                startActivity(intent);
                break;
            case R.id.tv_popular_platform:
                intent = new Intent(this, AnalyticsDataActivity.class);
                intent.putExtra(AnalyticsDataActivity.KEY_ANALYTICS_TYPE, AnalyticsDataActivity.KEY_POPULAR_PLATFORM);
                startActivity(intent);
                break;
            case R.id.tv_connections_directed:
                intent = new Intent(this, AnalyticsDataActivity.class);
                intent.putExtra(AnalyticsDataActivity.KEY_ANALYTICS_TYPE, AnalyticsDataActivity.KEY_CONNECTIONS_DIRECTED);
                startActivity(intent);
                break;
            case R.id.tv_connections_undirected:
                intent = new Intent(this, AnalyticsDataActivity.class);
                intent.putExtra(AnalyticsDataActivity.KEY_ANALYTICS_TYPE, AnalyticsDataActivity.KEY_CONNECTIONS_UNDIRECTED);
                startActivity(intent);
                break;
            case R.id.tv_os:
                intent = new Intent(this, AnalyticsDataActivity.class);
                intent.putExtra(AnalyticsDataActivity.KEY_ANALYTICS_TYPE, AnalyticsDataActivity.KEY_OS);
                startActivity(intent);
                break;
            case R.id.tv_platform:
                intent = new Intent(this, AnalyticsDataActivity.class);
                intent.putExtra(AnalyticsDataActivity.KEY_ANALYTICS_TYPE, AnalyticsDataActivity.KEY_PLATFORM);
                startActivity(intent);
                break;
            case R.id.tv_broswer:
                intent = new Intent(this, AnalyticsDataActivity.class);
                intent.putExtra(AnalyticsDataActivity.KEY_ANALYTICS_TYPE, AnalyticsDataActivity.KEY_BROWSER);
                startActivity(intent);
                break;
            case R.id.tv_user_agent:
                intent = new Intent(this, AnalyticsDataActivity.class);
                intent.putExtra(AnalyticsDataActivity.KEY_ANALYTICS_TYPE, AnalyticsDataActivity.KEY_USER_AGENT);
                startActivity(intent);
                break;
        }
    }
}
