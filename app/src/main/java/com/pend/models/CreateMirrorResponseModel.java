package com.pend.models;

import com.pend.BaseResponseModel;

import java.io.Serializable;

public class CreateMirrorResponseModel extends BaseResponseModel {
    public CreateMirrorData Data;

    public static class CreateMirrorData implements Serializable{
        public CreateMirrorDetails mirrorData;
    }

    public static class CreateMirrorDetails implements Serializable{
        public int userID;
        public int mirrorID;
        public int admireCount;
        public int hateCount;
        public int cantsayCount;

        public String mirrorUniqueID;
        public String mirrorName;
        public String imageName;
        public String imageURL;
        public String mirrorInfo;
        public String mirrorWikiLink;
        public String createdDatetime;

        public boolean isExists;
        public boolean isActive;
    }
}
