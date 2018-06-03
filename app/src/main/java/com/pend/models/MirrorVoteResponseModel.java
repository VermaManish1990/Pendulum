package com.pend.models;

import com.pend.BaseResponseModel;

import java.io.Serializable;

public class MirrorVoteResponseModel extends BaseResponseModel {
    public MirrorVoteData Data;

    public static class MirrorVoteData implements Serializable{
        public MirrorVoteDetails voteData;
    }

    public static class MirrorVoteDetails implements Serializable{
        public int mirrorAdmirePer;
        public int mirrorHatePer;
        public int mirrorCantSayPer;

        public boolean mirrorAdmire;
        public boolean mirrorHate;
        public boolean mirrorCantSay;
    }
}
