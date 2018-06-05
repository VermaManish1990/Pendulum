package com.pend.models;

import com.pend.BaseResponseModel;

import java.io.Serializable;
import java.util.ArrayList;

public class GetTrendingAndIntroducedMirrorResponseModel extends BaseResponseModel {
    public GetTrendingAndIntroducedMirrorData Data;

    public static class GetTrendingAndIntroducedMirrorData implements Serializable{
        public ArrayList<GetTrendingAndIntroducedMirrorDetails> mirrorList;
        public boolean hasNextPage;
    }

    public static class GetTrendingAndIntroducedMirrorDetails implements Serializable{
        public int mirrorID;
        public int newPost;

        public int mirrorName;
        public int imageURL;
        public int mirrorInfo;
        public int mirrorWikiLink;
    }
}
