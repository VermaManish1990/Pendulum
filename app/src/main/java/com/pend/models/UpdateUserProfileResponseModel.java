package com.pend.models;

import com.pend.BaseResponseModel;

import java.io.Serializable;

public class UpdateUserProfileResponseModel extends BaseResponseModel {
    public UpdateUserProfileData Data;

    private static class UpdateUserProfileData implements Serializable{
        public UpdateUserProfileDetails userData;
    }

    private static class UpdateUserProfileDetails {
        public int userAge;
        public int cityID;
        public String userID;
        public String userFullName;
        public String userGender;
        public String address;
    }
}
