package com.pend.interfaces;

public interface Constants {


    String SECURITY_CONSTANT = "PendulumApp";

    //Intent Keys
    String USER_DETAILS_KEY = "USER_DETAILS_KEY";
    String USER_DATA_MODEL_KEY = "USER_DATA_MODEL_KEY";

    //Header Keys
    String PARAM_SECURITY_KEY = "SecurityKey";
    String PARAM_DEVICE_ID = "DeviceID";
    String PARAM_USER_ID = "userID";
    String PREF_DEVICE_ID = "device_id";
    String PREF_PAGE_NUMBER = "pageNumber";

    //Volley Error Message
    String NoConnectionError = "Network Failure";
    String AuthFailureError = "Authentication Failure";
    String NetworkError = "Network Error";
    String ParseError = "Parse Error";
    String ServerError = "Server Error";
    String TimeoutError = "Timeout Error";

    // Validation Message
    String EMAIL_PATTERN = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
    String EMAIL_ID_VALIDATION_ERROR = "please enter Email Address";
    String NOT_VALID_EMAIL_ID_ERROR = "please enter valid Email Address";
    String PASSWORD_VALIDATION_ERROR = "please enter PASSWORD";
    String NAME_VALIDATION_ERROR = "please enter Name";
    String MOBILE_NUMBER_VALIDATION_ERROR = "please enter Phone Number";
    String NOT_VALID_MOBILE_NUMBER_ERROR = "please enter valid Phone Number";


}
