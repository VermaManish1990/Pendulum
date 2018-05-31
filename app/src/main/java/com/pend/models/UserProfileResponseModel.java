package com.pend.models;

import com.pend.BaseResponseModel;

import java.io.Serializable;
import java.util.ArrayList;

public class UserProfileResponseModel extends BaseResponseModel {
    public UserProfileData Data;

    public static class UserProfileData implements Serializable{
        public UserProfileDetails userData;
        public ArrayList<ImageDetails> imageData;
    }

    public static class UserProfileDetails implements Serializable{
        public int userID;
        public int cityID;
        public int userAge;
        public String userFullName;
        public String userEmail;
        public String userPhone;
        public String userGender;
        public String cityName;
        public String address;
        public String userName;
        public boolean isShowMeInOpenSearch;
        public boolean isVisibleInReflection;
    }

    public static class ImageDetails implements Serializable{
        public int imageID;
        public String imageName;
        public String imageURL;
        public boolean isProfileImage;
    }
}
