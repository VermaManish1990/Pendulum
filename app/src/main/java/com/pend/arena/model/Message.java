package com.pend.arena.model;

public class Message {

     private Integer chatRoomID;
     private Integer userID;
     private Integer selectedUserID;
     private String messageText;

    public Message(Integer chatRoomID, Integer userID, Integer selectedUserID, String messageText) {
        this.chatRoomID = chatRoomID;
        this.userID = userID;
        this.selectedUserID = selectedUserID;
        this.messageText = messageText;
    }

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

    public Integer getSelectedUserID() {
        return selectedUserID;
    }

    public void setSelectedUserID(Integer selectedUserID) {
        this.selectedUserID = selectedUserID;
    }

    public String getMessageText() {
        return messageText;
    }

    public void setMessageText(String messageText) {
        this.messageText = messageText;
    }
}

