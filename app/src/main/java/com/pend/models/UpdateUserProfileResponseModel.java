package com.pend.models;

import com.pend.BaseResponseModel;

import java.io.Serializable;

public class UpdateUserProfileResponseModel extends BaseResponseModel {
    public UpdateUserProfileData Data;

    public static class UpdateUserProfileData implements Serializable{
        public UpdateUserProfileDetails userData;
    }

    public static class UpdateUserProfileDetails {
        public int userAge;
        public int cityID;
        public String userID;
        public String userFullName;
        public String userGender;
        public String address;
    }
}
