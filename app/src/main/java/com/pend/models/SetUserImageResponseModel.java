package com.pend.models;

import com.pend.BaseResponseModel;

import java.io.Serializable;

public class SetUserImageResponseModel extends BaseResponseModel {
    public SetUserImageData Data;

    private static class SetUserImageData implements Serializable {
        public SetUserImageDetails imageData;
    }

    private static class SetUserImageDetails implements Serializable{
        public int userID;
        public int imageID;
        public boolean isProfileImage;
    }
}
