package com.pend.models;

import com.pend.BaseResponseModel;

import java.io.Serializable;
import java.util.ArrayList;

public class GetMetaDataResponseModel extends BaseResponseModel {
    public GetMetaData Data;

    public static class GetMetaData implements Serializable{
        public ArrayList<GetMetaDetails> metaData;
    }

    public static class GetMetaDetails implements Serializable{
        public String keyID;
        public String value;
        public String lastModifiedDatetime;
    }
}
