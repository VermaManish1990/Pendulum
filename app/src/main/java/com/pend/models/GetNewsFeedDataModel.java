package com.pend.models;

import com.pend.BaseResponseModel;

import java.io.Serializable;
import java.util.ArrayList;

public class GetNewsFeedDataModel extends BaseResponseModel {
    public GetPostsData Data;

    public static class GetPostsData implements Serializable {
        public ArrayList<NewsFeedList> newsFeedList;
        public boolean hasNextPage;

    }
        public static class NewsFeedList implements Serializable {

            public   Boolean isContest;
            public  Boolean isExitPoll;
            public  Boolean isPost;
            public  PostData postData;
            public  ExitPollData exitPollData;
            public  ContestData contestData;
            public  class PostData implements Serializable {


                public   Integer postID;
                public   String postInfo;
                public   String imageName;
                public    String imageURL;
                public    Boolean isLike;
                public    Boolean isUnLike;
                public    String createdDatetime;
                public    Integer userID;
                public    String userFullName;
                public    Integer commentCount;
                public    Integer likeCount;
                public    Integer unlikeCount;
                public    String commentUserFullName;
                public    String commentUserImageName;
                public    String commentUserImageURL;
                public   String commentText;
                public   String userImageName;
                public   String userImageNameURL;
                public    Integer mirrorID;
                public    String mirrorName;
                public   String MirrorImageUrl;
                public   String mirrorInfo;
                public    String mirrorWikiLink;

                    public Integer getPostID() {
                        return postID;
                    }

                    public void setPostID(Integer postID) {
                        this.postID = postID;
                    }

                    public String getPostInfo() {
                        return postInfo;
                    }

                    public void setPostInfo(String postInfo) {
                        this.postInfo = postInfo;
                    }

                    public String getImageName() {
                        return imageName;
                    }

                    public void setImageName(String imageName) {
                        this.imageName = imageName;
                    }

                    public String getImageURL() {
                        return imageURL;
                    }

                    public void setImageURL(String imageURL) {
                        this.imageURL = imageURL;
                    }

                    public Boolean getLike() {
                        return isLike;
                    }

                    public void setLike(Boolean like) {
                        isLike = like;
                    }

                    public Boolean getUnLike() {
                        return isUnLike;
                    }

                    public void setUnLike(Boolean unLike) {
                        isUnLike = unLike;
                    }

                    public String getCreatedDatetime() {
                        return createdDatetime;
                    }

                    public void setCreatedDatetime(String createdDatetime) {
                        this.createdDatetime = createdDatetime;
                    }

                    public Integer getUserID() {
                        return userID;
                    }

                    public void setUserID(Integer userID) {
                        this.userID = userID;
                    }

                    public String getUserFullName() {
                        return userFullName;
                    }

                    public void setUserFullName(String userFullName) {
                        this.userFullName = userFullName;
                    }

                    public Integer getCommentCount() {
                        return commentCount;
                    }

                    public void setCommentCount(Integer commentCount) {
                        this.commentCount = commentCount;
                    }

                    public Integer getLikeCount() {
                        return likeCount;
                    }

                    public void setLikeCount(Integer likeCount) {
                        this.likeCount = likeCount;
                    }

                    public Integer getUnlikeCount() {
                        return unlikeCount;
                    }

                    public void setUnlikeCount(Integer unlikeCount) {
                        this.unlikeCount = unlikeCount;
                    }

                    public String getCommentUserFullName() {
                        return commentUserFullName;
                    }

                    public void setCommentUserFullName(String commentUserFullName) {
                        this.commentUserFullName = commentUserFullName;
                    }

                    public String getCommentUserImageName() {
                        return commentUserImageName;
                    }

                    public void setCommentUserImageName(String commentUserImageName) {
                        this.commentUserImageName = commentUserImageName;
                    }

                    public String getCommentUserImageURL() {
                        return commentUserImageURL;
                    }

                    public void setCommentUserImageURL(String commentUserImageURL) {
                        this.commentUserImageURL = commentUserImageURL;
                    }

                    public String getCommentText() {
                        return commentText;
                    }

                    public void setCommentText(String commentText) {
                        this.commentText = commentText;
                    }

