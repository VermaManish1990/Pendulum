package com.pend.models;

import com.pend.BaseResponseModel;

import java.io.Serializable;

public class ErrorResponseModel extends BaseResponseModel {
    public ErrorData error;

    public static class ErrorData implements Serializable{
        public String messageCode;
        public String message;
        public String additionalMessage;
    }
}
