package com.example.project2021.board;

import java.util.ArrayList;
import java.util.Date;

public class PostInfo {
    private String contents;
    private String publisher;
    private Date createdAt;


    public PostInfo(String contents, String publisher, Date createdAt) {
        this.contents=contents;
        this.publisher=publisher;
        this.createdAt=createdAt;
    }

    public String getContents () {
        return this.contents;
    }

    public void setContents (String contents) {
        this.contents=contents;
    }

    public String getPublisher () {
        return this.publisher;
    }

    public void setPublisher (String publisher) {
        this.publisher=publisher;
    }

    public Date getCreatedAt () {
        return this.createdAt;
    }

    public void setCreatedAt (Date createdAt) {
        this.createdAt=createdAt;
    }



}