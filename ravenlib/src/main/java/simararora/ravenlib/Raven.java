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

import simararora.ravenlib.model.RavenResource;

/**
 * Created by Simar Arora on 15/03/18.
 */

public class Raven {
    private static final String TAG = Raven.class.getSimpleName();
    private static final String authority = "raven.lt";
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
                Log.i(TAG, "Raven Warning: Please enter your raven client id in your project's Manifest file!");
            } else
                mRaven.mPrefHelper.setRavenClientId(ravenClientId);
            mRaven.cache = new LruCache<>(10);
            FirebaseApp.initializeApp(context);
        }
    }

    static Raven getInstance() {
        if(mRaven == null)
            throw  new RuntimeException("init needs to be called before getInstance is called");
        return mRaven;
    }

    public RavenResource parse(Intent intent) {
        return parse(intent, null);
    }

    public RavenResource parse(Intent intent, ParseCompleteListener parseCompleteListener) {
        if (intent == null) {
            if (parseCompleteListener != null)
                parseCompleteListener.onParseFailed(new NullPointerException());
            return null;
        }
        return parse(intent.getData(), parseCompleteListener);
    }

    public RavenResource parse(Uri data) {
        return parse(data, null);
    }

    public RavenResource parse(Uri data, ParseCompleteListener parseCompleteListener) {
        if (data == null) {
            if (parseCompleteListener != null)
                parseCompleteListener.onParseFailed(new NullPointerException());
            return null;
        }

        RavenResource cachedResource = cache.get(data.toString());
        if (cachedResource != null){
            if (parseCompleteListener != null)
                parseCompleteListener.onParseComplete(cachedResource);
            return cachedResource;
        }

        String authority = data.getAuthority();
        //Check if the url needs to be handled by Raven
        if (!Raven.authority.equals(authority)) {
            if (parseCompleteListener != null)
                parseCompleteListener.onParseFailed(new Exception("Authority Mismatch"));
            return null;
        }

        String path = data.getPath();

        try {
            //Create RavenResource object
            RavenResource ravenResource = new RavenResource(path);
            if (parseCompleteListener != null)
                fetchResourceDetails(data, ravenResource, parseCompleteListener);
            return ravenResource;
        } catch (Exception e) {
            if (parseCompleteListener != null)
                parseCompleteListener.onParseFailed(e);
            return null;
        }
    }

    private void fetchResourceDetails(Uri data, RavenResource ravenResource, ParseCompleteListener parseCompleteListener) {
        String clientId = mPrefHelper.getRavenClientId();
        if (clientId == null) {
            parseCompleteListener.onParseFailed(new NullPointerException("Client Id is Null"));
            return;
        }
        String path = String.format("Clients/%s/", clientId);
        OnCompleteListenerComposite onCompleteListener = new OnCompleteListenerComposite(data, ravenResource, parseCompleteListener);
        //Source object is present at Clients/<clientId>/Sources/<sourceDocumentId>/
        FirebaseFirestore.getInstance().document(path).collection("Sources").whereEqualTo("$id", ravenResource.getSourceId()).get().addOnCompleteListener(onCompleteListener.sourceOnCompleteListener);
        //Resource object is present at Clients/<clientId>/Resources/<resourceDocumentId>/
        FirebaseFirestore.getInstance().document(path).collection("Resources").whereEqualTo("$id", String.format("%s-%s", ravenResource.getResourceType(), ravenResource.getResourceId())).get().addOnCompleteListener(onCompleteListener.resourceCompleteListener);
    }

    //Parallel requests are sent for source and resource.
    // The OnCompleteListenerComposite object waits for both the responses
    // and notifies the user once both responses have been successfully fetched
    private class OnCompleteListenerComposite {

        private Uri data;
        private RavenResource ravenResource;
        private ParseCompleteListener parseCompleteListener;
        private Boolean sourceCompleted;
        private Boolean resourceCompleted;

        OnCompleteListenerComposite(Uri data, RavenResource ravenResource, ParseCompleteListener parseCompleteListener) {
            this.data = data;
            this.ravenResource = ravenResource;
            this.parseCompleteListener = parseCompleteListener;
        }

        private OnCompleteListener<QuerySnapshot> sourceOnCompleteListener = new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    sourceCompleted = false;
                    for (DocumentSnapshot document : task.getResult()) {
                        //First result matching the query is used
                        ravenResource.setSourceIdParams(document.getData());
                        sourceCompleted = true;
                        break;
                    }
                } else {
                    sourceCompleted = false;
                }
                checkResponses();
            }
        };

        private OnCompleteListener<QuerySnapshot> resourceCompleteListener = new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    resourceCompleted = false;
                    for (DocumentSnapshot document : task.getResult()) {
                        //First result matching the query is used
                        ravenResource.setResourceIdParams(document.getData());
                        resourceCompleted = true;
                        break;
                    }
                } else {
                    resourceCompleted = false;
                }
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
                cache.put(data.toString(), ravenResource);
            } else {
                //Else we notify failure to the user
                parseCompleteListener.onParseFailed(new Exception());
            }
        }

    }
}
