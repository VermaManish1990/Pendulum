package com.pend.models;

import com.pend.BaseResponseModel;

import java.io.Serializable;

public class LoginResponseModel extends BaseResponseModel{
    public LoginResponseData Data;

    private static class LoginResponseData implements Serializable {
        public UserDetails userData;
    }

    private static class UserDetails implements Serializable{
        public int userID;
        public String userFullName;
        public String userEmail;
        public String userPhone;
        public String userName;
    }
}
