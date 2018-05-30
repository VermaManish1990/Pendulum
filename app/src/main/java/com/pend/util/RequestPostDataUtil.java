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

    /**
     * Method to get Request Parameters for Sign Up Api
     *
     * @param userFullName userFullName
     * @param userEmail userEmail
     * @param userPhone userPhone
     * @param userPassword userPassword
     * @return JsonObject
     */
    public static JsonObject signUpApiRegParam(String userFullName,String userEmail,String userPhone,String userPassword) {
        LoggerUtil.v(RequestPostDataUtil.class.getSimpleName(), "loginApiRegParam");

        JsonObject requestParameters = new JsonObject();
        requestParameters.addProperty("userFullName", userFullName);
        requestParameters.addProperty("userEmail", userEmail);
        requestParameters.addProperty("userPhone", userPhone);
        requestParameters.addProperty("userPassword", userPassword);

        LoggerUtil.d(RequestPostDataUtil.class.getSimpleName(), requestParameters.toString());
        return requestParameters;
    }
}
