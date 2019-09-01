package com.pend.models;

import com.pend.BaseResponseModel;

import java.io.Serializable;
import java.util.ArrayList;

public class GetContestsResponseModel extends BaseResponseModel {

    public GetContestsData Data;

    public static class GetContestsData implements Serializable{
        public ArrayList<GetContestDetails> contestList;
        public boolean hasNextPage;
    }

    public static class GetContestDetails implements Serializable{
        public int contestType;
        public int contestID;
        public int contestTypeID;

        public int createdUserID;
        public String createdUserName;

        public String questionText;
        public String imageName;
        public String imageURL;

        public int relatedMirrorID;
        public String relatedMirrorName;
        public String relatedMirrorImageURL;

        public int option1MirrorID;
        public String option1MirrorName;
        public String option1MirrorImageURL;

        public int option2MirrorID;
        public String option2MirrorName;
        public String option2MirrorImageURL;

        public int option3MirrorID;
        public String option3MirrorName;
        public String option3MirrorImageURL;

        public String option1Text;
        public String option2Text;
        public String option3Text;

        public boolean option1Vote;
        public boolean option2Vote;
        public boolean option3Vote;

        public int option1Per;
        public int option2Per;
        public int option3Per;

        public int option1MirrorIDCount;
        public int option2MirrorIDCount;
        public int option3MirrorIDCount;

        public int option1TextCount;
        public int option2TextCount;
        public int option3TextCount;

        public int TypeOneOptions;
        public int typeTwoOptions;
    }
}
