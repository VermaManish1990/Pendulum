package com.pend.models;

import com.pend.BaseResponseModel;

import java.io.Serializable;
import java.util.ArrayList;

public class SignupResponseModel extends BaseResponseModel{
    public SignupResponseModel.SignupResponseData Data;

    public static class SignupResponseData implements Serializable {
        public LoginResponseModel.UserDetails user;
        public ArrayList<LoginResponseModel.ImageDetails> imageData;
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
