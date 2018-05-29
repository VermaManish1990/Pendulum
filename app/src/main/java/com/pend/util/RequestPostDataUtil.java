package com.pend.util;

import com.google.gson.JsonObject;

public class RequestPostDataUtil {

    /**
     * Method to get Request Parameters for Login Api
     *
     * @param username unique username id
     * @param password password
     * @return JsonObject
     */
    public static JsonObject loginApiRegParam(String username,String password) {
        LoggerUtil.v(RequestPostDataUtil.class.getSimpleName(), "loginApiRegParam");

        JsonObject requestParameters = new JsonObject();
        requestParameters.addProperty("Username", username);
        requestParameters.addProperty("Password", password);

        LoggerUtil.d(RequestPostDataUtil.class.getSimpleName(), requestParameters.toString());
        return requestParameters;
    }
}
