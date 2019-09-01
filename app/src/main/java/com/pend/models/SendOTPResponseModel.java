package com.pend.models;

import com.pend.BaseResponseModel;

import java.io.Serializable;

public class SendOTPResponseModel extends BaseResponseModel
{

    public SendOTPResponseModel.SendOTPResponse Data;

    public static class SendOTPResponse implements Serializable {
        public  String Status;
        public  String Details;
    }

}

