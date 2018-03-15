package simararora.ravendashboard;

import android.os.Bundle;
import android.support.annotation.Nullable;

/**
 * Created by nateshrelhan on 3/16/18.
 */

public class AnalyticsActivity extends BaseAppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_analytics);
        setUpActionBar("Analytics", true);
    }
}
