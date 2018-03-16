package simararora.ravendashboard.queries;

import android.support.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by nateshrelhan on 3/16/18.
 */

public class QueryBot extends Query<Map<String, String>> {
    private static final String splitIdentifier_ = "$";
    private static final String inputDateIdentifier = "date";
    private static final String[] inputUserIdentifier = {"user", "clickedby", "who", "openedby", "by"};

    enum QueryType {
        DATE,
        OPENED_BY
    }

    private QueryType queryType;
    private String value;
    private boolean error = false;

    public boolean isError() {
        return error;
    }

    public QueryBot(String input) {
        if (!input.contains(splitIdentifier_) && input.indexOf(splitIdentifier_) != input.length() - 1) {
            error = true;
            return;
        }
        input = input.toLowerCase();
        if (input.contains(inputDateIdentifier)) {
            queryType = QueryType.DATE;
        } else if (Arrays.asList(inputUserIdentifier).contains(input)) {
            queryType = QueryType.OPENED_BY;
        }
        this.value = input.substring(input.indexOf(splitIdentifier_) + 1);
    }

    @Override
    void execute(final QueryCompleteListener<Map<String, String>> queryCompleteListener) {
        getDocumentReference().collection("Interactions").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    Map<String, String> resultMap = new HashMap<>();
                    for (DocumentSnapshot documentSnapshot : task.getResult()) {
                        Map<String, Object> documentData = documentSnapshot.getData();
                        switch (queryType) {
                            case DATE:
                                String date = (String) documentData.get("$date");
                                if (QueryBot.this.value.equals(date))
                                    resultMap.put("Resource Id", (String) documentData.get("resID"));
                                break;
                            case OPENED_BY:
                                String appUserID = (String) documentData.get("appUserID");
                                if (QueryBot.this.value.equals(appUserID))
                                    resultMap.put("Resource Id", (String) documentData.get("resID"));
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
