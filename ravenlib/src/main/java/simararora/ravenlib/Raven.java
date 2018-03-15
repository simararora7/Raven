package simararora.ravenlib;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import simararora.ravenlib.model.RavenResource;

/**
 * Created by Simar Arora on 15/03/18.
 */

public class Raven {
    private static final String TAG = Raven.class.getSimpleName();
    private static final String authority = "www.google.com";
    private static Raven mRaven;
    private Context mContext;
    private PrefHelper mPrefHelper;

    private Raven(Context mContext) {
        this.mContext = mContext;
        this.mPrefHelper = PrefHelper.getInstance(this.mContext);
    }

    public static void init(Context context) {
        if (mRaven == null) {
            mRaven = new Raven(context);
            String ravenClientId = mRaven.mPrefHelper.readRavenClientId();
            boolean isNewRavenClientIdSet;
            if (ravenClientId == null || ravenClientId.equalsIgnoreCase(PrefHelper.NO_STRING_VALUE)) {
                Log.i(TAG, "Raven Warning: Please enter your raven client id in your project's Manifest file!");
            } else
                isNewRavenClientIdSet = mRaven.mPrefHelper.setRavenClientId(ravenClientId);

        }
        FirebaseApp.initializeApp(context);
    }


    public static RavenResource parse(Intent intent) {
        return parse(intent, null);
    }

    public static RavenResource parse(Intent intent, ParseCompleteListener parseCompleteListener) {
        if (intent == null) {
            if (parseCompleteListener != null)
                parseCompleteListener.onParseFailed(new NullPointerException());
            return null;
        }
        return parse(intent.getData(), null);
    }

    public static RavenResource parse(Uri data) {
        return parse(data, null);
    }

    public static RavenResource parse(Uri data, ParseCompleteListener parseCompleteListener) {
        if (data == null) {
            if (parseCompleteListener != null)
                parseCompleteListener.onParseFailed(new NullPointerException());
            return null;
        }
        String authority = data.getAuthority();
        if (!Raven.authority.equals(authority)) {
            if (parseCompleteListener != null)
                parseCompleteListener.onParseFailed(new Exception("Authority Mismatch"));
            return null;
        }

        String path = data.getPath();

        try {
            RavenResource ravenResource = new RavenResource(path);
            if (parseCompleteListener != null)
                fetchResourceDetails(ravenResource, parseCompleteListener);
            return ravenResource;
        } catch (Exception e) {
            if (parseCompleteListener != null)
                parseCompleteListener.onParseFailed(e);
            return null;
        }
    }

    private static void fetchResourceDetails(RavenResource ravenResource, ParseCompleteListener parseCompleteListener) {

    }

    public static void testRead() {
        DocumentReference documentReference = FirebaseFirestore.getInstance().document("test/user");
        documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                Log.d("Simar", "onComplete: " + task.isSuccessful());
                Log.d("Simar", "onComplete: " + task.getResult().toString());
            }
        });
    }
}
