package com.example.project2021.profile;

import kotlin.internal.UProgressionUtilKt;

public class Memberinfo {
    private String name;
    private String photoUrl;
    private String address;
    private String type;
    private String id;

    public  Memberinfo (String name, String photoUrl, String address, String type, String id) {
        this.name = name;
        this.photoUrl = photoUrl;
        this.address = address;
        this.type = type;
        this.id = id;
    }

    public  Memberinfo (String name, String address, String type, String id) {
        this.name = name;
        this.address = address;
        this.type = type;
        this.id = id;
    }

    public  Memberinfo(String name, String type, String id){
        this.name = name;
        this.type = type;
        this.id = id;
    }

    public String getName () {
        return this.name;
    }

    public void setName (String name) {
        this.name=name;
    }

    public String getPhotoUrl () {
        return this.photoUrl;
    }

    public void setPhotoUrl (String photoUrl) {
        this.name=photoUrl;
    }

    public String getAddress () {
        return this.address;
    }

    public void setAddress (String address) {
        this.address=address;
    }

    public String getType () {
        return this.type;
    }

    public void setType (String type) { this.type=type; }

    public String getId() { return id; }

    public void setId(String id) { this.id = id; }
}
