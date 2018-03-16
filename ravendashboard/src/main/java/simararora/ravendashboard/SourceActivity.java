package simararora.ravendashboard;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.gson.Gson;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import simararora.ravendashboard.model.Request;

/**
 * Created by nateshrelhan on 3/15/18.
 */

public class SourceActivity extends BaseCreateDataActivity implements View.OnClickListener {
    private EditText etUsername;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_source);
        setUpActionBar("Source", true);
        keyValue = new HashMap<>();
        initializeCommonLayout();
        etUsername = findViewById(R.id.et_username);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.bt_submit:
                String username = etUsername.getText().toString();
                if (AppUtil.isEmptyOrNullString(username)) {
                    Toast.makeText(this, "Please enter username to proceed", Toast.LENGTH_SHORT).show();
                    return;
                }
                boolean sourceKeyValeNotEmpty = false;
                for (int i = 0; i < llDetails.getChildCount(); i++) {
                    LinearLayout llSource = (LinearLayout) llDetails.getChildAt(i);
                    String sourceKey = ((EditText) llSource.findViewById(R.id.et_key)).getText().toString();
                    String sourceValue = ((EditText) llSource.findViewById(R.id.et_value)).getText().toString();
                    if (AppUtil.isEmptyOrNullString(sourceKey) || AppUtil.isEmptyOrNullString(sourceValue))
                        continue;
                    sourceKeyValeNotEmpty = true;
                    keyValue.put(sourceKey, sourceValue);
                }
                if (!sourceKeyValeNotEmpty || keyValue.size() == 0) {
                    Toast.makeText(this, "Please enter at least one source detail to proceed", Toast.LENGTH_SHORT).show();
                    return;
                }
                keyValue.put("$userId", username);
                final Request sourceRequest = new Request(keyValue);
                DashboardApplication.getAPIService(this).createSource(new Request(keyValue))
                        .enqueue(new Callback<String>() {
                            @Override
                            public void onResponse(Call<String> call, Response<String> response) {
                                Toast.makeText(SourceActivity.this, response.body(), Toast.LENGTH_SHORT).show();
                                ResourceSession.getInstance(SourceActivity.this).updateHistory(ResourceSession.KEY_SOURCE_HISTORY, response.body(), new Gson().toJson(sourceRequest));
                            }

                            @Override
                            public void onFailure(Call<String> call, Throwable t) {
                                Toast.makeText(SourceActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });


                break;
        }
    }
}
