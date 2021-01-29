package com.example.project2021.board;

import android.widget.TextView;

import java.util.Date;

public class CommInfo {

    private String name ;
    private String comment ;
    private Date created_at;
    private String commentId;
    private int commentCount;


    public CommInfo(String name, String comment, Date created_at, String commentId) {
        this.name = name;
        this.comment = comment;
        this.created_at = created_at;
        this.commentId = commentId;
    }

    public String getCommentId() { return commentId; }

    public void setCommentId(String commentId) { this.commentId = commentId; }

    public Date getCreated_at() { return created_at; }

    public void setCreated_at(Date created_at) { this.created_at = created_at; }

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

    public int getCommentCount() { return commentCount; }

    public void setCommentCount(int commentCount) { this.commentCount = commentCount; }
}
