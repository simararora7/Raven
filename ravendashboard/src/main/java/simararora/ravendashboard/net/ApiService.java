package simararora.ravendashboard.net;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;
import simararora.ravendashboard.model.CreateModel;

/**
 * Created by Simar Arora on 15/03/18.
 */

public interface ApiService {

    @POST("api/generate_resource_id")
    Call<String> createResource(@Body CreateModel object);

    @POST("api/generate_source_id")
    Call<String> createSource(@Body CreateModel object);
}
