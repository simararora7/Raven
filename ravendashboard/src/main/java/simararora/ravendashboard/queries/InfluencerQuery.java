package simararora.ravendashboard.queries;

import android.support.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Simar Arora on 16/03/18.
 */

/**

 new InfluencerQuery().execute(new Query.QueryCompleteListener<Map<String, Integer>>() {
@Override
public void onSuccess(Map<String, Integer> result) {
for (String key: result.keySet())
Log.d("Simar", "onSuccess: " + key + " " + result.get(key));
}

@Override
public void onFailure() {
Log.d("Simar", "onFailure: ");
}
});

 */

public class InfluencerQuery extends Query<Map<String, Integer>> {

    @Override
    public void execute(final QueryCompleteListener<Map<String, Integer>> queryCompleteListener) {
        getDocumentReference().collection("Interactions").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    Map<String, Integer> resultMap = new HashMap<>();
                    for (DocumentSnapshot documentSnapshot : task.getResult()) {
                        Map<String, Object> documentData = documentSnapshot.getData();
                        Map<String, Object> sourceData = (Map<String, Object>) documentData.get("sourceData");
                        String userId = (String) sourceData.get("$userId");
                        if (userId != null){
                            if (resultMap.containsKey(userId)){
                                resultMap.put(userId, resultMap.get(userId) + 1);
                            }else{
                                resultMap.put(userId, 1);
                            }
                        }
                    }
                    queryCompleteListener.onSuccess(resultMap);
                } else {
                    queryCompleteListener.onFailure();
                }
            }
        });
    }
}


