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

        public String mirrorName;
        public String imageURL;
        public String mirrorInfo;
        public String mirrorWikiLink;

        public GetTrendingAndIntroducedMirrorDetails(int mirrorID, String mirrorName, String imageURL, String mirrorInfo, String mirrorWikiLink) {
            this.mirrorID = mirrorID;
            this.mirrorName = mirrorName;
            this.imageURL = imageURL;
            this.mirrorInfo = mirrorInfo;
            this.mirrorWikiLink = mirrorWikiLink;
        }
    }
}
