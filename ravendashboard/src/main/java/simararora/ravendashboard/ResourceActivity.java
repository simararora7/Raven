package simararora.ravendashboard;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.gson.Gson;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import simararora.ravendashboard.model.Request;

/**
 * Created by nateshrelhan on 3/15/18.
 */

public class ResourceActivity extends BaseAppCompatActivity implements View.OnClickListener {
    private EditText etResourceName;
    private ImageView ivAddResource;
    private LinearLayout llResourceDetails;
    private Button btSubmit;
    private Map<String, String> resourceKeyValue;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resource);
        setUpActionBar("Resource", true);
        etResourceName = findViewById(R.id.et_resource_name);
        ivAddResource = findViewById(R.id.iv_add_resource);
        llResourceDetails = findViewById(R.id.ll_resource_details);
        btSubmit = findViewById(R.id.bt_submit);
        btSubmit.setOnClickListener(this);
        ivAddResource.setOnClickListener(this);
        resourceKeyValue = new HashMap<>();
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_add_resource:
                final LinearLayout llNewResource = (LinearLayout) getLayoutInflater().inflate(R.layout.item_key_value, null);
                LinearLayout.LayoutParams llNewResourceParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                llNewResourceParams.weight = 1.0f;
                llNewResourceParams.setMargins(AppUtil.dpToPx(this, 12), AppUtil.dpToPx(this, 12), AppUtil.dpToPx(this, 12), AppUtil.dpToPx(this, 12));
                llNewResource.setLayoutParams(llNewResourceParams);
                llNewResource.setGravity(Gravity.CENTER_VERTICAL);
                final EditText etResourceKey = llNewResource.findViewById(R.id.et_key);
                final EditText etResourceValue = llNewResource.findViewById(R.id.et_value);
                LinearLayout.LayoutParams etNewResourceParams = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT);
                etNewResourceParams.weight = 0.5f;
                etResourceKey.setLayoutParams(etNewResourceParams);
                etResourceValue.setLayoutParams(etNewResourceParams);

                ImageView ivRemoveResource = llNewResource.findViewById(R.id.iv_remove);
                ivRemoveResource.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        resourceKeyValue.remove(etResourceKey.getText().toString());
                        llResourceDetails.removeView(llNewResource);
                    }
                });
                llResourceDetails.addView(llNewResource);
                break;
            case R.id.bt_submit:
                String resourceName = etResourceName.getText().toString();
                if (AppUtil.isEmptyOrNullString(resourceName)) {
                    Toast.makeText(this, "Please enter resource name to proceed", Toast.LENGTH_SHORT).show();
                    return;
                }
                boolean resourceKeyValeNotEmpty = false;
                for (int i = 0; i < llResourceDetails.getChildCount(); i++) {
                    LinearLayout llResource = (LinearLayout) llResourceDetails.getChildAt(i);
                    String resourceKey = ((EditText) llResource.findViewById(R.id.et_key)).getText().toString();
                    String resourceValue = ((EditText) llResource.findViewById(R.id.et_value)).getText().toString();
                    if (AppUtil.isEmptyOrNullString(resourceKey) || AppUtil.isEmptyOrNullString(resourceValue))
                        continue;
                    resourceKeyValeNotEmpty = true;
                    resourceKeyValue.put(resourceKey, resourceValue);
                }
                if (!resourceKeyValeNotEmpty || resourceKeyValue.size() == 0) {
                    Toast.makeText(this, "Please enter at least one resource detail to proceed", Toast.LENGTH_SHORT).show();
                    return;
                }
                final Request resourceRequest = new Request(resourceName, resourceKeyValue);
                DashboardApplication.getAPIService(this).createResource(resourceRequest)
                        .enqueue(new Callback<String>() {
                            @Override
                            public void onResponse(Call<String> call, Response<String> response) {
                                Toast.makeText(ResourceActivity.this, response.body(), Toast.LENGTH_SHORT).show();
                                ResourceSession.getInstance(ResourceActivity.this).updateHistory(ResourceSession.KEY_RESOURCE_HISTORY,response.body(), new Gson().toJson(resourceRequest));
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
