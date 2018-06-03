package com.pend.models;

import com.pend.BaseResponseModel;

import java.io.Serializable;
import java.util.ArrayList;

public class GetTrendingMirrorResponseModel extends BaseResponseModel {
    public GetTrendingMirrorData Data;

    public static class GetTrendingMirrorData implements Serializable{
        public ArrayList<GetTrendingMirrorDetails> mirrorList;
        public boolean hasNextPage;
    }

    public static class GetTrendingMirrorDetails implements Serializable{
        public int mirrorID;
        public int newPost;

        public int mirrorName;
        public int imageURL;
        public int mirrorInfo;
        public int mirrorWikiLink;
    }
}
