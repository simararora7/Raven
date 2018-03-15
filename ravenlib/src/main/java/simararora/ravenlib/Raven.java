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

    public static void init(Context context){
        FirebaseApp.initializeApp(context);
    }

    public static void parse(Intent data){

    }

    public static void testRead(){
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
