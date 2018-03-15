package simararora.ravendashboard;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.ArrayList;
import java.util.List;

import simararora.ravendashboard.model.History;


/**
 * Created by nateshrelhan on 3/16/18.
 */

public class ResourceSession {
    private static final String SHARED_PREF_FILE = "resource_session_shared_pref";
    public static final String KEY_RESOURCE_HISTORY = "resource_history";
    public static final String KEY_SOURCE_HISTORY = "source_history";
    private static final String resourceSeparator = "&&&";
    private static final String resourceEquality = "===";

    /**
     * A {@link String} value used where no string value is available.
     */
    public static final String NO_STRING_VALUE = "raven_no_value";
    /**
     * PrefHelper static object used in reading and writing values in respective to resources
     */
    private static ResourceSession mResourceSession;
    /**
     * Context used in initializing PrefHelper resources
     */
    private Context mContext;
    /**
     * SharedPreferences object used to read values
     */
    private SharedPreferences mPrefSharedPreferences;
    /**
     * SharedPreferences.Editor object used to write and commit values
     */
    private SharedPreferences.Editor mPrefEditor;

    private ResourceSession(Context mContext) {
        this.mContext = mContext;
        this.mPrefSharedPreferences = mContext.getSharedPreferences(SHARED_PREF_FILE, Context.MODE_PRIVATE);
        this.mPrefEditor = this.mPrefSharedPreferences.edit();
    }

    public static ResourceSession getInstance(Context mContext) {
        if (mResourceSession == null) {
            mResourceSession = new ResourceSession(mContext);
        }
        return mResourceSession;
    }

    /**
     * <p>A basic method that returns a {@link String} value from a specified preferences Key.</p>
     *
     * @param key A {@link String} value containing the key to reference.
     * @return A {@link String} value of the specified key as stored in preferences.
     */
    public String getString(String key) {
        return mResourceSession.mPrefSharedPreferences.getString(key, NO_STRING_VALUE);
    }

    /**
     * <p>Sets the value of the {@link String} key value supplied in preferences.</p>
     *
     * @param key   A {@link String} value containing the key to reference.
     * @param value A {@link String} value to set the preference record to.
     */
    public void setString(String key, String value) {
        mResourceSession.mPrefEditor.putString(key, value);
        mResourceSession.mPrefEditor.apply();
    }

    public List<History> getResourceHistory(String sharedPrefKey) {
        List<History> ravenResourceHistoryList = new ArrayList<>();
        String[] resourceHistories = getString(sharedPrefKey).split(resourceSeparator);
        if (resourceHistories.length == 0) return ravenResourceHistoryList;
        for (String resourceHistory : resourceHistories) {
            String[] specificResourceHistory = resourceHistory.split(resourceEquality);
            if (specificResourceHistory.length < 2) continue;
            ravenResourceHistoryList.add(new History(specificResourceHistory[0], specificResourceHistory[1]));
        }
        return ravenResourceHistoryList;
    }

    public void updateHistory(String sharedPrefKey, String encodedKey, String value) {
        String existingResourceHistory = getString(sharedPrefKey);
        if (existingResourceHistory == null || existingResourceHistory.equalsIgnoreCase(NO_STRING_VALUE))
            setString(sharedPrefKey, encodedKey + resourceEquality + value);
        else
            setString(sharedPrefKey, existingResourceHistory + resourceSeparator + encodedKey + resourceEquality + value);
    }
}
