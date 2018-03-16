package simararora.ravendashboard.queries;

import android.support.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by nateshrelhan on 3/16/18.
 */

public class QueryBot extends Query<Map<String, Integer>> {
    public static final String splitIdentifier_ = "$";
    private static final String inputDateIdentifier = "date";
    private static final String[] inputUserIdentifier = {"user", "clickedby", "who", "openedby", "by"};

    enum QueryType {
        DATE,
        OPENED_BY
    }

    private QueryType queryType;
    private String value;

    public QueryBot(String input) {
        input = input.toLowerCase();
        if (input.contains(inputDateIdentifier)) {
            queryType = QueryType.DATE;
        } else if (input.contains("openedby")) {
            queryType = QueryType.OPENED_BY;
        }
        this.value = input.substring(input.indexOf(splitIdentifier_) + 1);
    }

    @Override
    public void execute(final QueryCompleteListener<Map<String, Integer>> queryCompleteListener) {
        getDocumentReference().collection("Interactions").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    Map<String, Integer> resultMap = new HashMap<>();
                    for (DocumentSnapshot documentSnapshot : task.getResult()) {
                        Map<String, Object> documentData = documentSnapshot.getData();
                        switch (queryType) {
                            case DATE:
                                String date = (String) documentData.get("$date");
                                /*if (QueryBot.this.value.equals(date))
                                    resultMap.put("Resource Id", (String) documentData.get("resID"));*/
                                break;
                            case OPENED_BY:
                                String appUserID = (String) documentData.get("appUserID");
                                String resId = (String) documentData.get("resID");
                                if (appUserID != null) {
                                    appUserID = appUserID.toLowerCase();
                                    if (QueryBot.this.value.equals(appUserID))
                                        if (resultMap.containsKey(resId))
                                            resultMap.put(resId, resultMap.get(resId) + 1);
                                        else
                                            resultMap.put(resId, 1);
                                }
                                break;
                        }

                    }
                    queryCompleteListener.onSuccess(resultMap);
                } else
                    queryCompleteListener.onFailure();
            }
        });
    }
}