                    public String getUserImageName() {
                        return userImageName;
                    }

                    public void setUserImageName(String userImageName) {
                        this.userImageName = userImageName;
                    }

                    public String getUserImageNameURL() {
                        return userImageNameURL;
                    }

                    public void setUserImageNameURL(String userImageNameURL) {
                        this.userImageNameURL = userImageNameURL;
                    }

                    public Integer getMirrorID() {
                        return mirrorID;
                    }

                    public void setMirrorID(Integer mirrorID) {
                        this.mirrorID = mirrorID;
                    }

                    public String getMirrorName() {
                        return mirrorName;
                    }

                    public void setMirrorName(String mirrorName) {
                        this.mirrorName = mirrorName;
                    }

                    public String getMirrorImageUrl() {
                        return MirrorImageUrl;
                    }

                    public void setMirrorImageUrl(String mirrorImageUrl) {
                        MirrorImageUrl = mirrorImageUrl;
                    }

                    public String getMirrorInfo() {
                        return mirrorInfo;
                    }

                    public void setMirrorInfo(String mirrorInfo) {
                        this.mirrorInfo = mirrorInfo;
                    }

                    public String getMirrorWikiLink() {
                        return mirrorWikiLink;
                    }

                    public void setMirrorWikiLink(String mirrorWikiLink) {
                        this.mirrorWikiLink = mirrorWikiLink;
                    }
                }

             public    class ExitPollData implements Serializable {

                 public  Integer exitPollID;
                 public  Integer mirrorID;
                 public   String exitPollText;
                 public   Boolean pollAdmire;
                 public   Boolean pollHate;
                 public   Boolean pollCantSay;
                 public   Integer pollAdmirePer;
                 public   Integer pollHatePer;
                 public  Integer pollCantSayPer;

                    public Integer getExitPollID() {
                        return exitPollID;
                    }

                    public void setExitPollID(Integer exitPollID) {
                        this.exitPollID = exitPollID;
                    }

                    public Integer getMirrorID() {
                        return mirrorID;
                    }

                    public void setMirrorID(Integer mirrorID) {
                        this.mirrorID = mirrorID;
                    }

                    public String getExitPollText() {
                        return exitPollText;
                    }

                    public void setExitPollText(String exitPollText) {
                        this.exitPollText = exitPollText;
                    }

                    public Boolean getPollAdmire() {
                        return pollAdmire;
                    }

                    public void setPollAdmire(Boolean pollAdmire) {
                        this.pollAdmire = pollAdmire;
                    }

                    public Boolean getPollHate() {
                        return pollHate;
                    }

                    public void setPollHate(Boolean pollHate) {
                        this.pollHate = pollHate;
                    }

                    public Boolean getPollCantSay() {
                        return pollCantSay;
                    }

                    public void setPollCantSay(Boolean pollCantSay) {
                        this.pollCantSay = pollCantSay;
                    }

                    public Integer getPollAdmirePer() {
                        return pollAdmirePer;
                    }

                    public void setPollAdmirePer(Integer pollAdmirePer) {
                        this.pollAdmirePer = pollAdmirePer;
                    }

                    public Integer getPollHatePer() {
                        return pollHatePer;
                    }

                    public void setPollHatePer(Integer pollHatePer) {
                        this.pollHatePer = pollHatePer;
                    }

                    public Integer getPollCantSayPer() {
                        return pollCantSayPer;
                    }

                    public void setPollCantSayPer(Integer pollCantSayPer) {
                        this.pollCantSayPer = pollCantSayPer;
                    }
                }

             public    class ContestData implements Serializable {


