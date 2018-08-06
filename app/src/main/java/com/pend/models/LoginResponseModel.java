package com.pend.models;

import com.pend.BaseResponseModel;

import java.io.Serializable;
import java.util.ArrayList;

public class LoginResponseModel extends BaseResponseModel{
    public LoginResponseData Data;

    public static class LoginResponseData implements Serializable {
        public UserDetails userData;
        public ArrayList<ImageDetails> imageData;
    }

    public static class UserDetails implements Serializable{
        public int userID;
        public String userFullName;
        public String userEmail;
        public String userPhone;
        public String userName;
    }

    public static class ImageDetails implements Serializable{
        public int imageID;
        public String imageName;
        public String imageURL;
        public boolean isProfileImage;
    }
}
