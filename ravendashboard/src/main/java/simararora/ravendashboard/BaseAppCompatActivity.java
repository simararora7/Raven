package simararora.ravendashboard;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import simararora.ravendashboard.history.HistoryActivity;

/**
 * Created by nateshrelhan on 3/16/18.
 */

public class BaseAppCompatActivity extends AppCompatActivity {
    public void setUpActionBar(String title, boolean addBackButton) {
        final android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        if (actionBar == null) return;
        actionBar.setTitle(title);
        if (addBackButton) {
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowHomeEnabled(true);
        }
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
            case android.R.id.home:
                onBackPressed();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
