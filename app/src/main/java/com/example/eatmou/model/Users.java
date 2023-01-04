package com.example.eatmou.model;

import com.google.firebase.Timestamp;

import java.text.SimpleDateFormat;
import java.util.Map;

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

    public static Users toObject(Map<String, Object> map) {
        Users user = new Users(
                (String) map.get("bio"),
                ((Timestamp) map.get("dob")),
                (String) map.get("email"),
                (String) map.get("location"),
                (String) map.get("profileBgPicUrl"),
                (String) map.get("profilePicUrl"),
                (String) map.get("username"),
                (String) map.get("userID"));
        return user;
    }

    public String getDOBText() {
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        return formatter.format(this.dob.toDate());
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
