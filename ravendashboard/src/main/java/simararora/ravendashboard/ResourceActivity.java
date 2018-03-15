package simararora.ravendashboard;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.gson.Gson;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import simararora.ravendashboard.model.Request;

/**
 * Created by nateshrelhan on 3/15/18.
 */

public class ResourceActivity extends BaseCreateDataActivity implements View.OnClickListener {
    private EditText etResourceName;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resource);
        setUpActionBar("Resource", true);
        etResourceName = findViewById(R.id.et_resource_name);
        initializeCommonLayout();
    }


    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.bt_submit:
                String resourceName = etResourceName.getText().toString();
                if (AppUtil.isEmptyOrNullString(resourceName)) {
                    Toast.makeText(this, "Please enter resource name to proceed", Toast.LENGTH_SHORT).show();
                    return;
                }
                boolean resourceKeyValeNotEmpty = false;
                for (int i = 0; i < llDetails.getChildCount(); i++) {
                    LinearLayout llResource = (LinearLayout) llDetails.getChildAt(i);
                    String resourceKey = ((EditText) llResource.findViewById(R.id.et_key)).getText().toString();
                    String resourceValue = ((EditText) llResource.findViewById(R.id.et_value)).getText().toString();
                    if (AppUtil.isEmptyOrNullString(resourceKey) || AppUtil.isEmptyOrNullString(resourceValue))
                        continue;
                    resourceKeyValeNotEmpty = true;
                    keyValue.put(resourceKey, resourceValue);
                }
                if (!resourceKeyValeNotEmpty || keyValue.size() == 0) {
                    Toast.makeText(this, "Please enter at least one resource detail to proceed", Toast.LENGTH_SHORT).show();
                    return;
                }
                final Request resourceRequest = new Request(resourceName, keyValue);
                DashboardApplication.getAPIService(this).createResource(resourceRequest)
                        .enqueue(new Callback<String>() {
                            @Override
                            public void onResponse(Call<String> call, Response<String> response) {
                                Toast.makeText(ResourceActivity.this, response.body(), Toast.LENGTH_SHORT).show();
                                ResourceSession.getInstance(ResourceActivity.this).updateHistory(ResourceSession.KEY_RESOURCE_HISTORY, response.body(), new Gson().toJson(resourceRequest));
                            }

                            @Override
                            public void onFailure(Call<String> call, Throwable t) {
                                Toast.makeText(ResourceActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });

                break;
        }
    }
}
