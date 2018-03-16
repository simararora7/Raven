package simararora.raven;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Html;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import simararora.ravenlib.Raven;
import simararora.ravenlib.RavenActivity;
import simararora.ravenlib.model.RavenResource;

/**
 * Created by Simar Arora on 15/03/18.
 */

public class MainActivity extends RavenActivity {

    private TextView messageView;
    private RavenResource ravenResource;
    private Exception exception;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_raven);
        messageView = findViewById(R.id.tv_data);
        if (ravenResource != null) {
            messageView.setText(Html.fromHtml(ravenResource.toString()));
        } else if (exception != null) {
            messageView.setText(exception.getMessage());
        }
        final EditText etUsername = findViewById(R.id.et_username);
        findViewById(R.id.bt_submit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = etUsername.getText().toString();
                if (username != null && !username.isEmpty()) {
                    Raven.getInstance().setUserIdentifier(username);
                    Toast.makeText(MainActivity.this, "Raven initialized with " + username, Toast.LENGTH_SHORT).show();
                } else
                    Raven.getInstance().setUserIdentifier("Simar");
            }
        });
    }

    @Override
    public void onParseComplete(RavenResource ravenResource) {
        this.ravenResource = ravenResource;
        if (messageView != null)
            messageView.setText(Html.fromHtml(ravenResource.toString()));
    }

    @Override
    public void onParseFailed(Exception exception) {
        this.exception = exception;
        if (messageView != null)
            messageView.setText(exception.getMessage());
    }
}
