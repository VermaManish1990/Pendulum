package com.pend.models;

import com.pend.BaseResponseModel;

import java.io.Serializable;
import java.util.ArrayList;

public class GetPostCommentsResponseModel extends BaseResponseModel {
    public GetPostCommentsData Data;

    public static class GetPostCommentsData implements Serializable{
        public ArrayList<GetPostCommentsDetails> commentList;
        public boolean hasNextPage;
    }

    public static class GetPostCommentsDetails implements Serializable{
        public int userID;
        public int commentID;

        public String commentText;
        public String userFullName;
        public String imageName;
        public String commentUserImageURL;
        public String createdDatetime;

        public GetPostCommentsDetails(int userID, int commentID, String commentText, String userFullName, String imageName,
                                      String commentUserImageURL, String createdDatetime) {
            this.userID = userID;
            this.commentID = commentID;
            this.commentText = commentText;
            this.userFullName = userFullName;
            this.imageName = imageName;
            this.commentUserImageURL = commentUserImageURL;
            this.createdDatetime = createdDatetime;
        }
    }
}
