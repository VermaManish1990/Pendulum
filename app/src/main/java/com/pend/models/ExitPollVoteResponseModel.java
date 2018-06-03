package com.pend.models;

import com.pend.BaseResponseModel;

import java.io.Serializable;

public class ExitPollVoteResponseModel extends BaseResponseModel {
    public ExitPollVoteData Data;

    public static class ExitPollVoteData implements Serializable{
        public ExitPollVoteDetails voteData;
    }

    public static class ExitPollVoteDetails implements Serializable{
        public int userID;
        public int mirrorID;
        public int exitPollID;
        public int pollAdmirePer;
        public int pollHatePer;
        public int pollCantSayPer;

        public boolean pollAdmire;
        public boolean pollHate;
        public boolean pollCantSay;
    }
}
