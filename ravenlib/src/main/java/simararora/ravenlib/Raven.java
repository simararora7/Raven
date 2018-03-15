package simararora.ravenlib;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;
import android.util.LruCache;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import simararora.ravenlib.model.RavenResource;

/**
 * Created by Simar Arora on 15/03/18.
 */

public class Raven {
    private static final String TAG = Raven.class.getSimpleName();
    private static final String authority = "raven.lt";
    private static final String EXCEPTION_PREFIX = "Raven Warning: ";
    private static final String ERROR_MISSING_CLIENT_ID = EXCEPTION_PREFIX + "Please enter your raven client id in your project's Manifest file!";
    private static Raven mRaven;
    private PrefHelper mPrefHelper;
    private LruCache<String, RavenResource> cache;

    private Raven(Context mContext) {
        this.mPrefHelper = PrefHelper.getInstance(mContext);
    }

    public static void init(Context context) {
        if (mRaven == null) {
            mRaven = new Raven(context);
            String ravenClientId = mRaven.mPrefHelper.readRavenClientId();
            if (ravenClientId == null || ravenClientId.equalsIgnoreCase(PrefHelper.NO_STRING_VALUE)) {
                Log.i(TAG, ERROR_MISSING_CLIENT_ID);
            } else
                mRaven.mPrefHelper.setRavenClientId(ravenClientId);
            mRaven.cache = new LruCache<>(10);
        }
    }

    static Raven getInstance() {
        if (mRaven == null)
            throw new RuntimeException(EXCEPTION_PREFIX +"init needs to be called before getInstance is called");
        return mRaven;
    }

    /**
     * parse call for intent data and null parseCompleteListener
     *
     * @param intent intent to be parsed
     * @return raw RavenResource
     */
    public RavenResource parse(Intent intent) {
        return parse(intent, null);
    }

    /**
     * parse call for intent data and callback as parseCompleteListener
     *
     * @param intent                intent to be parsed
     * @param parseCompleteListener used as callback for raw RavenResource containing params fetch from server
     * @return raw RavenResource
     */
    public RavenResource parse(Intent intent, ParseCompleteListener parseCompleteListener) {
        if (intent == null) {
            if (parseCompleteListener != null)
                parseCompleteListener.onParseFailed(new NullPointerException(EXCEPTION_PREFIX +"No Link Present"));
            return null;
        }
        return parse(intent.getData(), parseCompleteListener);
    }

    /**
     * parse call for intent data and null parseCompleteListener
     *
     * @param data uri data to be parsed
     * @return raw RavenResource
     */
    public RavenResource parse(Uri data) {
        return parse(data, null);
    }

    /**
     * parse call for intent data and callback as parseCompleteListener
     *
     * @param data                  uri data to be parsed
     * @param parseCompleteListener used as callback for raw RavenResource containing params fetch from server
     * @return raw RavenResource
     */
    public RavenResource parse(Uri data, ParseCompleteListener parseCompleteListener) {
        //If data is null call onParseFailed
        if (data == null) {
            if (parseCompleteListener != null)
                parseCompleteListener.onParseFailed(new NullPointerException("No Link Present"));
            return null;
        }
        //Fetching data from our LRU cache and returning response on the go
        RavenResource cachedResource = cache.get(data.toString());
        if (cachedResource != null) {
            if (parseCompleteListener != null)
                parseCompleteListener.onParseComplete(cachedResource);
            return cachedResource;
        }
        //Check if the url needs to be handled by Raven
        if (!Raven.authority.equals(data.getAuthority())) {
            if (parseCompleteListener != null)
                parseCompleteListener.onParseFailed(new Exception("Authority Mismatch"));
            return null;
        }
        try {
            //Create RavenResource object
            RavenResource ravenResource = new RavenResource(data);
            if (parseCompleteListener != null)
                fetchResourceDetails(ravenResource, parseCompleteListener);
            return ravenResource;
        } catch (Exception e) {
            if (parseCompleteListener != null)
                parseCompleteListener.onParseFailed(e);
            return null;
        }
    }

    /**
     * Used to fetch data from raw RavenResource
     *
     * @param ravenResource         raw RavenResource created with the intent data
     * @param parseCompleteListener callback to be called after fetching data
     */
    private void fetchResourceDetails(RavenResource ravenResource, ParseCompleteListener parseCompleteListener) {
        //If no client id is specified in Manifest
        String clientId = mPrefHelper.getRavenClientId();
        if (clientId == null) {
            parseCompleteListener.onParseFailed(new NullPointerException(ERROR_MISSING_CLIENT_ID));
            return;
        }
        new RequestAsyncTask(ravenResource, parseCompleteListener).execute();
    }

    private static class RequestAsyncTask extends AsyncTask<Void, Void, Exception> {

        private RavenResource ravenResource;
        private ParseCompleteListener parseCompleteListener;

        RequestAsyncTask(RavenResource ravenResource, ParseCompleteListener parseCompleteListener) {
            this.ravenResource = ravenResource;
            this.parseCompleteListener = parseCompleteListener;
        }

        @Override
        protected Exception doInBackground(Void... voids) {
            HttpURLConnection urlConnection = null;
            try {
                URL url = new URL(String.format("https://us-central1-raven-347c7.cloudfunctions.net/api/linkinfo/%s/%s/%s", ravenResource.getResourceType(), ravenResource.getResourceId(), ravenResource.getSourceId()));
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                //Add userid as header
                urlConnection.setRequestProperty("userid", "Simar");
                int responseCode = urlConnection.getResponseCode();
                if (responseCode == HttpURLConnection.HTTP_OK) {
                    BufferedReader in = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                    String inputLine;
                    StringBuilder stringBuilder = new StringBuilder();
                    while ((inputLine = in.readLine()) != null) {
                        stringBuilder.append(inputLine);
                    }
                    in.close();
                    String response = stringBuilder.toString();
                    Log.d(TAG, "doInBackground: " + response);
                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.has("sourceData")) {
                        ravenResource.setSourceIdParams(jsonObject.getJSONObject("sourceData"));
                    }
                    if (jsonObject.has("resourceData")) {
                        ravenResource.setResourceIdParams(jsonObject.getJSONObject("resourceData"));
                    }
                    if (ravenResource.isComplete()) {
                        return null;
                    } else {
                        return new Exception("Failed to resolve link data");
                    }
                } else {
                    return new Exception("Http Error " + responseCode);
                }
            } catch (Exception e) {
                return e;
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
            }
        }

        @Override
        protected void onPostExecute(Exception e) {
            super.onPostExecute(e);
            if (e != null) {
                e.printStackTrace();
                parseCompleteListener.onParseFailed(e);
            } else {
                //Updating our LRU cache
                mRaven.cache.put(ravenResource.getUri().toString(), ravenResource);
                parseCompleteListener.onParseComplete(ravenResource);
            }
        }
    }
}
