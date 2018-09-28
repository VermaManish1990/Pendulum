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
    public static JsonObject updateUserProfileApiRegParam(int userID, String userFullName, int userAge, String userGender, int cityID, String address) {
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

    /**
     * Method is used to get Request Parameters for Create Mirror Api.
     *
     * @param userID         userID
     * @param mirrorUniqueID mirrorUniqueID
     * @param mirrorName     mirrorName
     * @param imageUrl       imageUrl
     * @param mirrorInfo     mirrorInfo
     * @param mirrorWikiLink mirrorWikiLink
     * @return JsonObject
     */
    public static JsonObject createMirrorApiRegParam(int userID, String mirrorUniqueID, String mirrorName, String imageUrl,
                                                     String mirrorInfo, String mirrorWikiLink) {
        LoggerUtil.v(RequestPostDataUtil.class.getSimpleName(), "createMirrorApiRegParam");

        JsonObject requestParameters = new JsonObject();
        requestParameters.addProperty("userID", userID);
        requestParameters.addProperty("mirrorUniqueID", mirrorUniqueID);
        requestParameters.addProperty("mirrorName", mirrorName);
        requestParameters.addProperty("imageUrl", imageUrl);
        requestParameters.addProperty("mirrorInfo", mirrorInfo);
        requestParameters.addProperty("mirrorWikiLink", mirrorWikiLink);

        LoggerUtil.d(RequestPostDataUtil.class.getSimpleName(), requestParameters.toString());
        return requestParameters;
    }

    /**
     * Method is used to get Request Parameters for Mirror Vote Api.
     *
     * @param userID        userID
     * @param mirrorID      mirrorID
     * @param mirrorAdmire  mirrorAdmire
     * @param mirrorHate    mirrorHate
     * @param mirrorCantSay mirrorCantSay
     * @return JsonObject
     */
    public static JsonObject mirrorVoteApiRegParam(int userID, int mirrorID, boolean mirrorAdmire, boolean mirrorHate,
                                                   boolean mirrorCantSay) {
        LoggerUtil.v(RequestPostDataUtil.class.getSimpleName(), "mirrorVoteApiRegParam");

        JsonObject requestParameters = new JsonObject();
        requestParameters.addProperty("userID", userID);
        requestParameters.addProperty("mirrorID", mirrorID);
        requestParameters.addProperty("mirrorAdmire", mirrorAdmire);
        requestParameters.addProperty("mirrorHate", mirrorHate);
        requestParameters.addProperty("mirrorCantSay", mirrorCantSay);

        LoggerUtil.d(RequestPostDataUtil.class.getSimpleName(), requestParameters.toString());
        return requestParameters;
    }

    /**
     * Method is used to get Request Parameters for Add Comment Api.
     *
     * @param userID      userID
     * @param postID      postID
     * @param commentText commentText
     * @return JsonObject
     */
    public static JsonObject addCommentApiRegParam(int userID, int postID, String commentText) {
        LoggerUtil.v(RequestPostDataUtil.class.getSimpleName(), "addCommentApiRegParam");

        JsonObject requestParameters = new JsonObject();
        requestParameters.addProperty("userID", userID);
        requestParameters.addProperty("postID", postID);
        requestParameters.addProperty("commentText", commentText);

        LoggerUtil.d(RequestPostDataUtil.class.getSimpleName(), requestParameters.toString());
        return requestParameters;
    }

    /**
     * Method is used to get Request Parameters for Update Comment Api.
     *
     * @param userID      userID
     * @param postID      postID
     * @param commentID   commentID
     * @param commentText commentText
     * @return JsonObject
     */
    public static JsonObject updateCommentApiRegParam(int userID, int postID, int commentID, String commentText) {
        LoggerUtil.v(RequestPostDataUtil.class.getSimpleName(), "updateCommentApiRegParam");

        JsonObject requestParameters = new JsonObject();
        requestParameters.addProperty("userID", userID);
        requestParameters.addProperty("postID", postID);
        requestParameters.addProperty("commentID", commentID);
        requestParameters.addProperty("commentText", commentText);

        LoggerUtil.d(RequestPostDataUtil.class.getSimpleName(), requestParameters.toString());
        return requestParameters;
    }

    /**
     * Method is used to get Request Parameters for Remove Comment Api.
     *
     * @param userID    userID
     * @param postID    postID
     * @param commentID commentID
     * @return JsonObject
     */
    public static JsonObject removeCommentApiRegParam(int userID, int postID, int commentID) {
        LoggerUtil.v(RequestPostDataUtil.class.getSimpleName(), "removeCommentApiRegParam");

        JsonObject requestParameters = new JsonObject();
        requestParameters.addProperty("userID", userID);
        requestParameters.addProperty("postID", postID);
        requestParameters.addProperty("commentID", commentID);

        LoggerUtil.d(RequestPostDataUtil.class.getSimpleName(), requestParameters.toString());
        return requestParameters;
    }

    /**
     * Method is used to get Request Parameters for Post Like Api.
     *
     * @param userID   userID
     * @param postID   postID
     * @param isLike   isLike
     * @param isUnLike isUnLike
     * @return JsonObject
     */
    public static JsonObject postLikeApiRegParam(int userID, int postID, boolean isLike, boolean isUnLike) {
        LoggerUtil.v(RequestPostDataUtil.class.getSimpleName(), "postLikeApiRegParam");

        JsonObject requestParameters = new JsonObject();
        requestParameters.addProperty("userID", userID);
        requestParameters.addProperty("postID", postID);
        requestParameters.addProperty("isLike", isLike);
        requestParameters.addProperty("isUnLike", isUnLike);

        LoggerUtil.d(RequestPostDataUtil.class.getSimpleName(), requestParameters.toString());
        return requestParameters;
    }

    /**
     * Method is used to get Request Parameters for Add Post Api.
     *
     * @param userID   userID
     * @param mirrorID mirrorID
     * @param postInfo postInfo
     * @param image
     * @return JsonObject
     */
    public static JsonObject addPostApiRegParam(int userID, int mirrorID, String postInfo, String image) {
        LoggerUtil.v(RequestPostDataUtil.class.getSimpleName(), "addPostApiRegParam");

        JsonObject requestParameters = new JsonObject();
        requestParameters.addProperty("userID", userID);
        requestParameters.addProperty("mirrorID", mirrorID);
        requestParameters.addProperty("postInfo", postInfo);
        requestParameters.addProperty("image", image);

        LoggerUtil.d(RequestPostDataUtil.class.getSimpleName(), requestParameters.toString());
        return requestParameters;
    }

    /**
     * Method is used to get Request Parameters for Update Post Api.
     *
     * @param userID     userID
     * @param postID     postID
     * @param mirrorID   mirrorID
     * @param postInfo   postInfo
     * @param image      image
     * @param isNewImage isNewImage
     * @param isOldImage isOldImage
     * @return JsonObject
     */
    public static JsonObject updatePostApiRegParam(int userID, int postID, int mirrorID, String postInfo, String image,
                                                   boolean isNewImage, boolean isOldImage) {
        LoggerUtil.v(RequestPostDataUtil.class.getSimpleName(), "updatePostApiRegParam");

        JsonObject requestParameters = new JsonObject();
        requestParameters.addProperty("userID", userID);
        requestParameters.addProperty("postID", postID);
        requestParameters.addProperty("mirrorID", mirrorID);
        requestParameters.addProperty("postInfo", postInfo);
        requestParameters.addProperty("image", image);
        requestParameters.addProperty("isNewImage", isNewImage);
        requestParameters.addProperty("isOldImage", isOldImage);

        LoggerUtil.d(RequestPostDataUtil.class.getSimpleName(), requestParameters.toString());
        return requestParameters;
    }

    /**
     * Method is used to get Request Parameters for Delete Post Api.
     *
     * @param userID   userID
     * @param postID   postID
     * @param mirrorID mirrorID
     * @return JsonObject
     */
    public static JsonObject removePostApiRegParam(int userID, int postID, int mirrorID) {
        LoggerUtil.v(RequestPostDataUtil.class.getSimpleName(), "removePostApiRegParam");

        JsonObject requestParameters = new JsonObject();
        requestParameters.addProperty("userID", userID);
        requestParameters.addProperty("postID", postID);
        requestParameters.addProperty("mirrorID", mirrorID);

        LoggerUtil.d(RequestPostDataUtil.class.getSimpleName(), requestParameters.toString());
        return requestParameters;
    }

    /**
     * Method is used to get Request Parameters for Exit Poll Vote Api.
     *
     * @param userID      userID
     * @param mirrorID    mirrorID
     * @param exitPollID  exitPollID
     * @param pollAdmire  pollAdmire
     * @param pollHate    pollHate
     * @param pollCantSay pollCantSay
     * @return JsonObject
     */
    public static JsonObject exitPollVoteApiRegParam(int userID, int mirrorID, int exitPollID, boolean pollAdmire,
                                                     boolean pollHate, boolean pollCantSay) {
        LoggerUtil.v(RequestPostDataUtil.class.getSimpleName(), "exitPollVoteApiRegParam");

        JsonObject requestParameters = new JsonObject();
        requestParameters.addProperty("userID", userID);
        requestParameters.addProperty("mirrorID", mirrorID);
        requestParameters.addProperty("exitPollID", exitPollID);
        requestParameters.addProperty("pollAdmire", pollAdmire);
        requestParameters.addProperty("pollHate", pollHate);
        requestParameters.addProperty("pollCantSay", pollCantSay);

        LoggerUtil.d(RequestPostDataUtil.class.getSimpleName(), requestParameters.toString());
        return requestParameters;
    }

    /**
     * Method is used to get Request Parameters for Create Contest Api.
     *
     * @param userID          userID
     * @param contestTypeID   contestTypeID
     * @param relatedMirrorID relatedMirrorID
     * @param questionText    questionText
     * @param option1MirrorID option1MirrorID
     * @param option2MirrorID option2MirrorID
     * @param option3MirrorID option3MirrorID
     * @param option1Text     option1Text
     * @param option2Text     option2Text
     * @param option3Text     option3Text
     * @param image           image
     * @return JsonObject
     */
    public static JsonObject createContestReqParam(int userID, int contestTypeID, int relatedMirrorID, String questionText,
                                                   int option1MirrorID, int option2MirrorID, int option3MirrorID, String option1Text,
                                                   String option2Text, String option3Text, String image) {
        LoggerUtil.v(RequestPostDataUtil.class.getSimpleName(), "createContestReqParam");

        JsonObject requestParameters = new JsonObject();
        requestParameters.addProperty("userID", userID);
        requestParameters.addProperty("contestTypeID", contestTypeID);
        requestParameters.addProperty("relatedMirrorID", relatedMirrorID);
        requestParameters.addProperty("questionText", questionText);
        requestParameters.addProperty("option1MirrorID", option1MirrorID);
        requestParameters.addProperty("option2MirrorID", option2MirrorID);
        requestParameters.addProperty("option3MirrorID", option3MirrorID);
        requestParameters.addProperty("option1Text", option1Text);
        requestParameters.addProperty("option2Text", option2Text);
        requestParameters.addProperty("option3Text", option3Text);
        requestParameters.addProperty("image", image);

        LoggerUtil.d(RequestPostDataUtil.class.getSimpleName(), requestParameters.toString());
        return requestParameters;
    }

    /**
     * Method is used to get Request Parameters for Contest Vote Api.
     *
     * @param userID        userID
     * @param contestID     contestID
     * @param contestTypeID contestTypeID
     * @param option1       option1
     * @param option2       option2
     * @param option3       option3
     * @return JsonObject
     */
    public static JsonObject contestVoteApiRegParam(int userID, int contestID, int contestTypeID, boolean option1,
                                                    boolean option2, boolean option3) {
        LoggerUtil.v(RequestPostDataUtil.class.getSimpleName(), "contestVoteApiRegParam");

        JsonObject requestParameters = new JsonObject();
        requestParameters.addProperty("userID", userID);
        requestParameters.addProperty("contestID", contestID);
        requestParameters.addProperty("contestTypeID", contestTypeID);
        requestParameters.addProperty("option1", option1);
        requestParameters.addProperty("option2", option2);
        requestParameters.addProperty("option3", option3);

        LoggerUtil.d(RequestPostDataUtil.class.getSimpleName(), requestParameters.toString());
        return requestParameters;
    }
}
