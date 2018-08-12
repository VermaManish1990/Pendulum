package com.pend.arena.presenter;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.pend.arena.api.APIError;
import com.pend.arena.api.ErrorUtils;
import com.pend.arena.model.search_user.SearchUserResponse;
import com.pend.arena.service.ArenaService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SearchUserPresenter {

    private final Context context;
    private final SearchUserPresenter.SearchUserPresenterListener mListener;
    private final ArenaService arenaService;

    public interface SearchUserPresenterListener{
        void searchUser(SearchUserResponse searchUserResponse);

    }

    public SearchUserPresenter(SearchUserPresenter.SearchUserPresenterListener listener, Context context){
        this.mListener = listener;
        this.context = context;
        this.arenaService = new ArenaService();
    }

    public void searchUser(Integer userID,Integer pageNumber,Double latitude,
               Double longitude,Integer distanceFrom,Integer distanceTo,Integer ageTo,Integer ageFrom,
                               String sex){
        arenaService
                .getAPI()
                .searchUser(userID,pageNumber,latitude,longitude,distanceFrom,distanceTo,ageTo,ageFrom,sex)
                .enqueue(new Callback<SearchUserResponse>() {
                    @Override
                    public void onResponse(Call<SearchUserResponse> call, Response<SearchUserResponse> response) {
                        SearchUserResponse result = response.body();

                        if(response.isSuccessful())
                        {   mListener.searchUser(result);

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

                            mListener.searchUser(result);
                        }
                        //Toast.makeText(context,"login unsuccessful",Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onFailure(Call<SearchUserResponse> call, Throwable t) {
                        try {
                            throw  new InterruptedException("Error");
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }
}
