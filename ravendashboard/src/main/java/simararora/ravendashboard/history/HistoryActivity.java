package simararora.ravendashboard.history;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import simararora.ravendashboard.BaseAppCompatActivity;
import simararora.ravendashboard.R;
import simararora.ravendashboard.ResourceSession;
import simararora.ravendashboard.adapter.HistoryAdapter;

/**
 * Created by nateshrelhan on 3/15/18.
 */

public class HistoryActivity extends BaseAppCompatActivity {
    public static final String KEY_FROM = "from_";
    public static final String KEY_FROM_RESOURCE = "resource_source";
    public static final String KEY_FROM_SOURCE = "source";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        setUpActionBar("History", true);
        RecyclerView rvResourceHistory = findViewById(R.id.rv_history);
        rvResourceHistory.setLayoutManager(new LinearLayoutManager(this));
        String source = getIntent().getStringExtra(KEY_FROM);
        switch (source) {
            case KEY_FROM_RESOURCE:
                rvResourceHistory.setAdapter(new HistoryAdapter(ResourceSession.getInstance(this).getResourceHistory(ResourceSession.KEY_RESOURCE_HISTORY)));
                break;
            case KEY_FROM_SOURCE:
                rvResourceHistory.setAdapter(new HistoryAdapter(ResourceSession.getInstance(this).getResourceHistory(ResourceSession.KEY_SOURCE_HISTORY)));
                break;
        }

    }
}
