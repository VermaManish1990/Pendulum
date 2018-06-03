package com.pend.models;

import com.pend.BaseResponseModel;

import java.io.Serializable;

public class GetMirrorDetailsResponseModel extends BaseResponseModel {
    public GetMirrorDetailsData Data;

    public static class GetMirrorDetailsData implements Serializable{
        public MirrorData mirrorData;
    }

    private static class MirrorData implements Serializable{
        public int mirrorID;
        public int mirrorAdmirePer;
        public int mirrorHatePer;
        public int mirrorCantSayPer;
        public int admireCount;
        public int hateCount;
        public int cantsayCount;

        public String mirrorName;
        public String imageURL;
        public String mirrorInfo;
        public String mirrorWikiLink;

        public boolean mirrorAdmire;
        public boolean mirrorHate;
        public boolean mirrorCantSay;
        public boolean isFollow;
        public boolean isExitPoll;
    }
}
