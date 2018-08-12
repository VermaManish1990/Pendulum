package com.pend.arena.api;

import com.pend.PendulumApplication;
import com.pend.interfaces.Constants;
import com.pend.interfaces.IWebServices;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


/**
 * Created by unoiaAndroid on 3/18/2017.
 */

public class ApiClient {
    private static Retrofit retrofit = null;

    /*static Gson gson = new GsonBuilder()
            .setLenient()
            .create();*/

    public static Retrofit getClient() {
        if (retrofit==null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(IWebServices.BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(ApiClient.okClient())
                    .build();
        }
        return retrofit;
    }

    private static OkHttpClient okClient() {
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        return new OkHttpClient.Builder()
                 .addInterceptor(interceptor)
                  .addInterceptor(new Interceptor() {
                    @Override
                    public Response intercept(Chain chain) throws IOException {
                        Request request = chain.request().newBuilder()
                                .addHeader(Constants.PARAM_SECURITY_KEY, PendulumApplication.securityKey)
                                .addHeader(Constants.PARAM_DEVICE_ID,PendulumApplication.deviceId)
                                .build();
                        return chain.proceed(request);
                    }
                })
                .connectTimeout(2, TimeUnit.MINUTES)
                .writeTimeout(1, TimeUnit.MINUTES)
                .readTimeout(1, TimeUnit.MINUTES)
                .build();
    }
}
