package simararora.ravenlib;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by Simar Arora on 15/03/18.
 */

public abstract class RavenActivity extends AppCompatActivity implements ParseCompleteListener {

    public static final String TAG = RavenActivity.class.getSimpleName();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initRaven();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        initRaven();
    }

    private void initRaven(){
        Raven.getInstance().parse(getIntent(), this);
    }
}