package com.pend.arena;

import com.pend.arena.api.ApiClient;
import com.pend.arena.api.URL;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * Created by chaudhary on 3/17/2018.
 */

public class MetaDataService {

    public interface  MetaDataAPI{

        @GET(URL.META_DATA_URL)
        Call<MetaDataResponse> getMetaData(@Query("userID") Integer userID);

        @POST(URL.USER_LOCATION)
        Call<LocationResponse> userLocation(@Body LocationVM locationVM);

    }

    public MetaDataAPI getAPI(){

        Retrofit retrofit = ApiClient.getClient();
        return retrofit.create(MetaDataAPI.class);
    }
}
