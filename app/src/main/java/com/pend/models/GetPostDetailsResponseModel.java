package com.pend.models;

import com.pend.BaseResponseModel;

import java.io.Serializable;

public class GetPostDetailsResponseModel extends BaseResponseModel {
    public GetPostDetailsData Data;

    public static class GetPostDetailsData implements Serializable {
        public GetPostDetails postData;
    }

    public static class GetPostDetails implements Serializable {
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

        public boolean isLike;
        public boolean isUnLike;
    }
}
