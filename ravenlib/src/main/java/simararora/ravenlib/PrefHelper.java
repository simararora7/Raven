package simararora.ravenlib;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.text.TextUtils;

/**
 * Created by nateshrelhan on 3/15/18.
 */

public class PrefHelper {
    /**
     * shared preferences key file name
     */
    private static final String SHARED_PREF_FILE = "raven_referral_shared_pref";
    private static final String KEY_RAVEN_CLIENT_ID = "raven_client_id";
    /**
     * A {@link String} value used where no string value is available.
     */
    public static final String NO_STRING_VALUE = "raven_no_value";
    /**
     * PrefHelper static object used in reading and writing values in respective to resources
     */
    private static PrefHelper mPrefHelper;
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

    /**
     * client id to be used in all the requests.
     */
    private static String ravenClientId = null;

    private PrefHelper(Context mContext) {
        this.mContext = mContext;
        this.mPrefSharedPreferences = mContext.getSharedPreferences(SHARED_PREF_FILE, Context.MODE_PRIVATE);
        this.mPrefEditor = this.mPrefSharedPreferences.edit();
    }

    public static PrefHelper getInstance(Context mContext) {
        if (mPrefHelper == null) {
            mPrefHelper = new PrefHelper(mContext);
        }
        return mPrefHelper;
    }


    public String readRavenClientId() {
        String ravenClientId = null;
        String metaDataKey = "raven.sdk.ClientId";
        try {
            final ApplicationInfo ai = this.mContext.getPackageManager().getApplicationInfo(this.mContext.getPackageName(), PackageManager.GET_META_DATA);
            if (ai.metaData != null) {
                ravenClientId = ai.metaData.getString(metaDataKey);
                if (ravenClientId == null)
                    ravenClientId = ai.metaData.getString(metaDataKey);
            }
        } catch (final PackageManager.NameNotFoundException ignore) {
        }
        // If Raven client id is not specified in the manifest check String resource
        if (TextUtils.isEmpty(ravenClientId)) {
            try {
                Resources resources = this.mContext.getResources();
                ravenClientId = resources.getString(resources.getIdentifier(metaDataKey, "string", this.mContext.getPackageName()));
            } catch (Exception ignore) {
            }
        }
        if (ravenClientId == null)
            ravenClientId = NO_STRING_VALUE;
        return ravenClientId;
    }

    /**
     * Set the given Raven client id to preference.
     *
     * @param clientId A {@link String} representing Client Id.
     * @return A {@link Boolean} which is true if the key set is a new key.
     */
    public boolean setRavenClientId(String clientId) {
        ravenClientId = clientId;
        String currentClientId = getString(KEY_RAVEN_CLIENT_ID);
        if (clientId == null || currentClientId == null || !currentClientId.equals(clientId)) {
            setString(KEY_RAVEN_CLIENT_ID, clientId);
            return true;
        }
        return false;
    }

    /**
     *
     * @return return current Raven client id
     */
    public String getRavenClientId() {
        if (ravenClientId == null) {
            ravenClientId = getString(KEY_RAVEN_CLIENT_ID);
        }
        return ravenClientId;
    }


    /**
     * <p>A basic method that returns a {@link String} value from a specified preferences Key.</p>
     *
     * @param key A {@link String} value containing the key to reference.
     * @return A {@link String} value of the specified key as stored in preferences.
     */
    public String getString(String key) {
        return mPrefHelper.mPrefSharedPreferences.getString(key, NO_STRING_VALUE);
    }

    /**
     * <p>Sets the value of the {@link String} key value supplied in preferences.</p>
     *
     * @param key   A {@link String} value containing the key to reference.
     * @param value A {@link String} value to set the preference record to.
     */
    public void setString(String key, String value) {
        mPrefHelper.mPrefEditor.putString(key, value);
        mPrefHelper.mPrefEditor.apply();
    }
}
