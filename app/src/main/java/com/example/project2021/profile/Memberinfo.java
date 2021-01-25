package com.example.project2021.profile;

public class Memberinfo {
    private String name;
    private String photoUrl;
    private String address;
    private String type;

    public  Memberinfo (String name, String photoUrl, String address, String type) {
        this.name = name;
        this.photoUrl = photoUrl;
        this.address = address;
        this.type = type;
    }

    public  Memberinfo (String name, String address, String type) {
        this.name = name;
        this.address = address;
        this.type = type;
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
}
