package com.pend.models;

import java.io.Serializable;

public class ContestData implements Serializable{

    public int contestType;
    public int contestID;
    public int contestTypeID;

    public int createdUserID;
    public String createdUserName;

    public String questionText;
    public String imageName;
    public String imageURL;

    public int relatedMirrorID;
    public String relatedMirrorName;
    public String relatedMirrorImageURL;

    public int option1MirrorID;
    public String option1MirrorName;
    public String option1MirrorImageURL;

    public int option2MirrorID;
    public String option2MirrorName;
    public String option2MirrorImageURL;

    public int option3MirrorID;
    public String option3MirrorName;
    public String option3MirrorImageURL;

    public String option1Text;
    public String option2Text;
    public String option3Text;

    public boolean option1Vote;
    public boolean option2Vote;
    public boolean option3Vote;

    public int option1Per;
    public int option2Per;
    public int option3Per;

    public int option1MirrorIDCount;
    public int option2MirrorIDCount;
    public int option3MirrorIDCount;

    public int option1TextCount;
    public int option2TextCount;
    public int option3TextCount;

    public int TypeOneOptions;

    public  int typeTwoOptions;

    public int getContestType() {
        return contestType;
    }

    public void setContestType(int contestType) {
        this.contestType = contestType;
    }

    public int getContestID() {
        return contestID;
    }

    public void setContestID(int contestID) {
        this.contestID = contestID;
    }

    public int getContestTypeID() {
        return contestTypeID;
    }

    public void setContestTypeID(int contestTypeID) {
        this.contestTypeID = contestTypeID;
    }

    public int getCreatedUserID() {
        return createdUserID;
    }

    public void setCreatedUserID(int createdUserID) {
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

    public int getRelatedMirrorID() {
        return relatedMirrorID;
    }

    public void setRelatedMirrorID(int relatedMirrorID) {
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

    public int getOption1MirrorID() {
        return option1MirrorID;
    }

    public void setOption1MirrorID(int option1MirrorID) {
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

    public int getOption2MirrorID() {
        return option2MirrorID;
    }

    public void setOption2MirrorID(int option2MirrorID) {
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

    public int getOption3MirrorID() {
        return option3MirrorID;
    }

    public void setOption3MirrorID(int option3MirrorID) {
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

    public boolean isOption1Vote() {
        return option1Vote;
    }

    public void setOption1Vote(boolean option1Vote) {
        this.option1Vote = option1Vote;
    }

    public boolean isOption2Vote() {
        return option2Vote;
    }

    public void setOption2Vote(boolean option2Vote) {
        this.option2Vote = option2Vote;
    }

    public boolean isOption3Vote() {
        return option3Vote;
    }

    public void setOption3Vote(boolean option3Vote) {
        this.option3Vote = option3Vote;
    }

    public int getOption1Per() {
        return option1Per;
    }

    public void setOption1Per(int option1Per) {
        this.option1Per = option1Per;
    }

    public int getOption2Per() {
        return option2Per;
    }

    public void setOption2Per(int option2Per) {
        this.option2Per = option2Per;
    }

    public int getOption3Per() {
        return option3Per;
    }

    public void setOption3Per(int option3Per) {
        this.option3Per = option3Per;
    }

    public int getOption1MirrorIDCount() {
        return option1MirrorIDCount;
    }

    public void setOption1MirrorIDCount(int option1MirrorIDCount) {
        this.option1MirrorIDCount = option1MirrorIDCount;
    }

    public int getOption2MirrorIDCount() {
        return option2MirrorIDCount;
    }

    public void setOption2MirrorIDCount(int option2MirrorIDCount) {
        this.option2MirrorIDCount = option2MirrorIDCount;
    }

    public int getOption3MirrorIDCount() {
        return option3MirrorIDCount;
    }

    public void setOption3MirrorIDCount(int option3MirrorIDCount) {
        this.option3MirrorIDCount = option3MirrorIDCount;
    }

    public int getOption1TextCount() {
        return option1TextCount;
    }

    public void setOption1TextCount(int option1TextCount) {
        this.option1TextCount = option1TextCount;
    }

    public int getOption2TextCount() {
        return option2TextCount;
    }

    public void setOption2TextCount(int option2TextCount) {
        this.option2TextCount = option2TextCount;
    }

    public int getOption3TextCount() {
        return option3TextCount;
    }

    public void setOption3TextCount(int option3TextCount) {
        this.option3TextCount = option3TextCount;
    }

    public int getTypeOneOptions() {
        return TypeOneOptions;
    }

    public void setTypeOneOptions(int typeOneOptions) {
        TypeOneOptions = typeOneOptions;
    }

    public int getTypeTwoOptions() {
        return typeTwoOptions;
    }

    public void setTypeTwoOptions(int typeTwoOptions) {
        this.typeTwoOptions = typeTwoOptions;
    }
}
