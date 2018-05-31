package com.pend.models;

import com.pend.BaseResponseModel;

import java.io.Serializable;

public class AddUserImageResponseModel extends BaseResponseModel {
    public AddUserImageData Data;

    public static class AddUserImageData implements Serializable {
        public AddUserImageDetails imageData;
    }

    public static class AddUserImageDetails implements Serializable{
        public int userID;
        public int imageID;
        public String imageUrl;
        public String imageName;
        public boolean isProfileImage;
    }
}
