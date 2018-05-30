package com.pend.models;

import com.pend.BaseResponseModel;

import java.io.Serializable;
import java.util.ArrayList;

public class UserTimeSheetResponseModel extends BaseResponseModel {
    public UserTimeSheetData Data;

    private static class UserTimeSheetData implements Serializable{
        public ArrayList<UserTimeSheetDetails> timeSheetData;
        public boolean hasNextPage;
    }

    private static class UserTimeSheetDetails implements Serializable{
        public int postID;
        public int mirrorid;
        public String createdDatetime;
        public String vote;
        public String type;
        public String mirrorName;
    }
}
