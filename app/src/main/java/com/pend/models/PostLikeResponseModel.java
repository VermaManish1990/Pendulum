package com.pend.models;

import com.pend.BaseResponseModel;

import java.io.Serializable;

public class PostLikeResponseModel extends BaseResponseModel {
    public PostLikeData Data;

    public static class PostLikeData implements Serializable {
        public PostLikeDetails likeData;
    }

    public static class PostLikeDetails implements Serializable {
        public int userID;
        public int postID;
        public int likeCount;
        public int unlikeCount;

        public boolean isLike;
        public boolean isUnLike;
    }
}
