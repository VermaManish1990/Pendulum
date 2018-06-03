package com.pend.models;

import com.pend.BaseResponseModel;

import java.io.Serializable;
import java.util.ArrayList;

public class GetIntroducedMirrorResponseModel extends BaseResponseModel {
    public GetIntroducedMirrorData Data;

    public static class GetIntroducedMirrorData implements Serializable{
        public ArrayList<GetIntroducedMirrorDetails> mirrorList;
        public boolean hasNextPage;
    }

    public static class GetIntroducedMirrorDetails implements Serializable{
        public int mirrorID;
        public int newPost;

        public String mirrorName;
        public String imageURL;
        public String mirrorInfo;
        public String mirrorWikiLink;
    }
}
