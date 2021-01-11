package com.example.project2021;


public class Post_item {

    public int img_profile ;
    public String nameStr ;
    public String addressStr ;
    public String textStr ;


    public Post_item(int profile, String name, String add, String text){
        this.img_profile = profile;
        this.nameStr = name;
        this.addressStr = add;
        this.textStr = text;
    }

    public int getProfile(){
        return img_profile;
    }

    public void setProfile(int profile){
        this.img_profile = profile;
    }

    public String getName() {
        return nameStr;
    }

    public void setName(String name) {
        this.nameStr = name;
    }

    public String getAddress() {
        return addressStr;
    }

    public void setAddress(String add) {
        this.addressStr = add;
    }

    public String getText() {
        return textStr;
    }

    public void setText(String text) {
        this.textStr = text;
    }
}