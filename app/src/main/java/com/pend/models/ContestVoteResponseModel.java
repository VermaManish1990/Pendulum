package com.pend.models;

import com.pend.BaseResponseModel;

import java.io.Serializable;

public class ContestVoteResponseModel extends BaseResponseModel {

    public ContestVoteData Data;

    public static class ContestVoteData implements Serializable{
        public ContestVoteDetails voteData;
    }

    public static class ContestVoteDetails implements Serializable{
        public int Option1Per;
        public int Option2Per;
        public int Option3Per;

        public boolean Option1;
        public boolean Option2;
        public boolean Option3;
    }
}
