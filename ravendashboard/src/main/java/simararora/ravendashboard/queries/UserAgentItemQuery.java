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

public class UserAgentItemQuery extends Query<Map<String, Integer>> {

    private String userAgentItem;
    private String secondaryKey;

    public UserAgentItemQuery(String userAgentItem) {
        this.userAgentItem = userAgentItem;
    }

    public UserAgentItemQuery(String userAgentItem, String secondaryKey) {
        this.userAgentItem = userAgentItem;
        this.secondaryKey = secondaryKey;
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
                        Map<String, Object> userAgentData = (Map<String, Object>) documentData.get("userAgentParced");
                        if (userAgentData != null) {
                            String item = (String) userAgentData.get(userAgentItem);
                            if (item != null) {
                                if (secondaryKey != null && item.equalsIgnoreCase("unknown")) {
                                    item = (String) userAgentData.get(secondaryKey);
                                    if (item != null) {
                                        item = getDisplayName(item);
                                    }
                                }
                                if (item != null && !item.equalsIgnoreCase("false")) {
                                    if (resultMap.containsKey(item)) {
                                        resultMap.put(item, resultMap.get(item) + 1);
                                    } else {
                                        resultMap.put(item, 1);
                                    }
                                }
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

    private String getDisplayName(String name){
        if (name.toLowerCase().contains("slack")){
            return "Slack";
        }
        if (name.toLowerCase().contains("whatsapp")){
            return "WhatsApp";
        }
        if (name.toLowerCase().contains("twitter")){
            return "Twitter";
        }
        if (name.toLowerCase().contains("facebook")){
            return "Facebook";
        }
        if (name.toLowerCase().contains("linkedin")){
            return "LinkedIn";
        }
        return name;
    }
}


