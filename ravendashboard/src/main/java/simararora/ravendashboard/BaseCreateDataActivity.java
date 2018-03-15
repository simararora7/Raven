package simararora.ravendashboard;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import java.util.HashMap;
import java.util.Map;

import simararora.ravendashboard.details.HistoryActivity;

/**
 * Created by nateshrelhan on 3/15/18.
 */

public class BaseCreateDataActivity extends BaseAppCompatActivity implements View.OnClickListener {
    protected LinearLayout llDetails;
    protected Map<String, String> keyValue;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        keyValue = new HashMap<>();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (this instanceof ResourceActivity || this instanceof SourceActivity) {
            MenuInflater menuInflater = getMenuInflater();
            menuInflater.inflate(R.menu.menu_history, menu);
            return true;
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.history:
                if (this instanceof ResourceActivity) {
                    Intent intentResourceHistory = new Intent(this, HistoryActivity.class);
                    intentResourceHistory.putExtra(HistoryActivity.KEY_FROM, HistoryActivity.KEY_FROM_RESOURCE);
                    startActivity(intentResourceHistory);
                } else if (this instanceof SourceActivity) {
                    Intent intentResourceHistory = new Intent(this, HistoryActivity.class);
                    intentResourceHistory.putExtra(HistoryActivity.KEY_FROM, HistoryActivity.KEY_FROM_SOURCE);
                    startActivity(intentResourceHistory);
                }
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    protected void initializeCommonLayout() {
        findViewById(R.id.ll_add).setOnClickListener(this);
        llDetails = findViewById(R.id.ll_details);
        llDetails.setOnClickListener(this);
        findViewById(R.id.bt_submit).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_add:
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
                        keyValue.remove(etResourceKey.getText().toString());
                        llDetails.removeView(llNewResource);
                    }
                });
                llDetails.addView(llNewResource);
                break;
        }
    }
}
