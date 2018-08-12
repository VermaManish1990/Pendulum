package com.pend.arena;

import android.content.Context;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by chaudhary on 3/17/2018.
 */

public class MetaDataPresenter {

    private final Context context;
    private final MetaDataPresenterListener mListener;
    private final MetaDataService metaDataService;

    public interface MetaDataPresenterListener{
        void loginUser(MetaDataResponse metaDataResponse);
        void updateLocation(LocationResponse locationResponse);
    }

    public MetaDataPresenter(MetaDataPresenterListener listener, Context context){
        this.mListener = listener;
        this.context = context;
        this.metaDataService = new MetaDataService();
    }

    public void metaData(Integer userId){
        metaDataService
                .getAPI()
                .getMetaData(userId)
                .enqueue(new Callback<MetaDataResponse>() {
                    @Override
                    public void onResponse(Call<MetaDataResponse> call, Response<MetaDataResponse> response) {
                        MetaDataResponse result = response.body();

                        if(result != null)
                        {   mListener.loginUser(result);

                            //Log.e("UserData firstname",result.getUser().getFirstname());
                        }
                        else
                        {

                        }
                            //Toast.makeText(context,"login unsuccessful",Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onFailure(Call<MetaDataResponse> call, Throwable t) {
                        try {
                            throw  new InterruptedException("Error");
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }


    public void updateLocation(LocationVM locationVM){
        metaDataService
                .getAPI()
                .userLocation(locationVM)
                .enqueue(new Callback<LocationResponse>() {
                    @Override
                    public void onResponse(Call<LocationResponse> call, Response<LocationResponse> response) {
                        LocationResponse result = response.body();

                        if(result != null)
                        {   mListener.updateLocation(result);

                            //Log.e("UserData firstname",result.getUser().getFirstname());
                        }
                        else
                        {

                        }
                        //Toast.makeText(context,"login unsuccessful",Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onFailure(Call<LocationResponse> call, Throwable t) {
                        try {
                            throw  new InterruptedException("Error");
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }
}
