package com.pend.arena.view;

public class ChatItem {

    String image ;
    String name ;
    String count;

    public ChatItem(String image, String name, String count) {
        this.image = image;
        this.name = name;
        this.count = count;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
    }
}
