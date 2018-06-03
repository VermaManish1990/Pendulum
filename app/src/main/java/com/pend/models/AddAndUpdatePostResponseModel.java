package com.pend.models;

import com.pend.BaseResponseModel;

import java.io.Serializable;

public class AddAndUpdatePostResponseModel extends BaseResponseModel {
    public AddAndUpdatePostData Data;

    public static class AddAndUpdatePostData implements Serializable{
        public AddAndUpdatePostDetails postData;
    }

    public static class AddAndUpdatePostDetails implements Serializable{
        public int userID;
        public int postID;
        public int mirrorID;
        public String postInfo;
        public String imageUrl;
    }
}
