package com.pend.models;

import com.pend.BaseResponseModel;

import java.io.Serializable;

public class DeleteUserImageResponseModel extends BaseResponseModel {
    public DeleteUserImageData Data;

    public static class DeleteUserImageData implements Serializable {
        public DeleteUserImageDetails imageData;
    }

    public static class DeleteUserImageDetails implements Serializable{
        public int userID;
        public int imageID;
    }
}
