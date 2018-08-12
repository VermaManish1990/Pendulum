package com.pend.arena.presenter;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.pend.arena.api.APIError;
import com.pend.arena.api.ErrorUtils;
import com.pend.arena.model.Message;
import com.pend.arena.model.get_chat_room.GetChatRoomResponse;
import com.pend.arena.model.user_chat.SendMessageResponse;
import com.pend.arena.model.user_chat.UserData;
import com.pend.arena.service.ArenaService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChatPresenter {

    private final Context context;
    private final ChatPresenter.ChatPresenterListener mListener;
    private final ArenaService arenaService;

    public interface ChatPresenterListener{
        void getUserChat(UserData userData);
        void sendMessage(SendMessageResponse sendMessageResponse);
        void getNewMessage(UserData userData);
        void getChatRoomID(GetChatRoomResponse getChatRoomResponse);
    }

    public ChatPresenter(ChatPresenter.ChatPresenterListener listener, Context context){
        this.mListener = listener;
        this.context = context;
        this.arenaService = new ArenaService();
    }

    public void getUserChat(Integer userID,Integer selectedUserID,Integer chatRoomID,
                               Integer pageNumber){
        arenaService
                .getAPI()
                .getUserChat(userID,selectedUserID,chatRoomID,pageNumber)
                .enqueue(new Callback<UserData>() {
                    @Override
                    public void onResponse(Call<UserData> call, Response<UserData> response) {
                        UserData result = response.body();

                        if(response.isSuccessful())
                        {   mListener.getUserChat(result);

                            //Toast.makeText(context,"login successful",Toast.LENGTH_LONG).show();
                        }
                        else
                        {

                            APIError error = ErrorUtils.parseError(response);
                            // … and use it to show error information

                            // … or just log the issue like we’re doing :)
                            Log.d("error message", error.getError().getAdditionalMessage());
                           // Toast.makeText(context,error.getError().getAdditionalMessage(),Toast.LENGTH_LONG).show();

                            mListener.getUserChat(result);
                        }
                        //Toast.makeText(context,"login unsuccessful",Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onFailure(Call<UserData> call, Throwable t) {
                        try {
                            throw  new InterruptedException("Error");
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }


    public void sendMessage(Message message){
        arenaService
                .getAPI()
                .sendMessage(message)
                .enqueue(new Callback<SendMessageResponse>() {
                    @Override
                    public void onResponse(Call<SendMessageResponse> call, Response<SendMessageResponse> response) {
                        SendMessageResponse result = response.body();

                        if(response.isSuccessful())
                        {   mListener.sendMessage(result);

                            //Toast.makeText(context,"login successful",Toast.LENGTH_LONG).show();
                        }
                        else
                        {

                            APIError error = ErrorUtils.parseError(response);
                            // … and use it to show error information

                            // … or just log the issue like we’re doing :)
                            if (error != null && error.getError() != null && error.getError().getAdditionalMessage() != null) {

                                Log.d("error message", error.getError().getAdditionalMessage());
                            }
                            mListener.sendMessage(result);
                        }
                        //Toast.makeText(context,"login unsuccessful",Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onFailure(Call<SendMessageResponse> call, Throwable t) {
                        try {

                            Log.d("error message", t.getLocalizedMessage());
                            Log.d("error message", t.getMessage());

                            throw  new InterruptedException("Error");
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }


    public void getNewMessage(Integer userID,Integer selectedUserID,Integer chatRoomID){
        arenaService
                .getAPI()
                .getNewMessage(userID,selectedUserID,chatRoomID)
                .enqueue(new Callback<UserData>() {
                    @Override
                    public void onResponse(Call<UserData> call, Response<UserData> response) {
                        UserData result = response.body();

                        if(response.isSuccessful())
                        {   mListener.getNewMessage(result);

                            //Toast.makeText(context,"login successful",Toast.LENGTH_LONG).show();
                        }
                        else
                        {

                            APIError error = ErrorUtils.parseError(response);
                            // … and use it to show error information

                            // … or just log the issue like we’re doing :)
                            Log.d("error message", error.getError().getAdditionalMessage());
                            /*Toast.makeText(context,error.getError().getAdditionalMessage(),Toast.LENGTH_LONG).show();
*/
                            mListener.getNewMessage(result);
                        }
                        //Toast.makeText(context,"login unsuccessful",Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onFailure(Call<UserData> call, Throwable t) {
                        try {
                            throw  new InterruptedException("Error");
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }


    public void getChatRoomID(Integer userID,Integer selectedUserID){
        arenaService
                .getAPI()
                .getChatRoomID(userID,selectedUserID)
                .enqueue(new Callback<GetChatRoomResponse>() {
                    @Override
                    public void onResponse(Call<GetChatRoomResponse> call, Response<GetChatRoomResponse> response) {
                        GetChatRoomResponse result = response.body();

                        if(response.isSuccessful())
                        {   mListener.getChatRoomID(result);

                            //Toast.makeText(context,"login successful",Toast.LENGTH_LONG).show();
                        }
                        else
                        {

                            APIError error = ErrorUtils.parseError(response);
                            // … and use it to show error information

                            // … or just log the issue like we’re doing :)
                            Log.d("error message", error.getError().getAdditionalMessage());
                            /*Toast.makeText(context,error.getError().getAdditionalMessage(),Toast.LENGTH_LONG).show();
                             */
                            mListener.getChatRoomID(result);
                        }
                        //Toast.makeText(context,"login unsuccessful",Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onFailure(Call<GetChatRoomResponse> call, Throwable t) {
                        try {
                            throw  new InterruptedException("Error");
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }
}
