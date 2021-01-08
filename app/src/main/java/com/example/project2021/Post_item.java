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
}