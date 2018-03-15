package simararora.ravenlib;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.util.Log;
import android.util.LruCache;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.List;

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
            FirebaseApp.initializeApp(context);
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
        String path = String.format("Clients/%s/", clientId);
        //Parallel requests are sent for source and resource.
        OnCompleteListenerComposite onCompleteListener = new OnCompleteListenerComposite(ravenResource, parseCompleteListener);
        //Source object is present at Clients/<clientId>/Sources/<sourceDocumentId>/
        FirebaseFirestore.getInstance().document(path).collection("Sources").whereEqualTo("$id", ravenResource.getSourceId()).get().addOnCompleteListener(onCompleteListener.sourceOnCompleteListener);
        //Resource object is present at Clients/<clientId>/Resources/<resourceDocumentId>/
        FirebaseFirestore.getInstance().document(path).collection("Resources").whereEqualTo("$id", String.format("%s-%s", ravenResource.getResourceType(), ravenResource.getResourceId())).get().addOnCompleteListener(onCompleteListener.resourceCompleteListener);
    }

    // The OnCompleteListenerComposite object waits for both the responses
    // and notifies the user once both responses have been successfully fetched
    private class OnCompleteListenerComposite {

        private RavenResource ravenResource;
        private ParseCompleteListener parseCompleteListener;
        private Boolean sourceCompleted;
        private Boolean resourceCompleted;

        OnCompleteListenerComposite(RavenResource ravenResource, ParseCompleteListener parseCompleteListener) {
            this.ravenResource = ravenResource;
            this.parseCompleteListener = parseCompleteListener;
        }

        /**
         * Callback called for after fetching source details from server
         */
        private OnCompleteListener<QuerySnapshot> sourceOnCompleteListener = new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    List<DocumentSnapshot> documentSnapshots = task.getResult().getDocuments();
                    if (!documentSnapshots.isEmpty()) {
                        //First result matching the query is used
                        ravenResource.setSourceIdParams(documentSnapshots.get(0).getData());
                        sourceCompleted = true;
                    } else
                        sourceCompleted = false;
                } else
                    sourceCompleted = false;
                checkResponses();
            }
        };

        /**
         * Callback called for after fetching resource details from server
         */
        private OnCompleteListener<QuerySnapshot> resourceCompleteListener = new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    List<DocumentSnapshot> documentSnapshots = task.getResult().getDocuments();
                    if (!documentSnapshots.isEmpty()) {
                        //First result matching the query is used
                        ravenResource.setResourceIdParams(documentSnapshots.get(0).getData());
                        resourceCompleted = true;
                    } else
                        resourceCompleted = false;

                } else
                    resourceCompleted = false;
                checkResponses();
            }
        };


        //checkResponse checks if both responses have been fetched and notifies the user accordingly
        //Since ths method is going to run on the same thread always, there are no race conditions
        private void checkResponses() {

            //These variables have three states
            //Null -> Waiting for response
            //false -> Failure
            //true -> Success

            //If any one of these variables is null, we are waiting for response
            if (sourceCompleted == null || resourceCompleted == null)
                return;
            //If both are true, both responses have been fetched successfully and can be passed to the user
            if (sourceCompleted && resourceCompleted) {
                parseCompleteListener.onParseComplete(ravenResource);
                //Updating our LRU cache
                cache.put(ravenResource.getUri().toString(), ravenResource);
            } else
                //Else we notify failure to the user
                parseCompleteListener.onParseFailed(new Exception("Failed to resolve link data"));

        }

    }
}
