package simararora.ravendashboard;

import android.app.Application;
import android.content.Context;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;
import simararora.ravendashboard.net.ApiService;

/**
 * Created by Simar Arora on 15/03/18.
 */

public class DashboardApplication extends Application {

    private static final String API_ENDPOINT = "https://us-central1-raven-347c7.cloudfunctions.net/";
    private ApiService apiService;

    public static ApiService getAPIService(Context context) {
        DashboardApplication application = (DashboardApplication) context.getApplicationContext();
        application.initAPIService();
        return application.apiService;
    }

    private void initAPIService() {
        if (apiService == null) {
            Retrofit retrofit = new Retrofit.Builder()
                    .addConverterFactory(ScalarsConverterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create())
                    .baseUrl(API_ENDPOINT)
                    .build();
            apiService = retrofit.create(ApiService.class);
        }
    }
}
