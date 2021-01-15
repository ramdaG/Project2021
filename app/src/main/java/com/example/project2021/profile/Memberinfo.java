package com.example.project2021.profile;

public class Memberinfo {
    private String name;
    private String photoUrl;

    public  Memberinfo (String name, String photoUrl) {
        this.name=name;
        this.photoUrl=photoUrl;

    }

    public  Memberinfo (String name) {
        this.name=name;

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


}
