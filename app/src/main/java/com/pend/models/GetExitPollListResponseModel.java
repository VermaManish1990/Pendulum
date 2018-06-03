package com.pend.models;

import com.pend.BaseResponseModel;

import java.io.Serializable;
import java.util.ArrayList;

public class GetExitPollListResponseModel extends BaseResponseModel {
    public GetExitPollListData Data;

    public static class GetExitPollListData implements Serializable{
        public ArrayList<GetExitPollListDetails> exitPollList;
        public boolean hasNextPage;
    }

    public static class GetExitPollListDetails implements Serializable{
        public int mirrorID;
        public int exitPollID;
        public int pollAdmirePer;
        public int pollHatePer;
        public int pollCantSayPer;

        public String exitPollText;

        public boolean pollAdmire;
        public boolean pollHate;
        public boolean pollCantSay;
    }
}
