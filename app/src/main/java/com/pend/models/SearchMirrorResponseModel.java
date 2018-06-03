package com.pend.models;

import com.pend.BaseResponseModel;

import java.io.Serializable;
import java.util.ArrayList;

public class SearchMirrorResponseModel extends BaseResponseModel {
    public SearchMirrorData Data;

    public static class SearchMirrorData implements Serializable{
        public ArrayList<SearchMirrorDetails> searchData;
    }

    private static class SearchMirrorDetails implements Serializable{
        public int mirrorUniqueID;
        public String mirrorName;
        public String mirrorInfo;
        public String mirrorWikiLink;
        public String imageUrl;
    }
}
