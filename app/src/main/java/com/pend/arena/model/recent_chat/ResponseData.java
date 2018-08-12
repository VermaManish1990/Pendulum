package com.pend.arena.model.recent_chat;

public class ResponseData {

      private  Integer chatRoomID;
      private Integer userID;
      private String userFullName;
      private String lastChatDate;
      private String  imageName;
      private String  imageURL;
      private Integer unRead;

    public Integer getChatRoomID() {
        return chatRoomID;
    }

    public void setChatRoomID(Integer chatRoomID) {
        this.chatRoomID = chatRoomID;
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

    public String getLastChatDate() {
        return lastChatDate;
    }

    public void setLastChatDate(String lastChatDate) {
        this.lastChatDate = lastChatDate;
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

    public Integer getUnRead() {
        return unRead;
    }

    public void setUnRead(Integer unRead) {
        this.unRead = unRead;
    }
}
