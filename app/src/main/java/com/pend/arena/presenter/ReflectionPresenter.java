package com.pend.arena.presenter;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.pend.arena.api.APIError;
import com.pend.arena.api.ErrorUtils;
import com.pend.arena.model.reflections.ReflectionsResponse;
import com.pend.arena.service.ArenaService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ReflectionPresenter {

    private final Context context;
    private final ReflectionPresenter.ReflectionPresenterListener mListener;
    private final ArenaService arenaService;

    public interface ReflectionPresenterListener{
        void getReflections(ReflectionsResponse reflectionsResponse);
    }

    public ReflectionPresenter(ReflectionPresenter.ReflectionPresenterListener listener, Context context){
        this.mListener = listener;
        this.context = context;
        this.arenaService = new ArenaService();
    }

    public void getReflections(Integer userID,Integer pageNumber,String searchText){
        arenaService
                .getAPI()
                .getReflections(userID,pageNumber,searchText)
                .enqueue(new Callback<ReflectionsResponse>() {
                    @Override
                    public void onResponse(Call<ReflectionsResponse> call, Response<ReflectionsResponse> response) {
                        ReflectionsResponse result = response.body();

                        if(response.isSuccessful())
                        {   mListener.getReflections(result);

                            //Toast.makeText(context,"login successful",Toast.LENGTH_LONG).show();
                        }
                        else
                        {

                            APIError error = ErrorUtils.parseError(response);
                            // … and use it to show error information

                            // … or just log the issue like we’re doing :)
                            if (error != null && error.getError() != null && error.getError().getAdditionalMessage() != null) {

                                Log.d("error message", error.getError().getAdditionalMessage());
                                Toast.makeText(context, error.getError().getAdditionalMessage(), Toast.LENGTH_LONG).show();
                            }

                            mListener.getReflections(result);
                        }
                        //Toast.makeText(context,"login unsuccessful",Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onFailure(Call<ReflectionsResponse> call, Throwable t) {
                        try {
                            throw  new InterruptedException("Error");
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }
}
