package com.pend.models;

import com.pend.BaseResponseModel;

import java.io.Serializable;
import java.util.ArrayList;

public class SearchInNewsFeedResponseModel extends BaseResponseModel {

    public SearchInNewsFeedData Data;

    public static class SearchInNewsFeedData implements Serializable{
        public ArrayList<MirrorDetails> mirrorList;
        public boolean hasNextPage;
    }

    public static class MirrorDetails implements Serializable{
        public int mirrorID;
        public int activeUsers;

        public String mirrorName;
        public String imageURL;
        public String mirrorInfo;
        public String mirrorWikiLink;

        public boolean mirrorAdmire;
        public boolean mirrorHate;
        public boolean mirrorCantSay;
    }
}
