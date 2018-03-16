package simararora.ravendashboard.details;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import simararora.ravendashboard.BaseAppCompatActivity;
import simararora.ravendashboard.R;
import simararora.ravendashboard.adapter.AnalyticsDataAdapter;
import simararora.ravendashboard.model.Analytics;
import simararora.ravendashboard.queries.ConnecteionsUndirectedQuery;
import simararora.ravendashboard.queries.ConnectionsDirectedQuery;
import simararora.ravendashboard.queries.InfluencerQuery;
import simararora.ravendashboard.queries.PopularPlatformQuery;
import simararora.ravendashboard.queries.Query;

/**
 * Created by nateshrelhan on 3/15/18.
 */

public class AnalyticsDataActivity extends BaseAppCompatActivity {
    public static final String KEY_ANALYTICS_TYPE = "analytics_type_";
    public static final String KEY_REAL_INFLUENCER = "real_influencer";
    public static final String KEY_POPULAR_PLATFORM = "popular_platform";
    public static final String KEY_CONNECTIONS_DIRECTED = "connections_directed";
    public static final String KEY_CONNECTIONS_UNDIRECTED = "connections_undirected";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listing);
        final RecyclerView rvResourceHistory = findViewById(R.id.rv_data);
        rvResourceHistory.setLayoutManager(new LinearLayoutManager(this));
        String source = getIntent().getStringExtra(KEY_ANALYTICS_TYPE);
        switch (source) {
            case KEY_REAL_INFLUENCER:
                setUpActionBar("Real Influencer", true);
                new InfluencerQuery().execute(new Query.QueryCompleteListener<Map<String, Integer>>() {
                    @Override
                    public void onSuccess(Map<String, Integer> result) {
                        List<Analytics> analyticsList = new ArrayList<>();
                        if (result.size() != 0) {
                            for (Map.Entry<String, Integer> entry : result.entrySet())
                                analyticsList.add(new Analytics(entry.getKey(), String.valueOf(entry.getValue())));
                        }
                        rvResourceHistory.setAdapter(new AnalyticsDataAdapter(analyticsList));
                    }

                    @Override
                    public void onFailure() {

                    }
                });
                break;
            case KEY_POPULAR_PLATFORM:
                setUpActionBar("Popular Platform", true);
                new PopularPlatformQuery().execute(new Query.QueryCompleteListener<Map<String, Integer>>() {
                    @Override
                    public void onSuccess(Map<String, Integer> result) {
                        List<Analytics> analyticsList = new ArrayList<>();
                        if (result.size() != 0) {
                            for (Map.Entry<String, Integer> entry : result.entrySet())
                                analyticsList.add(new Analytics(entry.getKey(), String.valueOf(entry.getValue())));
                        }
                        rvResourceHistory.setAdapter(new AnalyticsDataAdapter(analyticsList));
                    }

                    @Override
                    public void onFailure() {

                    }
                });
                break;
            case KEY_CONNECTIONS_DIRECTED:
                setUpActionBar("Connections (Directed)", true);
                new ConnectionsDirectedQuery().execute(new Query.QueryCompleteListener<Map<String, Integer>>() {
                    @Override
                    public void onSuccess(Map<String, Integer> result) {
                        List<Analytics> analyticsList = new ArrayList<>();
                        if (result.size() != 0) {
                            for (Map.Entry<String, Integer> entry : result.entrySet())
                                analyticsList.add(new Analytics(entry.getKey(), String.valueOf(entry.getValue())));
                        }
                        rvResourceHistory.setAdapter(new AnalyticsDataAdapter(analyticsList));
                    }

                    @Override
                    public void onFailure() {

                    }
                });
                break;
            case KEY_CONNECTIONS_UNDIRECTED:
                setUpActionBar("Connections (Undirected)", true);
                new ConnecteionsUndirectedQuery().execute(new Query.QueryCompleteListener<Map<String, Integer>>() {
                    @Override
                    public void onSuccess(Map<String, Integer> result) {
                        List<Analytics> analyticsList = new ArrayList<>();
                        if (result.size() != 0) {
                            for (Map.Entry<String, Integer> entry : result.entrySet())
                                analyticsList.add(new Analytics(entry.getKey(), String.valueOf(entry.getValue())));
                        }
                        rvResourceHistory.setAdapter(new AnalyticsDataAdapter(analyticsList));
                    }

                    @Override
                    public void onFailure() {

                    }
                });
                break;
        }

    }
}