                 public   Integer contestID;
                 public   Integer contestTypeID;
                 public   Integer createdUserID;
                 public   String createdUserName;
                 public   String questionText;
                 public   String imageName;
                 public   String imageURL;
                 public   Integer relatedMirrorID;
                 public    String relatedMirrorName;
                 public   String relatedMirrorImageURL;
                 public   Integer option1MirrorID;
                 public   String option1MirrorName;
                 public   String option1MirrorImageURL;
                 public   Integer option2MirrorID;
                 public   String option2MirrorName;
                 public   String option2MirrorImageURL;
                 public   Integer option3MirrorID;
                 public   String option3MirrorName;
                 public   String option3MirrorImageURL;
                 public   String option1Text;
                 public   String option2Text;
                 public   String option3Text;
                 public   Boolean option1Vote;
                 public  Boolean option2Vote;
                 public   Boolean option3Vote;
                 public   Integer option1Per;
                 public   Integer option2Per;
                 public  Integer option3Per;
                 public   Integer option1MirrorIDCount;
                 public   Integer option2MirrorIDCount;
                 public   Integer option3MirrorIDCount;
                 public   Integer option1TextCount;
                 public   Integer option2TextCount;
                 public   Integer option3TextCount;
                 public   Integer TypeOneOptions;
                 public   Integer typeTwoOptions;
                 public Integer contestType;

                    public Integer getContestID() {
                        return contestID;
                    }

                    public void setContestID(Integer contestID) {
                        this.contestID = contestID;
                    }

                    public Integer getContestTypeID() {
                        return contestTypeID;
                    }

                    public void setContestTypeID(Integer contestTypeID) {
                        this.contestTypeID = contestTypeID;
                    }

                    public Integer getCreatedUserID() {
                        return createdUserID;
                    }

                    public void setCreatedUserID(Integer createdUserID) {
                        this.createdUserID = createdUserID;
                    }

                    public String getCreatedUserName() {
                        return createdUserName;
                    }

                    public void setCreatedUserName(String createdUserName) {
                        this.createdUserName = createdUserName;
                    }

                    public String getQuestionText() {
                        return questionText;
                    }

                    public void setQuestionText(String questionText) {
                        this.questionText = questionText;
                    }

                    public String getImageName() {
                        return imageName;
                    }

                    public void setImageName(String imageName) {
                        this.imageName = imageName;
                    }

                    public String getImageURL() {
                        return imageURL;
                    }

                    public void setImageURL(String imageURL) {
                        this.imageURL = imageURL;
                    }

                    public Integer getRelatedMirrorID() {
                        return relatedMirrorID;
                    }

                    public void setRelatedMirrorID(Integer relatedMirrorID) {
                        this.relatedMirrorID = relatedMirrorID;
                    }

                    public String getRelatedMirrorName() {
                        return relatedMirrorName;
                    }

                    public void setRelatedMirrorName(String relatedMirrorName) {
                        this.relatedMirrorName = relatedMirrorName;
                    }

                    public String getRelatedMirrorImageURL() {
                        return relatedMirrorImageURL;
                    }

                    public void setRelatedMirrorImageURL(String relatedMirrorImageURL) {
                        this.relatedMirrorImageURL = relatedMirrorImageURL;
                    }

                    public Integer getOption1MirrorID() {
                        return option1MirrorID;
                    }

                    public void setOption1MirrorID(Integer option1MirrorID) {
                        this.option1MirrorID = option1MirrorID;
                    }

                    public String getOption1MirrorName() {
                        return option1MirrorName;
                    }

                    public void setOption1MirrorName(String option1MirrorName) {
                        this.option1MirrorName = option1MirrorName;
                    }

                    public String getOption1MirrorImageURL() {
                        return option1MirrorImageURL;
                    }

                    public void setOption1MirrorImageURL(String option1MirrorImageURL) {
                        this.option1MirrorImageURL = option1MirrorImageURL;
                    }

                    public Integer getOption2MirrorID() {
                        return option2MirrorID;
                    }

                    public void setOption2MirrorID(Integer option2MirrorID) {
                        this.option2MirrorID = option2MirrorID;
                    }

                    public String getOption2MirrorName() {
                        return option2MirrorName;
                    }

                    public void setOption2MirrorName(String option2MirrorName) {
                        this.option2MirrorName = option2MirrorName;
                    }

                    public String getOption2MirrorImageURL() {
                        return option2MirrorImageURL;
                    }

                    public void setOption2MirrorImageURL(String option2MirrorImageURL) {
                        this.option2MirrorImageURL = option2MirrorImageURL;
                    }

                    public Integer getOption3MirrorID() {
                        return option3MirrorID;
                    }

