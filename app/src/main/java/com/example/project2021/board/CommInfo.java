package com.example.project2021.board;

import android.widget.TextView;

import java.util.Date;

public class CommInfo {

    private String type ;
    private String name ;
    private String comment ;
    private String address;
    private Date createdAt;
    private String created_at;
    private String id;
    private int commentCount;


    public CommInfo(String name, String comment, String  created_at) {
        this.name = name;
        this.comment = comment;
        this.created_at = created_at;
    }

    public CommInfo(String name, String comment, Date createdAt) {
        this.name = name;
        this.comment = comment;
        this.createdAt = createdAt;
    }

    public String getCreated_at() { return created_at; }

    public void setCreated_at(String created_at) { this.created_at = created_at; }

    public String  getType(){
        return type;
    }

    public void setType(String type){
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Date getCreatedAt () {
        return this.createdAt;
    }

    public void setCreatedAt (Date createdAt) {
        this.createdAt=createdAt;
    }

    public String getAddress() { return address; }

    public void setAddress(String address) { this.address = address; }

    public String getId() { return id; }

    public void setId(String id) { this.id = id; }

    public int getCommentCount() { return commentCount; }

    public void setCommentCount(int commentCount) { this.commentCount = commentCount; }
}
