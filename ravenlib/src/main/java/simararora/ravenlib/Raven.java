package simararora.ravenlib;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

/**
 * Created by Simar Arora on 15/03/18.
 */

public class Raven {

    private static final String TAG = Raven.class.getSimpleName();
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

    public static void parse(Intent data) {

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
