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

public class UserAgentQuery extends Query<Map<String, Integer>> {

    @Override
    public void execute(final QueryCompleteListener<Map<String, Integer>> queryCompleteListener) {
        getDocumentReference().collection("Interactions").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    Map<String, Integer> resultMap = new HashMap<>();
                    for (DocumentSnapshot documentSnapshot : task.getResult()) {
                        Map<String, Object> documentData = documentSnapshot.getData();
                        String userAgent = (String) documentData.get("userAgentRaw");
                        if (userAgent != null){
                            if (resultMap.containsKey(userAgent)){
                                resultMap.put(userAgent, resultMap.get(userAgent) + 1);
                            }else{
                                resultMap.put(userAgent, 1);
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