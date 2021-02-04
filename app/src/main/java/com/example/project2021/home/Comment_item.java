package com.example.project2021.home;

import java.util.Date;

public class Comment_item {
    public String nameStr ;
    public String commentStr ;
    public Date timeStr ;


    public Comment_item(String name, String comment, Date time){
        this.nameStr = name;
        this.commentStr = comment;
        this.timeStr = time;
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

    public Date getTime() {
        return timeStr;
    }

    public void setTime(Date time) {
        this.timeStr = time;
    }
}
