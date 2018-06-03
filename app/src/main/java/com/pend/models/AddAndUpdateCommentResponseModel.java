package com.pend.models;

import com.pend.BaseResponseModel;

import java.io.Serializable;

public class AddAndUpdateCommentResponseModel extends BaseResponseModel {
    public AddAndUpdateCommentData Data;

    public static class AddAndUpdateCommentData implements Serializable{
        public AddAndUpdateCommentDetails commentData;
    }

    public static class AddAndUpdateCommentDetails implements Serializable{
        public int userID;
        public int postID;
        public int commentID;

        public String commentText;
        public String userFullName;
        public String imageName;
        public String commentUserImageURL;
        public String createdDatetime;
    }
}
