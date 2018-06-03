package com.pend.models;

import com.pend.BaseResponseModel;

import java.io.Serializable;
import java.util.ArrayList;

public class GetReflectionUsersResponseModel extends BaseResponseModel {
    public GetReflectionUsersData Data;

    public static class GetReflectionUsersData implements Serializable{
        public ArrayList<GetReflectionUsersDetails> usersData;
        public boolean hasNextPage;
    }

    public static class GetReflectionUsersDetails implements Serializable{
        public int userID;

        public String userFullName;

        public boolean mirrorAdmire;
        public boolean mirrorHate;
        public boolean mirrorCantSay;
    }
}
