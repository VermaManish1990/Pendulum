package com.pend.models;

import com.pend.BaseResponseModel;

import java.io.Serializable;

public class UpdateUserSettingResponseModel extends BaseResponseModel {
    public UpdateUserSettingData Data;

    public static class UpdateUserSettingData implements Serializable{
        public UpdateUserSettingDetails userData;
    }

    public static class UpdateUserSettingDetails implements Serializable{
        public int userID;
        public String userEmail;
        public String userPhone;
        public String userPassword;
        public boolean isShowMeInOpenSearch;
        public boolean isVisibleInReflection;
    }
}
