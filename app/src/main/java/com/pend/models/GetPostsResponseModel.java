package com.pend.models;

import com.pend.BaseResponseModel;

import java.io.Serializable;
import java.util.ArrayList;

public class GetPostsResponseModel extends BaseResponseModel {
    public GetPostsData Data;

    public static class GetPostsData implements Serializable{
        public ArrayList<GetPostsDetails> postList;
        public boolean hasNextPage;
    }

    public static class GetPostsDetails implements Serializable{
        public int userID;
        public int postID;
        public int commentCount;
        public int likeCount;
        public int unlikeCount;

        public String postInfo;
        public String imageName;
        public String imageURL;
        public String createdDatetime;
        public String userFullName;
        public String commentUserFullName;
        public String commentUserImageName;
        public String commentUserImageURL;
        public String commentText;
        public String userImageName;
        public String userImageNameURL;

        public boolean isLike;
        public boolean isUnLike;
    }
}