                    public void setOption3MirrorID(Integer option3MirrorID) {
                        this.option3MirrorID = option3MirrorID;
                    }

                    public String getOption3MirrorName() {
                        return option3MirrorName;
                    }

                    public void setOption3MirrorName(String option3MirrorName) {
                        this.option3MirrorName = option3MirrorName;
                    }

                    public String getOption3MirrorImageURL() {
                        return option3MirrorImageURL;
                    }

                    public void setOption3MirrorImageURL(String option3MirrorImageURL) {
                        this.option3MirrorImageURL = option3MirrorImageURL;
                    }

                    public String getOption1Text() {
                        return option1Text;
                    }

                    public void setOption1Text(String option1Text) {
                        this.option1Text = option1Text;
                    }

                    public String getOption2Text() {
                        return option2Text;
                    }

                    public void setOption2Text(String option2Text) {
                        this.option2Text = option2Text;
                    }

                    public String getOption3Text() {
                        return option3Text;
                    }

                    public void setOption3Text(String option3Text) {
                        this.option3Text = option3Text;
                    }

                    public Boolean getOption1Vote() {
                        return option1Vote;
                    }

                    public void setOption1Vote(Boolean option1Vote) {
                        this.option1Vote = option1Vote;
                    }

                    public Boolean getOption2Vote() {
                        return option2Vote;
                    }

                    public void setOption2Vote(Boolean option2Vote) {
                        this.option2Vote = option2Vote;
                    }

                    public Boolean getOption3Vote() {
                        return option3Vote;
                    }

                    public void setOption3Vote(Boolean option3Vote) {
                        this.option3Vote = option3Vote;
                    }

                    public Integer getOption1Per() {
                        return option1Per;
                    }

                    public void setOption1Per(Integer option1Per) {
                        this.option1Per = option1Per;
                    }

                    public Integer getOption2Per() {
                        return option2Per;
                    }

                    public void setOption2Per(Integer option2Per) {
                        this.option2Per = option2Per;
                    }

                    public Integer getOption3Per() {
                        return option3Per;
                    }

                    public void setOption3Per(Integer option3Per) {
                        this.option3Per = option3Per;
                    }

                    public Integer getOption1MirrorIDCount() {
                        return option1MirrorIDCount;
                    }

                    public void setOption1MirrorIDCount(Integer option1MirrorIDCount) {
                        this.option1MirrorIDCount = option1MirrorIDCount;
                    }

                    public Integer getOption2MirrorIDCount() {
                        return option2MirrorIDCount;
                    }

                    public void setOption2MirrorIDCount(Integer option2MirrorIDCount) {
                        this.option2MirrorIDCount = option2MirrorIDCount;
                    }

                    public Integer getOption3MirrorIDCount() {
                        return option3MirrorIDCount;
                    }

                    public void setOption3MirrorIDCount(Integer option3MirrorIDCount) {
                        this.option3MirrorIDCount = option3MirrorIDCount;
                    }

                    public Integer getOption1TextCount() {
                        return option1TextCount;
                    }

                    public void setOption1TextCount(Integer option1TextCount) {
                        this.option1TextCount = option1TextCount;
                    }

                    public Integer getOption2TextCount() {
                        return option2TextCount;
                    }

                    public void setOption2TextCount(Integer option2TextCount) {
                        this.option2TextCount = option2TextCount;
                    }

                    public Integer getOption3TextCount() {
                        return option3TextCount;
                    }

                    public void setOption3TextCount(Integer option3TextCount) {
                        this.option3TextCount = option3TextCount;
                    }

                    public Integer getTypeOneOptions() {
                        return TypeOneOptions;
                    }

                    public void setTypeOneOptions(Integer typeOneOptions) {
                        TypeOneOptions = typeOneOptions;
                    }

                 public Integer getTypeTwoOptions() {
                     return typeTwoOptions;
                 }

                 public void setTypeTwoOptions(Integer typeTwoOptions) {
                     this.typeTwoOptions = typeTwoOptions;
                 }

                 public Integer getContestType() {
                     return contestType;
                 }

                 public void setContestType(Integer contestType) {
                     this.contestType = contestType;
                 }
             }
            }
        }


