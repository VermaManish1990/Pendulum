package com.pend.arena.view;

public class ChatDataItem {

    private Boolean isSender;
    private String name;

    private String message;
    private String image;

    public ChatDataItem(Boolean isSender, String name, String message, String image) {
        this.isSender = isSender;
        this.name = name;
        this.message = message;
        this.image = image;
    }

    public Boolean getSender() {
        return isSender;
    }

    public void setSender(Boolean sender) {
        isSender = sender;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
