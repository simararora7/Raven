package simararora.ravendashboard.queries;

import android.support.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by nateshrelhan on 3/16/18.
 */

public class QueryBot extends Query<Map<String, Integer>> {
    public static final String splitIdentifier_ = "$";

    enum QueryType {
        DATE,
        OPENED_BY,
        RESOURCE_OPENED
    }

    private QueryType queryType;
    private String value;
    private boolean queryTypeFound = false;

    public boolean isQueryTypeFound() {
        return queryTypeFound;
    }

    public QueryBot(String input) {
        String[] inputSplit = input.split(" ");
        if (inputSplit.length == 1)
            inputSplit = input.split("$");

        for (String specificInput : inputSplit) {
            if (!queryTypeFound) {
                switch (specificInput) {
                    case "date":
                        queryTypeFound = true;
                        queryType = QueryType.DATE;
                        break;
                    case "clickedby":
                    case "openedby":
                    case "by":
                    case "opened":
                    case "clicked":
                    case "who":
                        queryTypeFound = true;
                        queryType = QueryType.OPENED_BY;
                        break;
                    case "resource":
                    case "id":
                    case "resourceid":
                    case "resource opened":
                        queryTypeFound = true;
                        queryType = QueryType.RESOURCE_OPENED;
                        break;
                }
            } else break;
        }
        if (queryTypeFound)
            this.value = input.substring(input.indexOf(splitIdentifier_) + 1);
    }

    @Override
    public void execute(final QueryCompleteListener<Map<String, Integer>> queryCompleteListener) {
        getDocumentReference().collection("Interactions").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    Map<String, Integer> resultMap = new HashMap<>();
                    switch (queryType) {
                        case DATE:
                            for (DocumentSnapshot documentSnapshot : task.getResult()) {
                                Map<String, Object> documentData = documentSnapshot.getData();
                                Long key = (Long) documentData.get("$date");
                                String data = (String) documentData.get("resID");
                                if (key != null && data != null) {
                                    try {
                                        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy"); // here set the pattern as you date in string was containing like date/month/year
                                        Date dateOfValue = sdf.parse(QueryBot.this.value);
                                        Date fetchedDate = new Date(key);
                                        fetchedDate = sdf.parse(sdf.format(fetchedDate));
                                        if (dateOfValue.compareTo(fetchedDate) == 0)
                                            if (resultMap.containsKey(data))
                                                resultMap.put(data, resultMap.get(data) + 1);
                                            else
                                                resultMap.put(data, 1);
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                        return;
                                    }
                                }
                            }
                            break;
                        case OPENED_BY:
                            for (DocumentSnapshot documentSnapshot : task.getResult()) {
                                Map<String, Object> documentData = documentSnapshot.getData();
                                String key = (String) documentData.get("appUserID");
                                String data = (String) documentData.get("resID");
                                if (key != null && data != null) {
                                    if (QueryBot.this.value.equals(key.toLowerCase())) {
                                        if (resultMap.containsKey(data))
                                            resultMap.put(data, resultMap.get(data) + 1);
                                        else
                                            resultMap.put(data, 1);
                                    }
                                }
                            }
                            break;
                        default:
                            return;
                    }
                    queryCompleteListener.onSuccess(resultMap);
                } else
                    queryCompleteListener.onFailure();
            }
        });
    }
}
