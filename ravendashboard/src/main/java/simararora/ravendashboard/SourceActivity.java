package simararora.ravendashboard;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by nateshrelhan on 3/15/18.
 */

public class SourceActivity extends AppCompatActivity implements View.OnClickListener {
    private ImageView ivAddSource;
    private LinearLayout llSourceDetails;
    private Button btSubmit;
    private Map<String, String> sourceKeyValue;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_source);
        ivAddSource = findViewById(R.id.iv_add_source);
        llSourceDetails = findViewById(R.id.ll_source_details);
        btSubmit = findViewById(R.id.bt_submit);
        btSubmit.setOnClickListener(this);
        ivAddSource.setOnClickListener(this);
        sourceKeyValue = new HashMap<>();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_add_source:
                final LinearLayout llNewSource = (LinearLayout) getLayoutInflater().inflate(R.layout.item_key_value, null);
                LinearLayout.LayoutParams llNewSourceParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                llNewSourceParams.weight = 1.0f;
                llNewSourceParams.setMargins(AppUtil.dpToPx(this, 12), AppUtil.dpToPx(this, 12), AppUtil.dpToPx(this, 12), AppUtil.dpToPx(this, 12));
                llNewSource.setLayoutParams(llNewSourceParams);
                llNewSource.setGravity(Gravity.CENTER_VERTICAL);
                final EditText etSourceKey = llNewSource.findViewById(R.id.et_key);
                final EditText etSourceValue = llNewSource.findViewById(R.id.et_value);
                LinearLayout.LayoutParams etNewSourceParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                etNewSourceParams.weight = 0.5f;
                etSourceKey.setLayoutParams(etNewSourceParams);
                etSourceValue.setLayoutParams(etNewSourceParams);

                ImageView ivRemoveSource = llNewSource.findViewById(R.id.iv_remove);
                ivRemoveSource.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        sourceKeyValue.remove(etSourceKey.getText().toString());
                        llSourceDetails.removeView(llNewSource);
                    }
                });
                llSourceDetails.addView(llNewSource);
                break;
            case R.id.bt_submit:
                for (int i = 0; i < llSourceDetails.getChildCount(); i++) {
                    LinearLayout llSource = (LinearLayout) llSourceDetails.getChildAt(i);
                    String sourceKey = ((EditText) llSource.findViewById(R.id.et_key)).getText().toString();
                    String sourceValue = ((EditText) llSource.findViewById(R.id.et_value)).getText().toString();
                    sourceKeyValue.put(sourceKey, sourceValue);
                }
                if (sourceKeyValue.size() == 0) {
                    Toast.makeText(this, "Please enter at least one source detail to proceed", Toast.LENGTH_LONG).show();
                    return;
                }
                break;
        }
    }
}
