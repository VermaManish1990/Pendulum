package com.pend.models;

import com.pend.BaseResponseModel;

import java.io.Serializable;
import java.util.ArrayList;

public class GetFollowingMirrorResponseModel extends BaseResponseModel {
    public GetFollowingMirrorData Data;

    public static class GetFollowingMirrorData implements Serializable {
        public ArrayList<GetFollowingMirrorDetails> mirrorList;
        public boolean hasNextPage;
    }

    public static class GetFollowingMirrorDetails implements Serializable {
        public int mirrorID;
        public int activeUsers;

        public String mirrorName;
        public String imageURL;
        public String mirrorInfo;
        public String mirrorWikiLink;

        public boolean mirrorAdmire;
        public boolean mirrorHate;
        public boolean mirrorCantSay;

        public GetFollowingMirrorDetails(int activeUsers, String mirrorName, String imageURL) {
            this.activeUsers = activeUsers;
            this.mirrorName = mirrorName;
            this.imageURL = imageURL;
        }
    }
}
