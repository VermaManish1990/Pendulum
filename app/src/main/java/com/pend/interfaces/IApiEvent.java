package com.pend.interfaces;

public interface IApiEvent {
    int REQUEST_LOGIN_CODE = 1;
    int REQUEST_SIGN_UP_CODE = 2;
    int REQUEST_UPDATE_USER_PROFILE_CODE = 3;
    int REQUEST_GET_USER_PROFILE_CODE = 4;
    int REQUEST_UPDATE_USER_SEETING_CODE = 5;
    int REQUEST_USER_LOGOUT_CODE = 6;
    int REQUEST_USER_ONLINE_CODE = 7;
    int REQUEST_FORGOT_PASSWORD_CODE = 8;
    int REQUEST_ADD_USER_IMAGE_CODE = 9;
    int REQUEST_DELETE_USER_IMAGE_CODE = 10;
    int REQUEST_SET_USER_IMAGE_CODE = 11;
    int REQUEST_GET_USER_TIME_SHEET_CODE = 12;

    int REQUEST_GET_TRENDING_CODE = 13;
    int REQUEST_GET_FOLLOWING_CODE = 14;
    int REQUEST_GET_INTRODUCED_CODE = 15;
    int REQUEST_GET_MIRROR_GRAPH_DATA_CODE = 16;
    int REQUEST_GET_MIRROR_DETAILS_CODE = 17;
    int REQUEST_SEARCH_MIRROR_CODE = 18;
    int REQUEST_CREATE_MIRROR_CODE = 19;

    int REQUEST_GET_META_DATA_CODE = 20;

    int REQUEST_MIRROR_VOTE_CODE = 21;

    int REQUEST_GET_POST_COMMENT_CODE = 22;
    int REQUEST_ADD_COMMENT_CODE = 23;
    int REQUEST_UPDATE_COMMENT_CODE = 24;
    int REQUEST_REMOVE_COMMENT_CODE = 25;

    int REQUEST_POST_LIKE_CODE = 26;

    int REQUEST_GET_POSTS_CODE = 27;
    int REQUEST_GET_POST_DETAIL_CODE = 28;
    int REQUEST_ADD_POST_CODE = 29;
    int REQUEST_UPDATE_POST_CODE = 30;
    int REQUEST_REMOVE_POST_CODE = 31;

    int REQUEST_GET_EXIT_POLL_MIRROR_CODE = 32;
    int REQUEST_GET_EXIT_POLL_LIST_CODE = 33;
    int REQUEST_EXIT_POLL_VOTE_CODE = 34;
    int REQUEST_GET_REFLECTION_USERS_CODE = 35;

    int REQUEST_SEARCH_NEWS_FEED_MIRROR_CODE = 36;

    int REQUEST_CREATE_CONTEST_CODE = 37;
    int REQUEST_GET_INTRODUCED_CONTESTS_CODE = 38;
    int REQUEST_CONTESTS_VOTE_CODE = 39;
    int REQUEST_GET_FOLLOWING_CONTESTS_CODE = 40;
    int REQUEST_GET_TRENDING_CONTESTS_CODE = 41;

    int REQUEST_GET_NEWS_FEED_DATA=42;

    int REQUEST_SEND_OTP=43;
    int REQUEST_VERIFY_OTP=44;

    int REQUEST_FORGOT_PASSWORD_SEND_OTP=45;
    int REQUEST_FORGOT_PASSWORD_VERIFY_OTP=46;
    int REQUEST_FORGOT_PASSWORD=47;
    int REQUEST_LOGIN_WITH_FB=47;
}
