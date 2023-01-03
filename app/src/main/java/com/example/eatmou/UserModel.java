package com.example.eatmou;

import com.google.firebase.Timestamp;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class UserModel {
    private String userID;
    private String username;
    private String email;
    private Date dob;
    private String bio;
    private String location;
    private String profilePicUrl;
    private String profileBgPicUrl;

    public UserModel(String userID, String username, String email, Date dob, String bio, String location, String profilePicUrl, String profileBgPicUrl) {
        this.userID = userID;
        this.username = username;
        this.email = email;
        this.dob = dob;
        this.bio = bio;
        this.location = location;
        this.profilePicUrl = profilePicUrl;
        this.profileBgPicUrl = profileBgPicUrl;
    }

    public Map<String, Object> toMap() {
        Map<String, Object> map = new HashMap<>();
        map.put("userID", userID);
        map.put("username", username);
        map.put("email", email);
        map.put("dob", dob);
        map.put("bio", bio);
        map.put("location", location);
        map.put("profilePicUrl", profilePicUrl);
        map.put("profileBgPicUrl", profileBgPicUrl);

        return map;
    }

    public static UserModel toObject(Map<String, Object> map) {
        UserModel user = new UserModel(
                (String) map.get("userID"),
                (String) map.get("username"),
                (String) map.get("email"),
                ((Timestamp) map.get("dob")).toDate(),
                (String) map.get("bio"),
                (String) map.get("location"),
                (String) map.get("profilePicUrl"),
                (String) map.get("profileBgPicUrl")
        );

        return user;
    }

    public String getUserID() {
        return userID;
    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public Date getDob() {
        return dob;
    }

    public String getBio() {
        return bio;
    }

    public String getLocation() {
        return location;
    }

    public String getProfilePicUrl() {
        return profilePicUrl;
    }

    public String getProfileBgPicUrl() {
        return profileBgPicUrl;
    }
}
