package com.pend.arena.presenter;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.pend.arena.api.APIError;
import com.pend.arena.api.ErrorUtils;
import com.pend.arena.model.recent_chat.RecentChatsResponse;
import com.pend.arena.service.ArenaService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ArenaHomePresenter {

    private final Context context;
    private final ArenaHomePresenter.ArenaHomePresenterListener mListener;
    private final ArenaService arenaService;

    public interface ArenaHomePresenterListener {
        void getRecentChats(RecentChatsResponse recentChatsResponse);

    }

    public ArenaHomePresenter(ArenaHomePresenter.ArenaHomePresenterListener listener, Context context) {
        this.mListener = listener;
        this.context = context;
        this.arenaService = new ArenaService();
    }

    public void getRecentChats(Integer userID, Integer pageNumber, String searchText) {
        arenaService
                .getAPI()
                .getRecentChats(userID, pageNumber, searchText)
                .enqueue(new Callback<RecentChatsResponse>() {
                    @Override
                    public void onResponse(Call<RecentChatsResponse> call, Response<RecentChatsResponse> response) {
                        RecentChatsResponse result = response.body();

                        if (response.isSuccessful()) {
                            mListener.getRecentChats(result);

                            //Toast.makeText(context,"login successful",Toast.LENGTH_LONG).show();
                        } else {

                            APIError error = ErrorUtils.parseError(response);
                            // … and use it to show error information

                            // … or just log the issue like we’re doing :)
                            if (error != null && error.getError() != null && error.getError().getAdditionalMessage() != null) {

                                Log.d("error message", error.getError().getAdditionalMessage());
                                Toast.makeText(context, error.getError().getAdditionalMessage(), Toast.LENGTH_LONG).show();
                            }

                            mListener.getRecentChats(result);
                        }
                        //Toast.makeText(context,"login unsuccessful",Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onFailure(Call<RecentChatsResponse> call, Throwable t) {
                        try {
                            throw new InterruptedException("Error");
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }
}
