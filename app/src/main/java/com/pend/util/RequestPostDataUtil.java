package com.pend.util;

import com.google.gson.JsonObject;

public class RequestPostDataUtil {

    /**
     * Method us used to get Request Parameters for Login Api.
     *
     * @param username unique username id
     * @param password password
     * @return JsonObject
     */
    public static JsonObject loginApiRegParam(String username, String password) {
        LoggerUtil.v(RequestPostDataUtil.class.getSimpleName(), "loginApiRegParam");

        JsonObject requestParameters = new JsonObject();
        requestParameters.addProperty("Username", username);
        requestParameters.addProperty("Password", password);

        LoggerUtil.d(RequestPostDataUtil.class.getSimpleName(), requestParameters.toString());
        return requestParameters;
    }

    /**
     * Method is used to get Request Parameters for Sign Up Api.
     *
     * @param userFullName userFullName
     * @param userEmail    userEmail
     * @param userPhone    userPhone
     * @param userPassword userPassword
     * @return JsonObject
     */
    public static JsonObject signUpApiRegParam(String userFullName, String userEmail, String userPhone, String userPassword) {
        LoggerUtil.v(RequestPostDataUtil.class.getSimpleName(), "signUpApiRegParam");

        JsonObject requestParameters = new JsonObject();
        requestParameters.addProperty("userFullName", userFullName);
        requestParameters.addProperty("userEmail", userEmail);
        requestParameters.addProperty("userPhone", userPhone);
        requestParameters.addProperty("userPassword", userPassword);

        LoggerUtil.d(RequestPostDataUtil.class.getSimpleName(), requestParameters.toString());
        return requestParameters;
    }

    /**
     * Method is used to get Request Parameters for Update User Profile Api.
     *
     * @param userID       userID
     * @param userFullName userFullName
     * @param userAge      userAge
     * @param userGender   userGender
     * @param cityID       cityID
     * @param address      address
     * @return JsonObject
     */
    public static JsonObject updateUserProfileApiRegParam(int userID, String userFullName, int userAge, String userGender, int cityID,
                                                          String address) {
        LoggerUtil.v(RequestPostDataUtil.class.getSimpleName(), "updateUserProfileApiRegParam");

        JsonObject requestParameters = new JsonObject();
        requestParameters.addProperty("userID", userID);
        requestParameters.addProperty("userFullName", userFullName);
        requestParameters.addProperty("userAge", userAge);
        requestParameters.addProperty("userGender", userGender);
        requestParameters.addProperty("cityID", cityID);
        requestParameters.addProperty("address", address);

        LoggerUtil.d(RequestPostDataUtil.class.getSimpleName(), requestParameters.toString());
        return requestParameters;
    }


    /**
     * Method is used to get Request Parameters for Setting Api.
     *
     * @param userID                userID
     * @param userEmail             userEmail
     * @param userPhone             userPhone
     * @param userPassword          userPassword
     * @param isShowMeInOpenSearch  isShowMeInOpenSearch
     * @param isVisibleInReflection isVisibleInReflection
     * @return JsonObject
     */
    public static JsonObject updateUserSettingApiRegParam(int userID, String userEmail, String userPhone, String userPassword, boolean isShowMeInOpenSearch,
                                                          boolean isVisibleInReflection) {
        LoggerUtil.v(RequestPostDataUtil.class.getSimpleName(), "updateUserSettingApiRegParam");

        JsonObject requestParameters = new JsonObject();
        requestParameters.addProperty("userID", userID);
        requestParameters.addProperty("userEmail", userEmail);
        requestParameters.addProperty("userPhone", userPhone);
        requestParameters.addProperty("userPassword", userPassword);
        requestParameters.addProperty("isShowMeInOpenSearch", isShowMeInOpenSearch);
        requestParameters.addProperty("isVisibleInReflection", isVisibleInReflection);

        LoggerUtil.d(RequestPostDataUtil.class.getSimpleName(), requestParameters.toString());
        return requestParameters;
    }

    /**
     * Method is used to get Request Parameters for Logout and UserOnline Api.
     *
     * @param userID userID
     * @return JsonObject
     */
    public static JsonObject logoutOrOnlineUserApiRegParam(int userID) {
        LoggerUtil.v(RequestPostDataUtil.class.getSimpleName(), "logoutOrOnlineUserApiRegParam");

        JsonObject requestParameters = new JsonObject();
        requestParameters.addProperty("userID", userID);

        LoggerUtil.d(RequestPostDataUtil.class.getSimpleName(), requestParameters.toString());
        return requestParameters;
    }

    /**
     * Method is used to get Request Parameters for Forgot Password Api.
     *
     * @param userEmail userEmail
     * @return JsonObject
     */
    public static JsonObject forgotPasswordApiRegParam(String userEmail) {
        LoggerUtil.v(RequestPostDataUtil.class.getSimpleName(), "forgotPasswordApiRegParam");

        JsonObject requestParameters = new JsonObject();
        requestParameters.addProperty("userEmail", userEmail);

        LoggerUtil.d(RequestPostDataUtil.class.getSimpleName(), requestParameters.toString());
        return requestParameters;
    }

    /**
     * Method is used to get Request Parameters for add User Image Api.
     *
     * @param userID         userID
     * @param isProfileImage isProfileImage
     * @param image          image
     * @return JsonObject
     */
    public static JsonObject addUserImageApiRegParam(int userID, boolean isProfileImage, String image) {
        LoggerUtil.v(RequestPostDataUtil.class.getSimpleName(), "addUserImageApiRegParam");

        JsonObject requestParameters = new JsonObject();
        requestParameters.addProperty("userID", userID);
        requestParameters.addProperty("isProfileImage", isProfileImage);
        requestParameters.addProperty("image", image);

        LoggerUtil.d(RequestPostDataUtil.class.getSimpleName(), requestParameters.toString());
        return requestParameters;
    }

    /**
     * Method is used to get Request Parameters for delete User Image Api.
     *
     * @param userID  userID
     * @param imageID imageID
     * @return JsonObject
     */
    public static JsonObject deleteUserImageApiRegParam(int userID, int imageID) {
        LoggerUtil.v(RequestPostDataUtil.class.getSimpleName(), "deleteUserImageApiRegParam");

        JsonObject requestParameters = new JsonObject();
        requestParameters.addProperty("userID", userID);
        requestParameters.addProperty("imageID", imageID);

        LoggerUtil.d(RequestPostDataUtil.class.getSimpleName(), requestParameters.toString());
        return requestParameters;
    }

    /**
     * Method is used to get Request Parameters for set User Image Api.
     *
     * @param userID         userID
     * @param imageID        imageID
     * @param isProfileImage isProfileImage
     * @return JsonObject
     */
    public static JsonObject setUserImageApiRegParam(int userID, int imageID, boolean isProfileImage) {
        LoggerUtil.v(RequestPostDataUtil.class.getSimpleName(), "setUserImageApiRegParam");

        JsonObject requestParameters = new JsonObject();
        requestParameters.addProperty("userID", userID);
        requestParameters.addProperty("imageID", imageID);
        requestParameters.addProperty("isProfileImage", isProfileImage);

        LoggerUtil.d(RequestPostDataUtil.class.getSimpleName(), requestParameters.toString());
        return requestParameters;
    }
}
