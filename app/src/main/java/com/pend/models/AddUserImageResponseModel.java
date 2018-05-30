package com.pend.models;

import com.pend.BaseResponseModel;

import java.io.Serializable;

public class AddUserImageResponseModel extends BaseResponseModel {
    public AddUserImageData Data;

    private static class AddUserImageData implements Serializable {
        public AddUserImageDetails imageData;
    }

    private static class AddUserImageDetails implements Serializable{
        public int userID;
        public int imageID;
        public String imageUrl;
        public String imageName;
        public boolean isProfileImage;
    }
}
