package com.pend.interfaces;

public interface IWebServices {

    String BASE_URL = "http://api.thewoodsfurnitures.com/";


    String REQUEST_SIGN_UP_URL = BASE_URL + "api/UserSignUp";
    String REQUEST_LOGIN_URL = BASE_URL + "api/UserLogin";
    String REQUEST_UPDATE_USER_PROFILE_URL = BASE_URL + "api/UpdateUserProfile";
    String REQUEST_GET_USER_PROFILE_URL = BASE_URL + "api/GetUserProfile";
    String REQUEST_UPDATE_USER_SEETING_URL = BASE_URL + "api/UpdateUserSettings";
    String REQUEST_USER_LOGOUT_URL = BASE_URL + "api/UserLogout";
    String REQUEST_USER_ONLINE_URL = BASE_URL + "api/UserOnline";
    String REQUEST_FORGOT_PASSWORD_URL = BASE_URL + "api/ForgotPassword";
    String REQUEST_ADD_USER_IMAGE_URL = BASE_URL + "api/AddUserImage";
    String REQUEST_DELETE_USER_IMAGE_URL = BASE_URL + "api/DeleteUserImage";
    String REQUEST_SET_USER_IMAGE_URL = BASE_URL + "api/SetUserImage";
    String REQUEST_GET_USER_TIME_SHEET_URL = BASE_URL + "api/GetUserTimeSheet";

}
