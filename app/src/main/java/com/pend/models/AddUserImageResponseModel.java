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

        public AddUserImageDetails(int userID, int imageID, String imageUrl, String imageName, boolean isProfileImage) {
            this.userID = userID;
            this.imageID = imageID;
            this.imageUrl = imageUrl;
            this.imageName = imageName;
            this.isProfileImage = isProfileImage;
        }
    }
}
