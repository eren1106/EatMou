package com.example.eatmou.model;

public class Users {
    private String userID;
    private String username;
    private String bio;
    private String location;
    private String personal_info;
    private String profile_pic;

    public Users(String userID, String username, String bio, String location, String personal_info, String profile_pic) {
        this.userID = userID;
        this.username = username;
        this.bio = bio;
        this.location = location;
        this.personal_info = personal_info;
        this.profile_pic = profile_pic;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getPersonal_info() {
        return personal_info;
    }

    public void setPersonal_info(String personal_info) {
        this.personal_info = personal_info;
    }

    public String getProfile_pic() {
        return profile_pic;
    }

    public void setProfile_pic(String profile_pic) {
        this.profile_pic = profile_pic;
    }
}
