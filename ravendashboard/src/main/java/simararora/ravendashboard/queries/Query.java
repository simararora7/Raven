package simararora.ravendashboard.queries;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

/**
 * Created by Simar Arora on 16/03/18.
 */

public abstract class Query<T> {

    private String clientId = "zcUDoLdHnKLfjlIFd9bu";

    protected DocumentReference getDocumentReference(){
        return FirebaseFirestore.getInstance().document(String.format("Clients/%s/", clientId));
    }

    abstract void execute(QueryCompleteListener<T> queryCompleteListener);

    public interface QueryCompleteListener<T>{

        void onSuccess(T result);

        void onFailure();
    }
}
