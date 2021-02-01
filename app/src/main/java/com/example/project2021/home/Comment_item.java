package com.example.project2021.home;

import java.util.Date;

public class Comment_item {

    //public int img_type ;
    public String nameStr ;
    public String commentStr ;
    public Date timeStr ;


    public Comment_item(String name, String comment, Date time){
        //this.img_type = type;
        this.nameStr = name;
        this.commentStr = comment;
        this.timeStr = time;
    }



//    public int getType(){
//        return img_type;
//    }

    //public void setType(int type){
//        this.img_type = type;
//    }

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
