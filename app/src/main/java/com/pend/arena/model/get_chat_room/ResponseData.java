package com.pend.arena.model.get_chat_room;

public class ResponseData {


    private Integer chatRoomID;
    private Integer userID;
    private Integer selectedUserID;


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
}
