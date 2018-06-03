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

        public int commentText;
        public int userFullName;
        public int imageName;
        public int commentUserImageURL;
        public int createdDatetime;
    }
}
