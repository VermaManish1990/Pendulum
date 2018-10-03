package com.pend.models;

import com.pend.BaseResponseModel;

import java.io.Serializable;

public class CreateContestResponseModel extends BaseResponseModel {

    public CreateContestData Data;

    public class CreateContestData implements Serializable{
        public CreateContestDetails responseData;
    }

    public static class CreateContestDetails implements Serializable{
        public int userID;
        public int contestID;
        public int contestTypeID;
        public int relatedMirrorID;
        public int option1MirrorID;
        public int option2MirrorID;
        public int option3MirrorID;

        public String questionText;
        public String option1Text;
        public String option2Text;
        public String option3Text;
        public String imageUrl;
        public String imageName;
    }
}
