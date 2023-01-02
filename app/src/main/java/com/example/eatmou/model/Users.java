package com.example.eatmou.model;

import com.google.firebase.Timestamp;

public class Users {
    private String bio;
    private Timestamp dob;
    private String email;
    private String location;
    private String profileBgPicUrl;
    private String profilePicUrl;
    private String userID;
    private String username;


    public Users(){}

    public Users(String bio, Timestamp dob, String email, String location, String profileBgPicUrl, String profilePicUrl, String username, String userID) {
        this.bio = bio;
        this.dob = dob;
        this.email = email;
        this.location = location;
        this.profileBgPicUrl = profileBgPicUrl;
        this.profilePicUrl = profilePicUrl;
        this.username = username;
        this.userID = userID;
    }

    public String getBio() {
        return bio;
    }

    public Timestamp getDob() {
        return dob;
    }

    public String getEmail() {
        return email;
    }

    public String getLocation() {
        return location;
    }

    public String getProfileBgPicUrl() {
        return profileBgPicUrl;
    }

    public String getProfilePicUrl() {
        return profilePicUrl;
    }

    public String getUsername() {
        return username;
    }

    public String getUserID() {
        return userID;
    }
}
