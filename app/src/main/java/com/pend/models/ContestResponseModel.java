package com.pend.models;

import com.pend.BaseResponseModel;

import java.io.Serializable;
import java.util.ArrayList;

public class ContestResponseModel extends BaseResponseModel {

    public ContestData Data;

    public static class ContestData implements Serializable {
        public ArrayList<ContestDetails> contestData;
        public boolean hasNextPage;
    }

    public static class ContestDetails implements Serializable {
        public int userId;
        public int contestId;
        public int commentCount;
        public int type;

        public int mirror1Per;
        public int mirror2Per;
        public int mirror3Per;

        public String mirror1Name;
        public String mirror2Name;
        public String mirror3Name;

        public String mirror1ImageUrl;
        public String mirror2ImageUrl;
        public String mirror3ImageUrl;

        public String createdBy;
        public String contestInfo;

        public String userName;
        public String userImageUrl;

        public ContestDetails(int type, int mirror1Per, int mirror2Per, int mirror3Per) {
            this.type = type;
            this.mirror1Per = mirror1Per;
            this.mirror2Per = mirror2Per;
            this.mirror3Per = mirror3Per;
        }
    }
}
