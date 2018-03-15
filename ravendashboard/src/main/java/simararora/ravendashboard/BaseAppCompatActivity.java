package simararora.ravendashboard;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import simararora.ravendashboard.history.HistoryActivity;

/**
 * Created by nateshrelhan on 3/15/18.
 */

public class BaseAppCompatActivity extends AppCompatActivity {
    public ActionBar setUpActionBar(String title, boolean addBackButton) {
        final android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle(title);
        if (addBackButton) {
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowHomeEnabled(true);
        }
        return actionBar;
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
}
