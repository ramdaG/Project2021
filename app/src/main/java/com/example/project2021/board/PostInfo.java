package com.example.project2021.board;

import android.provider.Settings;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class PostInfo implements Serializable {
    private String contents;
    private String publisher;
    private Date createdAt;
    private String id;
    private int likesCount;
    boolean userLike = false;
    String likeId = publisher;
    private String name;
    private String address;
    private String type;
    private String photoUrl;


    public PostInfo(String contents, String publisher, Date createdAt, String id) {
        this.contents=contents;
        this.publisher=publisher;
        this.createdAt=createdAt;
        this.id=id;
    }

    public PostInfo(String contents, String publisher, Date createdAt) {
        this.contents=contents;
        this.publisher=publisher;
        this.createdAt=createdAt;
    }

    public PostInfo(String name, String address, String photoUrl, String type) {

        this.name = name;
        this.address = address;
        this.type = type;
        this.photoUrl = photoUrl;
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

    public String getId () {
        return this.id;
    }

    public void setId (String id) {
        this.id=id;
    }

    public void setLikesCount(int likesCount) { this.likesCount = likesCount; }

    public int getLikesCount() { return likesCount; }

    public void setUserLiked(boolean userLike) { this.userLike = userLike; }

    public boolean isUserLiked() { return userLike; }

    public void setLikeId(String likeId) { this.likeId = likeId; }

    public String getLikeId() { return likeId; }

    public String getName() { return name; }

    public void setName(String name) { this.name = name; }

    public String getAddress() { return address; }

    public void setAddress(String address) { this.address = address; }

    public String getPhotoUrl() { return photoUrl; }

    public void setPhotoUrl(String photoUrl) { this.photoUrl = photoUrl; }

    public String getType() { return type; }

    public void setType(String type) { this.type = type; }
}