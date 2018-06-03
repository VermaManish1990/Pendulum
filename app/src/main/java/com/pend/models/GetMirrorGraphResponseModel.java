package com.pend.models;

import com.pend.BaseResponseModel;

import java.io.Serializable;
import java.util.ArrayList;

public class GetMirrorGraphResponseModel extends BaseResponseModel {
    public GetMirrorGraphData Data;

    public static class GetMirrorGraphData implements Serializable{
        public ArrayList<GetMirrorGraphDetails> graphData;
    }

    public static class GetMirrorGraphDetails implements Serializable{
        public int mirrorID;
        public int percentage;
        public String createdDate;
    }
}
