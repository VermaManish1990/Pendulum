package com.pend.interfaces;

public interface Constants {


    String SECURITY_CONSTANT = "PendulumApp";

    //Intent Keys
    String USER_DETAILS_KEY = "USER_DETAILS_KEY";
    String USER_DATA_MODEL_KEY = "USER_DATA_MODEL_KEY";
    String MIRROR_ID_KEY = "MIRROR_ID_KEY";

    //Header Keys
    String PARAM_SECURITY_KEY = "SecurityKey";
    String PARAM_DEVICE_ID = "DeviceID";

    //Query parameter keys
    String PARAM_USER_ID = "userID";
    String PARAM_MIRROR_ID = "mirrorID";
    String PARAM_POST_ID = "postID";
    String PARAM_PAGE_NUMBER = "pageNumber";
    String PARAM_SEARCH_TEXT = "searchText";
    String PARAM_MONTH = "month";
    String PARAM_YEAR = "year";

    //Volley Error Message
    String NoConnectionError = "Network Failure";
    String AuthFailureError = "Authentication Failure";
    String NetworkError = "Network Error";
    String ParseError = "Parse Error";
    String ServerError = "Server Error";
    String TimeoutError = "Timeout Error";

    // Application Constant
    long SPLASH_TIME_OUT = 2000;
    int REQUEST_SELECT_IMAGE_FROM_ALBUM = 1;
    int REQUEST_TAKE_PHOTO = 2;
    int ALL_PERMISSION_REQUEST_CODE = 3;
    String EMAIL_PATTERN = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

}
