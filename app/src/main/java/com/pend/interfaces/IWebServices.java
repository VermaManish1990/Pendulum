package com.pend.interfaces;

public interface IWebServices {

//    String BASE_URL = "http://pendulumapp-dev.ap-south-1.elasticbeanstalk.com/";
   // String BASE_URL = "http://103.87.174.201:1015/";
String BASE_URL = "http://3.6.114.114:943/";

    String REQUEST_SIGN_UP_URL = BASE_URL + "api/UserSignUp";
    String REQUEST_LOGIN_URL = BASE_URL + "api/UserLogin";
    String REQUEST_UPDATE_USER_PROFILE_URL = BASE_URL + "api/UpdateUserProfile";
    String REQUEST_GET_USER_PROFILE_URL = BASE_URL + "api/GetUserProfile?";
    String REQUEST_UPDATE_USER_SEETING_URL = BASE_URL + "api/UpdateUserSettings";
    String REQUEST_USER_LOGOUT_URL = BASE_URL + "api/UserLogout";
    String REQUEST_USER_ONLINE_URL = BASE_URL + "api/UserOnline";
    String REQUEST_FORGOT_PASSWORD_URL = BASE_URL + "api/ForgotPassword";
    String REQUEST_ADD_USER_IMAGE_URL = BASE_URL + "api/AddUserImage";
    String REQUEST_DELETE_USER_IMAGE_URL = BASE_URL + "api/DeleteUserImage";
    String REQUEST_SET_USER_IMAGE_URL = BASE_URL + "api/SetUserImage";
    String REQUEST_GET_USER_TIME_SHEET_URL = BASE_URL + "api/GetUserTimeSheet?";

    String REQUEST_GET_TRENDING_URL = BASE_URL + "api/GetTrendingsMirrors?";
    String REQUEST_GET_FOLLOWING_URL = BASE_URL + "api/GetFollowingMirrors?";
    String REQUEST_GET_INTRODUCED_URL = BASE_URL + "api/GetIntroducedMirrors?";
    String REQUEST_GET_MIRROR_GRAPH_DATA_URL = BASE_URL + "api/GetMirrorGraphData?";
    String REQUEST_GET_MIRROR_DETAILS_URL = BASE_URL + "api/GetMirrorDetails?";
    String REQUEST_SEARCH_MIRROR_URL = BASE_URL + "api/SearchMirror?";
    String REQUEST_CREATE_MIRROR_URL = BASE_URL + "api/CreateMirror";

    String REQUEST_GET_META_DATA_URL = BASE_URL + "api/GetMetaData?";

    String REQUEST_MIRROR_VOTE_URL = BASE_URL + "api/MirrorVote";

    String REQUEST_SEARCH_NEWS_FEED_MIRROR_URL = BASE_URL + "api/SearchNewsFeedMirror?";

    String REQUEST_GET_POST_COMMENT_URL = BASE_URL + "api/GetPostComments?";
    String REQUEST_ADD_COMMENT_URL = BASE_URL + "api/AddComment";
    String REQUEST_UPDATE_COMMENT_URL = BASE_URL + "api/UpdateComment";
    String REQUEST_REMOVE_COMMENT_URL = BASE_URL + "api/RemoveComment";

    String REQUEST_POST_LIKE_URL = BASE_URL + "api/PostLike";

    String REQUEST_GET_POSTS_URL = BASE_URL + "api/GetPosts?";
    String REQUEST_GET_POST_DETAIL_URL = BASE_URL + "api/GetPostDetails?";
    String REQUEST_ADD_POST_URL = BASE_URL + "api/AddPost";
    String REQUEST_UPDATE_POST_URL = BASE_URL + "api/UpdatePost";
    String REQUEST_REMOVE_POST_URL = BASE_URL + "api/RemovePost";

    String REQUEST_GET_EXIT_POLL_MIRROR_URL = BASE_URL + "api/GetExitPollMirror?";
    String REQUEST_GET_EXIT_POLL_LIST_URL = BASE_URL + "api/GetExitPollList?";
    String REQUEST_EXIT_POLL_VOTE_URL = BASE_URL + "api/ExitPollVote";
    String REQUEST_GET_REFLECTION_USERS_URL = BASE_URL + "api/GetReflectionUsers?";

    String REQUEST_CREATE_CONTEST_URL = BASE_URL + "api/CreateContest";
    String REQUEST_GET_INTRODUCED_CONTESTS_URL = BASE_URL + "api/GetIntroducedContests?";
    String REQUEST_GET_TRENDING_CONTESTS_URL = BASE_URL + "api/GetTrendingContests?";
    String REQUEST_GET_FOLLOWING_CONTESTS_URL = BASE_URL + "api/GetFollowingContests?";
    String REQUEST_CONTESTS_VOTE_URL = BASE_URL + "api/ContestVote";

    String REQUEST_GET_NEWS_FEED_DATA=BASE_URL+"/api/GetNewsFeedDataForUser?";

    String REQUEST_SEND_OTP=BASE_URL+"/api/SendOTP";
    String REQUEST_VERIFY_OTP=BASE_URL+"/api/VerifyOTP";


    String REQUEST_FORGOT_PASSWORD_SEND_OTP=BASE_URL+"/api/ForgotPasswordSendOtp";
    String REQUEST_FORGOT_PASSWORD_VERIFY_OTP=BASE_URL+"/api/ForgotPasswordVerifyOtp";
    String REQUEST_FORGOT_PASSWORD=BASE_URL+"/api/ForgotPassword";

    String REQUEST_LOGIN_WITH_FB=BASE_URL+"/api/LoginWithFB";

}
