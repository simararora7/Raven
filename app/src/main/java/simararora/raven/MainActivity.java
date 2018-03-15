package simararora.raven;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Html;
import android.widget.TextView;

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
        setContentView(R.layout.activity_main);
        messageView = findViewById(R.id.tv_data);
        if (ravenResource != null){
            messageView.setText(Html.fromHtml(ravenResource.toString()));
        }else if (exception != null){
            messageView.setText(Html.fromHtml(exception.getMessage()));
        }

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
            messageView.setText(Html.fromHtml(exception.getMessage()));
    }
}
