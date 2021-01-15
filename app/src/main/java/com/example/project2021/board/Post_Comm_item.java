package com.example.project2021.board;

import android.widget.TextView;

public class Post_Comm_item {

    public int img_type ;
    public String nameStr ;
    public String commentStr ;
    public String timeStr ;


    public Post_Comm_item(int type, String name, String comment, String time){
        this.img_type = type;
        this.nameStr = name;
        this.commentStr = comment;
        this.timeStr = time;
    }

    public int getType(){
        return img_type;
    }

    public void setType(int type){
        this.img_type = type;
    }

    public String getName() {
        return nameStr;
    }

    public void setName(String name) {
        this.nameStr = name;
    }

    public String getComment() {
        return commentStr;
    }

    public void setComment(String comment) {
        this.commentStr = comment;
    }

    public String getTime() {
        return timeStr;
    }

    public void setTime(String time) {
        this.timeStr = time;
    }
}
