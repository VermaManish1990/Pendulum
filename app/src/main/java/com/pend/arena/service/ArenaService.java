package com.pend.arena.service;

import com.pend.arena.api.ApiClient;
import com.pend.arena.api.URL;
import com.pend.arena.model.Message;
import com.pend.arena.model.get_chat_room.GetChatRoomResponse;
import com.pend.arena.model.recent_chat.RecentChatsResponse;
import com.pend.arena.model.reflections.ReflectionsResponse;
import com.pend.arena.model.search_user.SearchUserResponse;
import com.pend.arena.model.user_chat.SendMessageResponse;
import com.pend.arena.model.user_chat.UserData;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public class ArenaService {

    public interface ArenaAPI {

        @GET(URL.GET_RECENT_CHATS)
        Call<RecentChatsResponse> getRecentChats(@Query("userID") Integer userId, @Query("pageNumber") Integer pageNumber,
                                                 @Query("searchText") String searchText);

        @GET(URL.GET_USER_CHAT)
        Call<UserData> getUserChat(@Query("userID") Integer userID,
                                   @Query("selectedUserID") Integer selectedUserID,
                                   @Query("chatRoomID") Integer chatRoomID,
                                   @Query("pageNumber") Integer pageNumber);

        @GET(URL.GET_NEW_MESSAGE)
        Call<UserData> getNewMessage(@Query("userID") Integer userID,
                                     @Query("selectedUserID") Integer selectedUserID,
                                     @Query("chatRoomID") Integer chatRoomID);

        @POST(URL.SEND_MESSAGE)
        Call<SendMessageResponse> sendMessage(@Body Message message);


        @GET(URL.GET_REFLECTIONS)
        Call<ReflectionsResponse> getReflections(@Query("userID") Integer userId, @Query("pageNumber") Integer pageNumber,
                                                 @Query("searchText") String searchText);


        @GET(URL.GET_CHAT_ID)
        Call<GetChatRoomResponse> getChatRoomID(@Query("userID") Integer userId,
                                                @Query("selectedUserID") Integer selectedUserID);

        @GET(URL.SEARCH_USER)
        Call<SearchUserResponse> searchUser(@Query("userID") Integer userId,
                                            @Query("pageNumber") Integer pageNumber, @Query("latitude") Double latitude,
                                            @Query("longitude") Double longitude, @Query("distanceFrom") Integer distanceFrom
                , @Query("distanceTo") Integer distanceTo,
                                            @Query("ageTo") Integer ageTo, @Query("ageFrom") Integer ageFrom,
                                            @Query("sex") String sex);


    }

    public ArenaService.ArenaAPI getAPI() {

        Retrofit retrofit = ApiClient.getClient();
        return retrofit.create(ArenaService.ArenaAPI.class);
    }
}
