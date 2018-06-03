package com.pend.models;

import com.pend.BaseResponseModel;

import java.io.Serializable;

public class GetExitPollMirrorResponseModel extends BaseResponseModel {
    public GetExitPollMirrorData Data;

    public static class GetExitPollMirrorData implements Serializable{
        public GetExitPollMirrorDetails mirrorData;
    }

    public static class GetExitPollMirrorDetails implements Serializable{
        public int userID;
        public int mirrorID;

        public String mirrorName;
        public String imageURL;
        public String mirrorInfo;
        public String mirrorWikiLink;
        public String userFullName;
    }
}
