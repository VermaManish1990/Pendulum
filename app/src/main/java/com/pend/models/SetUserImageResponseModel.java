package com.pend.models;

import com.pend.BaseResponseModel;

import java.io.Serializable;

public class SetUserImageResponseModel extends BaseResponseModel {
    public SetUserImageData Data;

    public static class SetUserImageData implements Serializable {
        public SetUserImageDetails imageData;
    }

    public static class SetUserImageDetails implements Serializable{
        public int userID;
        public int imageID;
        public boolean isProfileImage;
    }
}
