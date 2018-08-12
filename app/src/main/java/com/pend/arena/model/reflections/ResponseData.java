package com.pend.arena.model.reflections;

public class ResponseData {

      Integer userID;
      String userFullName;
      String imageName;
      String imageURL;
      Integer mirrorCount;


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

    public Integer getMirrorCount() {
        return mirrorCount;
    }

    public void setMirrorCount(Integer mirrorCount) {
        this.mirrorCount = mirrorCount;
    }
}
