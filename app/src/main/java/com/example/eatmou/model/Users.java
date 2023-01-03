package com.example.eatmou.model;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Users implements Serializable {
    private String bio;
    private Date dob;
    private String email;
    private String location;
    private String profileBgPicUrl;
    private String profilePicUrl;
    private String userID;
    private String username;


    public Users(){}

    public Users(String bio, Date dob, String email, String location, String profileBgPicUrl, String profilePicUrl, String username, String userID) {
        this.bio = bio;
        this.dob = dob;
        this.email = email;
        this.location = location;
        this.profileBgPicUrl = profileBgPicUrl;
        this.profilePicUrl = profilePicUrl;
        this.username = username;
        this.userID = userID;
    }

    public String getDobText() {
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        return formatter.format(this.dob);
    }

    public String getBio() {
        return bio;
    }

    public Date getDob() {
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
